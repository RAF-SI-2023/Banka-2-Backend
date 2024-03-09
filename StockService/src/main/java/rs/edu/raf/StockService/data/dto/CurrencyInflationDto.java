package rs.edu.raf.StockService.data.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import rs.edu.raf.StockService.data.entities.Currency;

@Data
@AllArgsConstructor
public class CurrencyInflationDto {
    @NotNull
    private double inflationRate;
    @NotNull
    private long year;
    @NotNull
    private Currency currency;
}
