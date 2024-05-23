package rs.edu.raf.BankService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.BankService.data.enums.UserAccountUserProfileLinkState;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {

    private Long id;
    private String accountNumber;
    private UserAccountUserProfileLinkState linkState;
    private String email;
    private boolean status;
    private String accountType;
    private double availableBalance;
    private double reservedFunds;
    private Long employeeId;
    private Long creationData;
    private Long expirationDate;
    private String currencyCode;
    private Double maintenanceFee;
    private boolean primaryAccount;


}
