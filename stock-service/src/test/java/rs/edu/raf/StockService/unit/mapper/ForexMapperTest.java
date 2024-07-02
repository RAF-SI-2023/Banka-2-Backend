package rs.edu.raf.StockService.unit.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import rs.edu.raf.StockService.data.dto.ForexDto;
import rs.edu.raf.StockService.data.entities.Forex;
import rs.edu.raf.StockService.mapper.ForexMapper;

public class ForexMapperTest {

    private ForexMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new ForexMapper();
    }

    @Test
    public void forexToForexDto_Success() {
        // Given
        Forex forex = new Forex("EUR/USD", "Euro to US Dollar", "FX", System.currentTimeMillis(), 1.1, 1.15, 1.05, 0.01,
                1000000, "EUR", "USD");

        // When
        ForexDto dto = mapper.forexToForexDto(forex);

        // Then
        assertEquals("EUR/USD", dto.getSymbol());
        assertEquals("Euro to US Dollar", dto.getDescription());
        assertEquals("FX", dto.getExchange());
        assertEquals(forex.getLastRefresh(), dto.getLastRefresh());
        assertEquals(1.1, dto.getPrice());
        assertEquals(1.15, dto.getHigh());
        assertEquals(1.05, dto.getLow());
        assertEquals(0.01, dto.getChange());
        assertEquals(1000000, dto.getVolume());
        assertEquals("EUR", dto.getBaseCurrency());
        assertEquals("USD", dto.getQuoteCurrency());
    }

    @Test
    public void forexDtoToForex_Success() {
        // Given
        ForexDto dto = new ForexDto(1L, "EUR/USD", "Euro to US Dollar", "FX", System.currentTimeMillis(), 1.1, 1.15,
                1.05, 0.01, 1000000, "EUR", "USD");

        // When
        Forex forex = mapper.forexDtoToForex(dto);

        // Then
        assertEquals("EUR/USD", forex.getSymbol());
        assertEquals("Euro to US Dollar", forex.getDescription());
        assertEquals("FX", forex.getExchange());
        assertEquals(dto.getLastRefresh(), forex.getLastRefresh());
        assertEquals(1.1, forex.getPrice());
        assertEquals(1.15, forex.getHigh());
        assertEquals(1.05, forex.getLow());
        assertEquals(0.01, forex.getChange());
        assertEquals(1000000, forex.getVolume());
        assertEquals("EUR", forex.getBaseCurrency());
        assertEquals("USD", forex.getQuoteCurrency());
    }
}
