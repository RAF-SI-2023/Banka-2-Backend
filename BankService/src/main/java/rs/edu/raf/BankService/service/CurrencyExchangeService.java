package rs.edu.raf.BankService.service;

import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.data.dto.ExchangeRatesDto;
import rs.edu.raf.BankService.data.dto.ExchangeRequestDto;
import rs.edu.raf.BankService.data.dto.ExchangeTransferDetailsDto;

import java.util.List;

@Service
public interface CurrencyExchangeService {
    List<ExchangeRatesDto> getAllExchangeRates();

    List<ExchangeRatesDto> getExchangeRatesForCurrency(String currencyCode);

    ExchangeTransferDetailsDto exchangeCurrency(ExchangeRequestDto exchangeRequestDto);

    double calculateAmountInDefaultCurrency(String fromCurrency, double amount);

    double calculateAmountBetweenCurrencies(String fromCurrency, String toCurrency, double amount);

    double convert(String fromCurrency, String toCurrency, double amount);
}
