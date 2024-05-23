package rs.edu.raf.StockService.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.StockService.data.dto.CurrencyDto;
import rs.edu.raf.StockService.data.entities.Currency;

@Component
public class CurrencyMapper {
    public CurrencyDto currencyToCurrencyDto(Currency currency) {
        return new CurrencyDto(
                currency.getCurrencyName(),
                currency.getCurrencyCode(),
                currency.getCurrencySymbol(),
                currency.getCurrencyPolity(),
                currency.getInflationList()
        );
    }

    public Currency currencyDtoToCurrency(CurrencyDto dto) {
        return new Currency(
                dto.getCurrencyName(),
                dto.getCurrencyCode(),
                dto.getCurrencySymbol(),
                dto.getCurrencyPolity(),
                dto.getInflationList()
        );
    }

}
