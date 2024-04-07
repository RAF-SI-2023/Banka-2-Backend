package rs.edu.raf.StockService.unit;

import org.junit.jupiter.api.Test;
import rs.edu.raf.StockService.data.dto.StockDto;
import rs.edu.raf.StockService.data.entities.Stock;
import rs.edu.raf.StockService.mapper.StockMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StockMapperTests {
    @Test
    public void testStockToStockDto() {
        Stock stock = new Stock(
                "Stock1 Symbol",
                "Stock1 Description",
                "Stock1 Exchange",
                1L,
                1.0,
                1.0,
                1.0,
                1.0,
                1,
                1,
                1.0
        );
        StockMapper mapper = new StockMapper();
        StockDto dto = mapper.stockToStockDto(stock);


        assertEquals("Stock1 Symbol", dto.getSymbol());
        assertEquals("Stock1 Description", dto.getDescription());
        assertEquals("Stock1 Exchange", dto.getExchange());
        assertEquals(1.0, dto.getPrice());
        assertEquals(1, dto.getShares());
        assertEquals(1.0, dto.getYield());
    }

    @Test
    public void testStockDtoToStock() {
        StockDto stockDto = new StockDto(
                "Stock1 Symbol",
                "Stock1 Description",
                "Stock1 Exchange",
                1L,
                1.0,
                1.0,
                1.0,
                1.0,
                1,
                1,
                1.0
        );

        StockMapper mapper = new StockMapper();
        Stock stock = mapper.stockDtoToStock(stockDto);

        assertEquals("Stock1 Symbol", stock.getSymbol());
        assertEquals("Stock1 Description", stock.getDescription());
        assertEquals("Stock1 Exchange", stock.getExchange());
        assertEquals(1.0, stock.getPrice());
        assertEquals(1, stock.getShares());
        assertEquals(1.0, stock.getYield());
    }
}
