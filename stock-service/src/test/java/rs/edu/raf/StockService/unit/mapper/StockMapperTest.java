package rs.edu.raf.StockService.unit.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import rs.edu.raf.StockService.data.dto.StockDto;
import rs.edu.raf.StockService.data.entities.Stock;
import rs.edu.raf.StockService.mapper.StockMapper;

public class StockMapperTest {

    private StockMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new StockMapper();
    }

    @Test
    public void stockToStockDto_Success() {
        // Given
        Stock stock = new Stock(
                "AAPL",
                "Apple Inc.",
                "NASDAQ",
                1682552399000L,
                150.0,
                155.0,
                145.0,
                5.0,
                1000000,
                50000,
                1.2);
        stock.setId(1L);

        // When
        StockDto dto = mapper.stockToStockDto(stock);

        // Then
        assertEquals(1L, dto.getId());
        assertEquals("AAPL", dto.getSymbol());
        assertEquals("Apple Inc.", dto.getDescription());
        assertEquals("NASDAQ", dto.getExchange());
        assertEquals(1682552399000L, dto.getLastRefresh());
        assertEquals(150.0, dto.getPrice());
        assertEquals(155.0, dto.getHigh());
        assertEquals(145.0, dto.getLow());
        assertEquals(5.0, dto.getChange());
        assertEquals(1000000, dto.getVolume());
        assertEquals(50000, dto.getShares());
        assertEquals(1.2, dto.getYield());
    }

    @Test
    public void stockDtoToStock_Success() {
        // Given
        StockDto dto = new StockDto(
                1L,
                "AAPL",
                "Apple Inc.",
                "NASDAQ",
                1682552399000L,
                150.0,
                155.0,
                145.0,
                5.0,
                1000000,
                50000,
                1.2);

        // When
        Stock stock = mapper.stockDtoToStock(dto);

        // Then
        assertEquals("AAPL", stock.getSymbol());
        assertEquals("Apple Inc.", stock.getDescription());
        assertEquals("NASDAQ", stock.getExchange());
        assertEquals(1682552399000L, stock.getLastRefresh());
        assertEquals(150.0, stock.getPrice());
        assertEquals(155.0, stock.getHigh());
        assertEquals(145.0, stock.getLow());
        assertEquals(5.0, stock.getChange());
        assertEquals(1000000, stock.getVolume());
        assertEquals(50000, stock.getShares());
        assertEquals(1.2, stock.getYield());
    }
}
