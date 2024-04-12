package rs.edu.raf.BankService.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.data.dto.*;
import rs.edu.raf.BankService.data.entities.accounts.Account;
import rs.edu.raf.BankService.data.entities.accounts.DomesticCurrencyAccount;
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

        if (from.getCurrencyCode().equals(to.getCurrencyCode())) {
            throw new RuntimeException("Cannot exchange same currency");
        }

        if (from instanceof DomesticCurrencyAccount && to instanceof DomesticCurrencyAccount) {
            throw new RuntimeException("Cannot exchange between domestic currency accounts");
        }

        ExchangeRates exchangeRate = exchangeRateRepository.findByFromCurrencyAndToCurrency(from.getCurrencyCode(), to.getCurrencyCode());
        if (exchangeRate == null) {
            throw new RuntimeException("Exchange rate not found");
        }

        Account bankAccountReceiver = bankAccounts.stream().filter(account -> account.getCurrencyCode().equals(from.getCurrencyCode())).findFirst().orElse(null);
        Account bankAccountSender = bankAccounts.stream().filter(account -> account.getCurrencyCode().equals(to.getCurrencyCode())).findFirst().orElse(null);

        if (bankAccountReceiver == null || bankAccountSender == null) {
            throw new AccountNotFoundException("Bank account not found");
        }

        double exchangeRateValue = exchangeRate.getExchangeRate();
        Double amountToSubtract = exchangeRequestDto.getAmount() * exchangeRateValue;
        if (from.getAvailableBalance() < exchangeRequestDto.getAmount()) {
            throw new NotEnoughFundsException();
        }


        from.setAvailableBalance((long) (from.getAvailableBalance() - exchangeRequestDto.getAmount()));
        accountRepository.save(from);

        bankAccountReceiver.setAvailableBalance((long) (bankAccountReceiver.getAvailableBalance() + exchangeRequestDto.getAmount()));
        accountRepository.save(bankAccountReceiver);


        to.setAvailableBalance((long) (to.getAvailableBalance() + amountToSubtract));
        accountRepository.save(to);

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
