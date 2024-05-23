package rs.edu.raf.StockService.unit;

import org.junit.jupiter.api.Test;
import rs.edu.raf.StockService.data.dto.ExchangeDto;
import rs.edu.raf.StockService.data.entities.Exchange;
import rs.edu.raf.StockService.mapper.ExchangeMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExchangeMapperTests {
    @Test
    public void testExchangeToExchangeDto() {
        Exchange exchange = new Exchange(
                1L,
                "Jakarta Futures Exchange (bursa Berjangka Jakarta)",
                "BBJ",
                "XBBJ",
                "Indonesia",
                "Indonesian Rupiah",
                7
        );

        ExchangeMapper mapper = new ExchangeMapper();
        ExchangeDto dto = mapper.exchangeToExchangeDto(exchange);


        assertEquals("Jakarta Futures Exchange (bursa Berjangka Jakarta)", dto.getExchangeName());
        assertEquals("BBJ", dto.getExchangeAcronym());
        assertEquals("XBBJ", dto.getExchangeMICode());
        assertEquals("Indonesia", dto.getPolity());
        assertEquals("Indonesian Rupiah", dto.getCurrency());
        assertEquals(7, dto.getTimeZone());
    }

    @Test
    public void testExchangeDtoToExchange() {
        ExchangeDto exchangeDto = new ExchangeDto(
                "Jakarta Futures Exchange (bursa Berjangka Jakarta)",
                "BBJ",
                "XBBJ",
                "Indonesia",
                "Indonesian Rupiah",
                7
        );

        ExchangeMapper mapper = new ExchangeMapper();
        Exchange exchange = mapper.exchangeDtoToExchange(exchangeDto);

        assertEquals("Jakarta Futures Exchange (bursa Berjangka Jakarta)", exchange.getExchangeName());
        assertEquals("BBJ", exchange.getExchangeAcronym());
        assertEquals("XBBJ", exchange.getExchangeMICode());
        assertEquals("Indonesia", exchange.getPolity());
        assertEquals("Indonesian Rupiah", exchange.getCurrency());
        assertEquals(7, exchange.getTimeZone());
    }
}
