package rs.edu.raf.StockService.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.StockService.data.dto.StockDto;
import rs.edu.raf.StockService.data.entities.Stock;

@Component
public class StockMapper {
    public StockDto stockToStockDto(Stock stock) {
        return new StockDto(
                stock.getSymbol(),
                stock.getDescription(),
                stock.getExchange(),
                stock.getLastRefresh(),
                stock.getPrice(),
                stock.getHigh(),
                stock.getLow(),
                stock.getChange(),
                stock.getVolume(),
                stock.getShares(),
                stock.getYield()
        );
    }

    public Stock stockDtoToStock(StockDto stockDto) {
        return new Stock(
                stockDto.getSymbol(),
                stockDto.getDescription(),
                stockDto.getExchange(),
                stockDto.getLastRefresh(),
                stockDto.getPrice(),
                stockDto.getHigh(),
                stockDto.getLow(),
                stockDto.getChange(),
                stockDto.getVolume(),
                stockDto.getShares(),
                stockDto.getYield()
        );
    }
}
