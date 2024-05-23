package rs.edu.raf.StockService.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.StockService.data.dto.ExchangeDto;
import rs.edu.raf.StockService.data.entities.Exchange;

@Component
public class ExchangeMapper {
    public ExchangeDto exchangeToExchangeDto(Exchange exchange) {
        return new ExchangeDto(
                exchange.getExchangeName(),
                exchange.getExchangeAcronym(),
                exchange.getExchangeMICode(),
                exchange.getPolity(),
                exchange.getCurrency(),
                exchange.getTimeZone()
        );
    }

    public Exchange exchangeDtoToExchange(ExchangeDto exchangeDto) {
        return new Exchange(
                exchangeDto.getExchangeName(),
                exchangeDto.getExchangeAcronym(),
                exchangeDto.getExchangeMICode(),
                exchangeDto.getPolity(),
                exchangeDto.getCurrency(),
                exchangeDto.getTimeZone()
        );
    }
}
