package rs.edu.raf.StockService.unit;

import org.junit.jupiter.api.Test;
import rs.edu.raf.StockService.data.dto.CurrencyDto;
import rs.edu.raf.StockService.data.dto.CurrencyInflationDto;
import rs.edu.raf.StockService.data.entities.Currency;
import rs.edu.raf.StockService.data.entities.CurrencyInflation;
import rs.edu.raf.StockService.mapper.CurrencyInflationMapper;
import rs.edu.raf.StockService.mapper.CurrencyMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CurrencyMapperTests {

    @Test
    public void testCurrencyToCurrencyDto() {
        // Create a Currency object
        Currency currency = new Currency("US Dollar", "USD", "$", "United States", null);

        // Create a CurrencyMapper instance
        CurrencyMapper mapper = new CurrencyMapper();

        // Call the method under test
        CurrencyDto dto = mapper.currencyToCurrencyDto(currency);

        // Assert
        assertEquals("US Dollar", dto.getCurrencyName());
        assertEquals("USD", dto.getCurrencyCode());
        assertEquals("$", dto.getCurrencySymbol());
        assertEquals("United States", dto.getCurrencyPolity());
        assertNull(dto.getInflationList()); // Assuming inflation list is null
    }

    @Test
    public void testCurrencyDtoToCurrency() {
        // Create a CurrencyDto object
        CurrencyDto dto = new CurrencyDto("US Dollar", "USD", "$", "United States", null);

        // Create a CurrencyMapper instance
        CurrencyMapper mapper = new CurrencyMapper();

        // Call the method under test
        Currency currency = mapper.currencyDtoToCurrency(dto);

        // Assert
        assertEquals("US Dollar", currency.getCurrencyName());
        assertEquals("USD", currency.getCurrencyCode());
        assertEquals("$", currency.getCurrencySymbol());
        assertEquals("United States", currency.getCurrencyPolity());
        assertNull(currency.getInflationList()); // Assuming inflation list is null
    }

    @Test
    public void testCurrencyInflationToCurrencyInflationDto() {
        // Create a CurrencyInflation object
        CurrencyInflation currencyInflation = new CurrencyInflation(5.2, 2022L, 1L);

        // Create a CurrencyInflationMapper instance
        CurrencyInflationMapper mapper = new CurrencyInflationMapper();

        // Call the method under test
        CurrencyInflationDto dto = mapper.currencyInflationToCurrencyInflationDto(currencyInflation);

        // Assert
        assertEquals(5.2, dto.getInflationRate());
        assertEquals(2022L, dto.getYear());
        assertEquals(1L, dto.getCurrencyId());
    }

    @Test
    public void testCurrencyInflationDtoToCurrencyInflation() {
        // Create a CurrencyInflationDto object
        CurrencyInflationDto dto = new CurrencyInflationDto(5.2, 2022L, 1L);

        // Create a CurrencyInflationMapper instance
        CurrencyInflationMapper mapper = new CurrencyInflationMapper();

        // Call the method under test
        CurrencyInflation currencyInflation = mapper.currencyInflationDtoToCurrencyInflation(dto);

        // Assert
        assertEquals(5.2, currencyInflation.getInflationRate());
        assertEquals(2022L, currencyInflation.getYear());
        assertEquals(1L, currencyInflation.getCurrencyId());
    }
}

