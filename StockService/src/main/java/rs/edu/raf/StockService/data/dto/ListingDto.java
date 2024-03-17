package rs.edu.raf.StockService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListingDto {
    private String symbol;
    private String description;
    private String exchange;
    private Long lastRefresh;
    private double price;
    private double high;
    private double low;
    private double change;
    private Integer volume;
}
