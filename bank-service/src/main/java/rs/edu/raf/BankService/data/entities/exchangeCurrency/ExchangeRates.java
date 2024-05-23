package rs.edu.raf.BankService.data.entities.exchangeCurrency;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "exchange_rates")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long timeLastUpdated;
    private long timeNextUpdate;
    private String fromCurrency;
    private String toCurrency;
    private double exchangeRate;

}
