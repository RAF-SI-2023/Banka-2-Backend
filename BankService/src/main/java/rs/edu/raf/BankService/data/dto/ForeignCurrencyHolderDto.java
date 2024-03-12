package rs.edu.raf.BankService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForeignCurrencyHolderDto {

    private Long id;
    private String currencyCode;
    private Long availableBalance;
    private Long reservedFunds;

}
