package rs.edu.raf.BankService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {

    private Long id;
    private boolean linkedWithUserProfile;
    private String email;
    private boolean status;
    private String accountType;
    private Long availableBalance;
    private Long reservedFunds;
    private Long employeeId;
    private Long creationData;
    private Long expirationDate;
    private String currencyCode;
    private Double maintenanceFee;

}
