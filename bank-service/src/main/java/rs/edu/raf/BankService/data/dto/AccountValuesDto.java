package rs.edu.raf.BankService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountValuesDto {

    private String currency;
    private double total;
    private double availableBalance;
    private double reservedFunds;

}
