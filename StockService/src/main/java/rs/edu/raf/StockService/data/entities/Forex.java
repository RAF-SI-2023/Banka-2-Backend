package rs.edu.raf.StockService.data.entities;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Forex extends Listing {
    private String baseCurrency;
    private String quoteCurrency;

    public Forex(String symbol, String description, String exchange, Long lastRefresh, Double price, Double high,
                 Double low, Double change, Integer volume, String baseCurrency, String quoteCurrency) {
        super(symbol, description, exchange, lastRefresh, price, high, low, change, volume);
        this.baseCurrency = baseCurrency;
        this.quoteCurrency = quoteCurrency;
    }


    public Integer getContractSize() {
        return 1000;
    }

    public Double getMaintenanceMargin() {
        return getContractSize() * getPrice() * 0.1;
    }
}
