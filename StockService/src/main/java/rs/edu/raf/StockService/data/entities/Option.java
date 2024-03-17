package rs.edu.raf.StockService.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.StockService.data.enums.OptionType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String stockListing;
    private OptionType optionType;
    private String strikePrice;
    private String impliedVolatility;
    private String openInterest;
    private Long settlementDate;

    public Option(
            String stockListing,
            OptionType optionType,
            String strikePrice,
            String impliedVolatility,
            String openInterest,
            Long settlementDate
    ) {
        this.stockListing = stockListing;
        this.optionType = optionType;
        this.strikePrice = strikePrice;
        this.impliedVolatility = impliedVolatility;
        this.openInterest = openInterest;
        this.settlementDate = settlementDate;
    }
}
