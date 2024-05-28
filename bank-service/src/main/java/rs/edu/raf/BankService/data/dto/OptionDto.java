package rs.edu.raf.BankService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionDto {
    private Long id;
    private String stockListing;
    private String optionType;
    private Double strikePrice;
    private Double impliedVolatility;
    private Double openInterest;
    private Long settlementDate;
    private String currency;

    @Override
    public String toString() {
        return
                  stockListing + '|' +
                 optionType + '|' +
                 strikePrice + '|'+
                 settlementDate +"|"+
                 currency;
    }
    public static OptionDto fromString(String s){
        if (s == null) {
            throw new IllegalArgumentException("Input string  for OptionDto cannot be null");
        }

        String[] parts = s.split("\\|");
        if (parts.length != 5) {
            throw new IllegalArgumentException("Invalid string format. Expected 5 parts separated by '|'");
        }

        try {
            String stockListing = parts[0];
            String optionType = parts[1];
            Double strikePrice = Double.valueOf(parts[2]);
            Long settlementDate = Long.valueOf(parts[3]);
            String currency = parts[4];

            return new OptionDto(null, stockListing, optionType, strikePrice, null, null, settlementDate, currency);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Error parsing OptionDto string to numeric values", e);
        }
    }
}
