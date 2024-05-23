package rs.edu.raf.StockService.mapper;


import org.springframework.stereotype.Component;
import rs.edu.raf.StockService.data.dto.CurrencyInflationDto;
import rs.edu.raf.StockService.data.entities.CurrencyInflation;

@Component
public class CurrencyInflationMapper {

    public CurrencyInflationDto currencyInflationToCurrencyInflationDto(CurrencyInflation currencyInflation) {
        return new CurrencyInflationDto(
                currencyInflation.getInflationRate(),
                currencyInflation.getYear(),
                currencyInflation.getCurrencyId()
        );
    }

    public CurrencyInflation currencyInflationDtoToCurrencyInflation(CurrencyInflationDto dto) {
        return new CurrencyInflation(
                dto.getInflationRate(),
                dto.getYear(),
                dto.getCurrencyId()
        );
    }
}
