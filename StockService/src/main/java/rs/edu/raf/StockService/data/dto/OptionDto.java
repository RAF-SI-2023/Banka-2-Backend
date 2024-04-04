package rs.edu.raf.StockService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.StockService.data.enums.OptionType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptionDto {

    private String stockListing;
    private OptionType optionType;
    private Double strikePrice;
    private Double impliedVolatility;
    private Double openInterest;
    private Long settlementDate;
}
