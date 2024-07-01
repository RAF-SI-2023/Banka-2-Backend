package rs.edu.raf.StockService.unit.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import rs.edu.raf.StockService.data.dto.CurrencyDto;
import rs.edu.raf.StockService.data.entities.Currency;
import rs.edu.raf.StockService.data.entities.CurrencyInflation;
import rs.edu.raf.StockService.mapper.CurrencyMapper;

public class CurrencyMapperTest {

    private CurrencyMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new CurrencyMapper();
    }

    @Test
    public void currencyToCurrencyDto_Success() {
        // Given
        CurrencyInflation inflation1 = new CurrencyInflation(1.5, 2023, 1L);
        CurrencyInflation inflation2 = new CurrencyInflation(2.0, 2024, 1L);
        List<CurrencyInflation> inflationList = Arrays.asList(inflation1, inflation2);
        Currency currency = new Currency("US Dollar", "USD", "$", "USA", inflationList);

        // When
        CurrencyDto dto = mapper.currencyToCurrencyDto(currency);

        // Then
        assertEquals("US Dollar", dto.getCurrencyName());
        assertEquals("USD", dto.getCurrencyCode());
        assertEquals("$", dto.getCurrencySymbol());
        assertEquals("USA", dto.getCurrencyPolity());
        assertEquals(2, dto.getInflationList().size());
    }

    @Test
    public void currencyDtoToCurrency_Success() {
        // Given
        CurrencyInflation inflation1 = new CurrencyInflation(1.5, 2023, 1L);
        CurrencyInflation inflation2 = new CurrencyInflation(2.0, 2024, 1L);
        List<CurrencyInflation> inflationList = Arrays.asList(inflation1, inflation2);
        CurrencyDto dto = new CurrencyDto("US Dollar", "USD", "$", "USA", inflationList);

        // When
        Currency currency = mapper.currencyDtoToCurrency(dto);

        // Then
        assertEquals("US Dollar", currency.getCurrencyName());
        assertEquals("USD", currency.getCurrencyCode());
        assertEquals("$", currency.getCurrencySymbol());
        assertEquals("USA", currency.getCurrencyPolity());
        assertEquals(2, currency.getInflationList().size());
    }
}
