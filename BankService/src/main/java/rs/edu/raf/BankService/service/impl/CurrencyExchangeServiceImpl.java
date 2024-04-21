package rs.edu.raf.BankService.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.data.dto.*;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.data.entities.accounts.DomesticCurrencyCashAccount;
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
        CashAccount from = accountRepository.findByAccountNumber(exchangeRequestDto.getFromAccount());
        CashAccount to = accountRepository.findByAccountNumber(exchangeRequestDto.getToAccount());
        List<CashAccount> bankCashAccounts = accountRepository.findAllByEmail("bankAccount@bank.rs");

        if (from == null || to == null) {
            throw new AccountNotFoundException("Account not found");
        }
        if (!from.getEmail().equals(to.getEmail())) {
            throw new RuntimeException("Accounts are not owned by the same user");
        }

        if (from.getCurrencyCode().equals(to.getCurrencyCode())) {
            throw new RuntimeException("Cannot exchange same currency");
        }

        if (from instanceof DomesticCurrencyCashAccount && to instanceof DomesticCurrencyCashAccount) {
            throw new RuntimeException("Cannot exchange between domestic currency accounts");
        }

        ExchangeRates exchangeRate = exchangeRateRepository.findByFromCurrencyAndToCurrency(from.getCurrencyCode(), to.getCurrencyCode());
        if (exchangeRate == null) {
            throw new RuntimeException("Exchange rate not found");
        }

        CashAccount bankCashAccountReceiver = bankCashAccounts.stream().filter(account -> account.getCurrencyCode().equals(from.getCurrencyCode())).findFirst().orElse(null);
        CashAccount bankCashAccountSender = bankCashAccounts.stream().filter(account -> account.getCurrencyCode().equals(to.getCurrencyCode())).findFirst().orElse(null);

        if (bankCashAccountReceiver == null || bankCashAccountSender == null) {
            throw new AccountNotFoundException("Bank account not found");
        }

        double exchangeRateValue = exchangeRate.getExchangeRate();
        Double amountToSubtract = exchangeRequestDto.getAmount() * exchangeRateValue;
        if (from.getAvailableBalance() < exchangeRequestDto.getAmount()) {
            throw new NotEnoughFundsException();
        }


        from.setAvailableBalance((long) (from.getAvailableBalance() - exchangeRequestDto.getAmount()));
        accountRepository.save(from);

        bankCashAccountReceiver.setAvailableBalance((long) (bankCashAccountReceiver.getAvailableBalance() + exchangeRequestDto.getAmount()));
        accountRepository.save(bankCashAccountReceiver);


        to.setAvailableBalance((long) (to.getAvailableBalance() + amountToSubtract));
        accountRepository.save(to);

        bankCashAccountSender.setAvailableBalance((long) (bankCashAccountSender.getAvailableBalance() - amountToSubtract));
        accountRepository.save(bankCashAccountSender);

        ExchangeTransferTransactionDetails exchangeTransferDetails = new ExchangeTransferTransactionDetails();
        exchangeTransferDetails.setExchangeRate(exchangeRateValue);
        exchangeTransferDetails.setAmount(exchangeRequestDto.getAmount());
        exchangeTransferDetails.setFromCurrency(from.getCurrencyCode());
        exchangeTransferDetails.setToCurrency(to.getCurrencyCode());
        exchangeTransferDetails.setSenderCashAccount(from);
        exchangeTransferDetails.setReceiverCashAccount(to);
        exchangeTransferDetails.setFee(0);
        exchangeTransferDetails.setTotalAmount(amountToSubtract);
        exchangeTransferDetails.setCreatedAt(LocalDateTime.now());
        exchangeTransferDetails.setStatus(TransactionStatus.CONFIRMED);
        exchangeTransferDetails = transactionRepository.save(exchangeTransferDetails);

        return exchangeTransferDetailsMapper.exchangeTransferDetailsToExchangeTransferDetailsDto(exchangeTransferDetails);

    }

}
