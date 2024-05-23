package rs.edu.raf.StockService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.StockService.data.entities.CurrencyInflation;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDto {
    private String currencyName;
    private String currencyCode;
    private String currencySymbol;
    private String currencyPolity;
    private List<CurrencyInflation> inflationList = new ArrayList<>();
}
