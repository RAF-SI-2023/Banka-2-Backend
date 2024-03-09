package rs.edu.raf.StockService.data.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor

public class CurrencyInflation {

    private double inflationRate;
    private long year;
    @Id
    private Long id;
    @ManyToOne
    private Currency currency;


    public CurrencyInflation(double inflationRate, long year, Currency currency) {
        this.inflationRate = inflationRate;
        this.year = year;
        this.currency = currency;
    }
}
