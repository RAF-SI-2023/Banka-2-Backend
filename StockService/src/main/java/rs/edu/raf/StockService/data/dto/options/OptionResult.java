package rs.edu.raf.StockService.data.dto.options;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.StockService.data.entities.Option;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptionResult {
    private String underlyingSymbol;
    private List<Long> expirationDates;
    private List<Double> strikes;
    private boolean hasMiniOptions;
    private QuoteDto quoteDto;
    private List<Option> options;
}
