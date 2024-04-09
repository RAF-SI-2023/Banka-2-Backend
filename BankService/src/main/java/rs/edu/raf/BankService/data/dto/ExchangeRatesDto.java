package rs.edu.raf.BankService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRatesDto {
    private Long id;
    private long timeLastUpdated;
    private long timeNextUpdate;
    private String fromCurrency;
    private String toCurrency;
    private double exchangeRate;
}
