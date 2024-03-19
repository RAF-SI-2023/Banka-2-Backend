package rs.edu.raf.StockService.data.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor

public class CurrencyInflation implements Serializable {

    /**
     * Izbrisati ako se upisuje u DB
     */
    @JsonIgnore
    @Transient
    private static long generatedId;
    private double inflationRate;
    private long year;
    @Id
    @GeneratedValue
    private Long id;
    private long currencyId;

    public CurrencyInflation(double inflationRate, long year, long currency) {
        this.inflationRate = inflationRate;
        this.year = year;
        this.currencyId = currency;

        /** Ovaj deo izbrisati ako se upisuje u DB, kao i celo polje generated Id*/
        this.id = generatedId++;
    }

}
