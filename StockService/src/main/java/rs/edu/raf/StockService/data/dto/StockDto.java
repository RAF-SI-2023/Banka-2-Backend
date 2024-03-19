package rs.edu.raf.StockService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockDto extends ListingDto {
    private Integer shares;
    private Double yield;

    public StockDto(String symbol, String description, String exchange, Long lastRefresh, Double price, Double high,
                    Double low, Double change, Integer volume, Integer shares, Double yield) {
        super(symbol, description, exchange, lastRefresh, price, high, low, change, volume);
        this.shares = shares;
        this.yield = yield;
    }
}
