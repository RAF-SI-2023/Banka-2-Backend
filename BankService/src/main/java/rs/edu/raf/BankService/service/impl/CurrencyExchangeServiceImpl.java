package rs.edu.raf.BankService.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.data.dto.*;
import rs.edu.raf.BankService.data.entities.accounts.Account;
import rs.edu.raf.BankService.data.entities.accounts.ForeignCurrencyAccount;
import rs.edu.raf.BankService.data.entities.accounts.ForeignCurrencyHolder;
import rs.edu.raf.BankService.data.entities.exchangeCurrency.ExchangeRates;
import rs.edu.raf.BankService.data.entities.exchangeCurrency.ExchangeTransferTransactionDetails;
import rs.edu.raf.BankService.data.enums.TransactionStatus;
import rs.edu.raf.BankService.exception.AccountNotFoundException;
import rs.edu.raf.BankService.exception.NotEnoughFundsException;
import rs.edu.raf.BankService.mapper.ExchangeRatesMapper;
import rs.edu.raf.BankService.mapper.ExchangeTransferDetailsMapper;
import rs.edu.raf.BankService.repository.AccountRepository;
import rs.edu.raf.BankService.repository.ExchangeRateRepository;
import rs.edu.raf.BankService.repository.ForeignCurrencyHolderRepository;
import rs.edu.raf.BankService.repository.TransactionRepository;
import rs.edu.raf.BankService.service.CurrencyExchangeService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {
    private final ExchangeRateRepository exchangeRateRepository;
    private final ExchangeRatesMapper exchangeRatesMapper;
    private final ExchangeTransferDetailsMapper exchangeTransferDetailsMapper;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final ForeignCurrencyHolderRepository foreignCurrencyHolderRepository;

    @Override
    public List<ExchangeRatesDto> getAllExchangeRates() {
        return exchangeRateRepository.findAll().stream()
                .map(exchangeRatesMapper::exchangeRatesToExchangeRatesDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ExchangeRatesDto> getExchangeRatesForCurrency(String currencyCode) {
        List<ExchangeRates> exchangeRates = exchangeRateRepository.findAll();
        return exchangeRates.stream()
                .filter(exchangeRate -> exchangeRate.getFromCurrency().equals(currencyCode))
                .map(exchangeRatesMapper::exchangeRatesToExchangeRatesDto)
                .collect(Collectors.toList());

    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ExchangeTransferDetailsDto exchangeCurrency(ExchangeRequestDto exchangeRequestDto) {
        Account from = accountRepository.findByAccountNumber(exchangeRequestDto.getFromAccount());
        Account to = accountRepository.findByAccountNumber(exchangeRequestDto.getToAccount());
        List<Account> bankAccounts = accountRepository.findAllByEmail("bankAccount@bank.rs");

        if (from == null || to == null) {
            throw new AccountNotFoundException("Account not found");
        }
        if (!from.getEmail().equals(to.getEmail())) {
            throw new RuntimeException("Accounts are not owned by the same user");
        }

        if (exchangeRequestDto.getFromCurrency().equals(exchangeRequestDto.getToCurrency())) {
            throw new RuntimeException("Cannot exchange same currency");
        }
        ForeignCurrencyHolder fcHolderFrom = null;
        ForeignCurrencyHolder fcHolderTo = null;
        if (from instanceof ForeignCurrencyAccount foreignCurrencyAccount) {
            fcHolderFrom = foreignCurrencyAccount.getForeignCurrencyHolders().stream()
                    .filter(foreignCurrencyHolder -> foreignCurrencyHolder.getCurrencyCode().equals(exchangeRequestDto.getFromCurrency())).
                    findFirst().orElseThrow(() -> new RuntimeException("Currency not found"));
        }
        if (to instanceof ForeignCurrencyAccount foreignCurrencyAccount) {
            fcHolderTo = foreignCurrencyAccount.getForeignCurrencyHolders().stream()
                    .filter(foreignCurrencyHolder -> foreignCurrencyHolder.getCurrencyCode().equals(exchangeRequestDto.getToCurrency())).
                    findFirst().orElseThrow(() -> new RuntimeException("Currency not found"));
        }
        if (fcHolderFrom == null && fcHolderTo == null) {
            throw new RuntimeException("Both accounts are domestic");
        }

        ExchangeRates exchangeRate = exchangeRateRepository.findByFromCurrencyAndToCurrency(exchangeRequestDto.getFromCurrency(), exchangeRequestDto.getToCurrency());
        if (exchangeRate == null) {
            throw new RuntimeException("Exchange rate not found");
        }

        Account bankAccountReceiver = bankAccounts.stream().filter(account -> account.getCurrencyCode().equals(exchangeRequestDto.getFromCurrency())).findFirst().orElse(null);
        Account bankAccountSender = bankAccounts.stream().filter(account -> account.getCurrencyCode().equals(exchangeRequestDto.getToCurrency())).findFirst().orElse(null);

        if (bankAccountReceiver == null || bankAccountSender == null) {
            throw new AccountNotFoundException("Bank account not found");
        }

        double exchangeRateValue = exchangeRate.getExchangeRate();
        Double amountToSubtract = exchangeRequestDto.getAmount() * exchangeRateValue;
        if (fcHolderFrom != null) {
            if (fcHolderFrom.getAvailableBalance() < exchangeRequestDto.getAmount()) {
                throw new NotEnoughFundsException();
            }
        } else if (from.getAvailableBalance() < exchangeRequestDto.getAmount()) {
            throw new NotEnoughFundsException();
        }


        if (fcHolderFrom != null) {
            fcHolderFrom.setAvailableBalance((long) (fcHolderFrom.getAvailableBalance() - exchangeRequestDto.getAmount()));
            foreignCurrencyHolderRepository.save(fcHolderFrom);
        } else {
            from.setAvailableBalance((long) (from.getAvailableBalance() - exchangeRequestDto.getAmount()));
            accountRepository.save(from);
        }
        bankAccountReceiver.setAvailableBalance((long) (bankAccountReceiver.getAvailableBalance() + exchangeRequestDto.getAmount()));
        accountRepository.save(bankAccountReceiver);


        if (fcHolderTo != null) {
            fcHolderTo.setAvailableBalance((long) (fcHolderTo.getAvailableBalance() + amountToSubtract));
            foreignCurrencyHolderRepository.save(fcHolderTo);
        } else {
            to.setAvailableBalance((long) (to.getAvailableBalance() + amountToSubtract));
            accountRepository.save(to);
        }
        bankAccountSender.setAvailableBalance((long) (bankAccountSender.getAvailableBalance() - amountToSubtract));
        accountRepository.save(bankAccountSender);

        ExchangeTransferTransactionDetails exchangeTransferDetails = new ExchangeTransferTransactionDetails();
        exchangeTransferDetails.setExchangeRate(exchangeRateValue);
        exchangeTransferDetails.setAmount(exchangeRequestDto.getAmount());
        exchangeTransferDetails.setFromCurrency(from.getCurrencyCode());
        exchangeTransferDetails.setToCurrency(to.getCurrencyCode());
        exchangeTransferDetails.setSenderAccount(from);
        exchangeTransferDetails.setReceiverAccount(to);
        exchangeTransferDetails.setFee(0);
        exchangeTransferDetails.setTotalAmount(amountToSubtract);
        exchangeTransferDetails.setCreatedAt(LocalDateTime.now());
        exchangeTransferDetails.setStatus(TransactionStatus.CONFIRMED);
        exchangeTransferDetails = transactionRepository.save(exchangeTransferDetails);

        return exchangeTransferDetailsMapper.exchangeTransferDetailsToExchangeTransferDetailsDto(exchangeTransferDetails);

    }

}
