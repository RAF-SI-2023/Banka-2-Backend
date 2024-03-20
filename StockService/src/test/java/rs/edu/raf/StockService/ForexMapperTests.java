package rs.edu.raf.StockService;

import org.junit.jupiter.api.Test;
import rs.edu.raf.StockService.data.dto.ForexDto;
import rs.edu.raf.StockService.data.entities.Forex;
import rs.edu.raf.StockService.mapper.ForexMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ForexMapperTests {
    @Test
    public void testForexToForexDto() {
        Forex forex = new Forex(
                "Forex1 Symbol",
                "Forex1 Description",
                "Forex1 Exchange",
                1L,
                1.0,
                1.0,
                1.0,
                1.0,
                1,
                "Forex1 baseCurrency",
                "Forex1 quoteCurrency"
        );

        ForexMapper mapper = new ForexMapper();
        ForexDto dto = mapper.forexToForexDto(forex);


        assertEquals("Forex1 Symbol", dto.getSymbol());
        assertEquals("Forex1 Description", dto.getDescription());
        assertEquals("Forex1 Exchange", dto.getExchange());
        assertEquals(1.0, dto.getPrice());
        assertEquals("Forex1 baseCurrency", dto.getBaseCurrency());
        assertEquals("Forex1 quoteCurrency", dto.getQuoteCurrency());
    }

    @Test
    public void testForexDtoToForex() {
        ForexDto forexDto = new ForexDto(
                "Forex1 Symbol",
                "Forex1 Description",
                "Forex1 Exchange",
                1L,
                1.0,
                1.0,
                1.0,
                1.0,
                1,
                "Forex1 baseCurrency",
                "Forex1 quoteCurrency"
        );

        ForexMapper mapper = new ForexMapper();
        Forex forex = mapper.forexDtoToForex(forexDto);

        assertEquals("Forex1 Symbol", forex.getSymbol());
        assertEquals("Forex1 Description", forex.getDescription());
        assertEquals("Forex1 Exchange", forex.getExchange());
        assertEquals(1.0, forex.getPrice());
        assertEquals("Forex1 baseCurrency", forex.getBaseCurrency());
        assertEquals("Forex1 quoteCurrency", forex.getQuoteCurrency());
    }
}
