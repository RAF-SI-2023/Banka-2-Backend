package rs.edu.raf.StockService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecuritiesPriceDto {
    private Double price;
    private Double high;
    private Double low;
}
