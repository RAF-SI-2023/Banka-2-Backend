package rs.edu.raf.StockService.data.entities;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Stock extends Listing {
    private Integer shares;
    private Double yield;

    public Stock(String symbol, String description, String exchange, Long lastRefresh, Double price, Double high,
                 Double low, Double change, Integer volume, Integer shares, Double yield) {
        super(symbol, description, exchange, lastRefresh, price, high, low, change, volume);
        this.shares = shares;
        this.yield = yield;
    }

    public Double getMarketCap() {
        return shares * getPrice();
    }

    public Integer getContractSize() {
        return 1;
    }

    public Double getMaintenanceMargin() {
        return getPrice() * 0.5;
    }
}
