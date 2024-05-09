package rs.edu.raf.StockService.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BankStockDto {

    private Long listingId;
    private String listingType;
}
