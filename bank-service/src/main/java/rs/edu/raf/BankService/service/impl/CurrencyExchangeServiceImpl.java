package rs.edu.raf.BankService.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.data.dto.ExchangeRatesDto;
import rs.edu.raf.BankService.data.dto.ExchangeRequestDto;
import rs.edu.raf.BankService.data.dto.ExchangeTransferDetailsDto;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.data.entities.accounts.DomesticCurrencyCashAccount;
import rs.edu.raf.BankService.data.entities.exchangeCurrency.ExchangeRates;
import rs.edu.raf.BankService.data.entities.exchangeCurrency.ExchangeTransferTransactionDetails;
import rs.edu.raf.BankService.data.enums.TransactionStatus;
import rs.edu.raf.BankService.exception.AccountNotFoundException;
import rs.edu.raf.BankService.exception.NotEnoughFundsException;
import rs.edu.raf.BankService.mapper.ExchangeRatesMapper;
import rs.edu.raf.BankService.mapper.ExchangeTransferDetailsMapper;
import rs.edu.raf.BankService.repository.CashAccountRepository;
import rs.edu.raf.BankService.repository.CashTransactionRepository;
import rs.edu.raf.BankService.repository.ExchangeRateRepository;
import rs.edu.raf.BankService.repository.ForeignCurrencyHolderRepository;
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
    private final CashAccountRepository cashAccountRepository;
    private final CashTransactionRepository cashTransactionRepository;
    private final ForeignCurrencyHolderRepository foreignCurrencyHolderRepository;

    @Value("${bank.default.currency:RSD}")
    private String defaultCurrency;

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
        CashAccount from = cashAccountRepository.findByAccountNumber(exchangeRequestDto.getFromAccount());
        CashAccount to = cashAccountRepository.findByAccountNumber(exchangeRequestDto.getToAccount());
        List<CashAccount> bankCashAccounts = cashAccountRepository.findAllByEmail("bankAccount@bank.rs");

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
        cashAccountRepository.save(from);

        bankCashAccountReceiver.setAvailableBalance((long) (bankCashAccountReceiver.getAvailableBalance() + exchangeRequestDto.getAmount()));
        cashAccountRepository.save(bankCashAccountReceiver);


        to.setAvailableBalance((long) (to.getAvailableBalance() + amountToSubtract));
        cashAccountRepository.save(to);

        bankCashAccountSender.setAvailableBalance((long) (bankCashAccountSender.getAvailableBalance() - amountToSubtract));
        cashAccountRepository.save(bankCashAccountSender);

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
        exchangeTransferDetails = cashTransactionRepository.save(exchangeTransferDetails);

        return exchangeTransferDetailsMapper.exchangeTransferDetailsToExchangeTransferDetailsDto(exchangeTransferDetails);

    }

//    @Override
//    @Transactional(rollbackOn = Exception.class)
//    public double convertToDefaultCurrency(String fromCurrency, double amount) {
//         if(fromCurrency.equals(defaultCurrency)) {
//             return amount;
//         }
//
//        List<CashAccount> bankCashAccounts = cashAccountRepository.findAllByEmail("bankAccount@bank.rs");
//        CashAccount from = bankCashAccounts.stream().filter(account -> account.getCurrencyCode().equals(fromCurrency)).findFirst().orElse(null);
//        CashAccount to = bankCashAccounts.stream().filter(account -> account.getCurrencyCode().equals(defaultCurrency)).findFirst().orElse(null);
//
//        if (from == null || to == null) {
//            throw new AccountNotFoundException("Bank account not found");
//        }
//
//        ExchangeRates exchangeRate = exchangeRateRepository.findByFromCurrencyAndToCurrency(from.getCurrencyCode(), to.getCurrencyCode());
//        if (exchangeRate == null) {
//            throw new RuntimeException("Exchange rate not found");
//        }
//
//        if (from.getAvailableBalance() - from.getReservedFunds() < amount) {
//            throw new NotEnoughFundsException();
//        }
//
//        double exchangeRateValue = exchangeRate.getExchangeRate();
//        Double exchangeValue = amount * exchangeRateValue;
//        if(exchangeValue == null) {
//            throw new RuntimeException("Null pointer exception when tried to calculate exchangeValue");
//        }
//
//        to.setAvailableBalance((long) (to.getAvailableBalance() + exchangeValue));
//        cashAccountRepository.save(to);
//
//        from.setAvailableBalance((long) (from.getAvailableBalance() - amount));
//        cashAccountRepository.save(from);
//
//        return exchangeValue;
//    }

    @Override
    public double calculateAmountInDefaultCurrency(String fromCurrency, double amount) {
        return calculateAmountBetweenCurrencies(fromCurrency, defaultCurrency, amount);
    }

    @Override
    public double calculateAmountBetweenCurrencies(String fromCurrency, String toCurrency, double amount) {
        if(fromCurrency.equals(toCurrency)) {
            return amount;
        }

        ExchangeRates exchangeRate = exchangeRateRepository.findByFromCurrencyAndToCurrency(fromCurrency, toCurrency);
        if (exchangeRate == null) {
            throw new RuntimeException("Exchange rate not found");
        }

        double exchangeRateValue = exchangeRate.getExchangeRate();
        Double amountInNewCurrency = amount * exchangeRateValue;
        if(amountInNewCurrency == null) {
            throw new RuntimeException("Null pointer exception when tried to calculate amountInNewCurrency");
        }

        return amountInNewCurrency;
    }

    @Override
    public double convert(String fromCurrency, String toCurrency, double amount) {
        if(fromCurrency.equals(toCurrency)) {
            return amount;
        }



        return 0;
    }

}
