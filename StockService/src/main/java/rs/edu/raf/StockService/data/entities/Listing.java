package rs.edu.raf.StockService.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Listing {
    @Id
    @GeneratedValue
    private Long id;
    private String symbol;
    private String description;
    private String exchange;
    private Long lastRefresh;
    private double price;
    private double high;
    private double low;
    private double change;
    private Integer volume;

    public Listing(
            String symbol,
            String description,
            String exchange,
            Long lastRefresh,
            double price,
            double high,
            double low,
            double change,
            Integer volume
    ) {
        this.symbol = symbol;
        this.description = description;
        this.exchange = exchange;
        this.lastRefresh = lastRefresh;
        this.price = price;
        this.high = high;
        this.low = low;
        this.change = change;
        this.volume = volume;
    }
}
