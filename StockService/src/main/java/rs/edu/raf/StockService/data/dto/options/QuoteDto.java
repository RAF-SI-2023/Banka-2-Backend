package rs.edu.raf.StockService.data.dto.options;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuoteDto {
    private String language;
    private String region;
    private String quoteType;
    private boolean triggerable;
    private String quoteSourceName;
    private String exchange;
    private String currency;
    private String market;
    private double epsTrailingTwelveMonths;
    private double epsForward;
    private long sharesOutstanding;
    private double bookValue;
    private double fiftyDayAverage;
    private double fiftyDayAverageChange;
}
