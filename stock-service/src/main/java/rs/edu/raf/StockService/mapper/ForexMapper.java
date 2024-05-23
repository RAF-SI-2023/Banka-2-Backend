package rs.edu.raf.StockService.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.StockService.data.dto.ForexDto;
import rs.edu.raf.StockService.data.entities.Forex;

@Component
public class ForexMapper {
    public ForexDto forexToForexDto(Forex forex) {
        return new ForexDto(
                forex.getId(),
                forex.getSymbol(),
                forex.getDescription(),
                forex.getExchange(),
                forex.getLastRefresh(),
                forex.getPrice(),
                forex.getHigh(),
                forex.getLow(),
                forex.getChange(),
                forex.getVolume(),
                forex.getBaseCurrency(),
                forex.getQuoteCurrency()
        );
    }

    public Forex forexDtoToForex(ForexDto forexDto) {
        return new Forex(
                forexDto.getSymbol(),
                forexDto.getDescription(),
                forexDto.getExchange(),
                forexDto.getLastRefresh(),
                forexDto.getPrice(),
                forexDto.getHigh(),
                forexDto.getLow(),
                forexDto.getChange(),
                forexDto.getVolume(),
                forexDto.getBaseCurrency(),
                forexDto.getQuoteCurrency()
        );
    }
}
