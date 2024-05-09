package rs.edu.raf.StockService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForexDto extends ListingDto {
    private String baseCurrency;
    private String quoteCurrency;

    public ForexDto(Long id, String symbol, String description, String exchange, Long lastRefresh, Double price, Double high,
                    Double low, Double change, Integer volume, String baseCurrency, String quoteCurrency) {
        super(id, symbol, description, exchange, lastRefresh, price, high, low, change, volume);
        this.baseCurrency = baseCurrency;
        this.quoteCurrency = quoteCurrency;
    }
}
