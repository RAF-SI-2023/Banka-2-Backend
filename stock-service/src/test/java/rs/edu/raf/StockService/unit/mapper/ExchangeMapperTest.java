package rs.edu.raf.StockService.unit.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import rs.edu.raf.StockService.data.dto.ExchangeDto;
import rs.edu.raf.StockService.data.entities.Exchange;
import rs.edu.raf.StockService.mapper.ExchangeMapper;

public class ExchangeMapperTest {

    private ExchangeMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new ExchangeMapper();
    }

    @Test
    public void exchangeToExchangeDto_Success() {
        // Given
        Exchange exchange = new Exchange("New York Stock Exchange", "NYSE", "XNYS", "USA", "USD", -5);

        // When
        ExchangeDto dto = mapper.exchangeToExchangeDto(exchange);

        // Then
        assertEquals("New York Stock Exchange", dto.getExchangeName());
        assertEquals("NYSE", dto.getExchangeAcronym());
        assertEquals("XNYS", dto.getExchangeMICode());
        assertEquals("USA", dto.getPolity());
        assertEquals("USD", dto.getCurrency());
        assertEquals(-5, dto.getTimeZone());
    }

    @Test
    public void exchangeDtoToExchange_Success() {
        // Given
        ExchangeDto dto = new ExchangeDto("New York Stock Exchange", "NYSE", "XNYS", "USA", "USD", -5);

        // When
        Exchange exchange = mapper.exchangeDtoToExchange(dto);

        // Then
        assertEquals("New York Stock Exchange", exchange.getExchangeName());
        assertEquals("NYSE", exchange.getExchangeAcronym());
        assertEquals("XNYS", exchange.getExchangeMICode());
        assertEquals("USA", exchange.getPolity());
        assertEquals("USD", exchange.getCurrency());
        assertEquals(-5, exchange.getTimeZone());
    }
}
