package rs.edu.raf.BankService.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.BankService.data.dto.ExchangeRatesDto;
import rs.edu.raf.BankService.data.entities.exchangeCurrency.ExchangeRates;

@Component
public class ExchangeRatesMapper {
    public ExchangeRatesDto exchangeRatesToExchangeRatesDto(ExchangeRates exchangeRates) {
        return new ExchangeRatesDto(
                exchangeRates.getId(),
                exchangeRates.getTimeLastUpdated(),
                exchangeRates.getTimeNextUpdate(),
                exchangeRates.getFromCurrency(),
                exchangeRates.getToCurrency(),
                exchangeRates.getExchangeRate());
    }

    public ExchangeRates exchangeRatesDtoToExchangeRates(ExchangeRatesDto dto) {
        return new ExchangeRates(
                dto.getId(),
                dto.getTimeLastUpdated(),
                dto.getTimeNextUpdate(),
                dto.getFromCurrency(),
                dto.getToCurrency(),
                dto.getExchangeRate());
    }
}
