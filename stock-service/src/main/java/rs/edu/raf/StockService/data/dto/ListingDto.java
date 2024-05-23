package rs.edu.raf.StockService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListingDto {
    private Long id;
    private String symbol;
    private String description;
    private String exchange;
    private Long lastRefresh;
    private Double price;
    private Double high;
    private Double low;
    private Double change;
    private Integer volume;
}
