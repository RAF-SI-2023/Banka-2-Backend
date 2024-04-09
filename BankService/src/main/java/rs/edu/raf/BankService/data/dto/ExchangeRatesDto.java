package rs.edu.raf.BankService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExchangeRatesDto {
    private Long id;
    private long timeLastUpdated;
    private long timeNextUpdate;
    private String fromCurrency;
    private String toCurrency;
    private double exchangeRate;
}
