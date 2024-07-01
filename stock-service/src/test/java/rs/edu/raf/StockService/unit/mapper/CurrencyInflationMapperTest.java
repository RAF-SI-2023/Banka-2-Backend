package rs.edu.raf.StockService.unit.mapper;

import static org.junit.Assert.assertEquals;


import org.junit.jupiter.api.*;

import rs.edu.raf.StockService.data.dto.CurrencyInflationDto;
import rs.edu.raf.StockService.data.entities.CurrencyInflation;
import rs.edu.raf.StockService.mapper.CurrencyInflationMapper;

public class CurrencyInflationMapperTest {

    private CurrencyInflationMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new CurrencyInflationMapper();
    }

    @Test
    public void currencyInflationToCurrencyInflationDto_Success() {
        // Given
        CurrencyInflation inflation = new CurrencyInflation();
        inflation.setInflationRate(1.2);
        inflation.setYear(2024);
        inflation.setCurrencyId(2L);

        // When
        CurrencyInflationDto dto = mapper.currencyInflationToCurrencyInflationDto(inflation);

        // Then
        assertEquals(1.2, dto.getInflationRate(), 0);
        assertEquals(2024, dto.getYear());
        assertEquals(2L, dto.getCurrencyId());
    }

    @Test
    public void currencyInflationDtoToCurrencyInflation_Success() {
        // Given
        CurrencyInflationDto dto = new CurrencyInflationDto(1.2, 2024, 2L);

        // When
        CurrencyInflation inflation = mapper.currencyInflationDtoToCurrencyInflation(dto);

        // Then
        assertEquals(1.2, inflation.getInflationRate(), 0);
        assertEquals(2024, inflation.getYear());
        assertEquals(2L, inflation.getCurrencyId());

    }
}
