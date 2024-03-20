package rs.edu.raf.BankService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.BankService.data.enums.UserAccountUserProfileLinkState;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessAccountDto extends AccountDto {

    private String PIB;
    private String identificationNumber;

    public BusinessAccountDto(
            Long id,
            String accountNumber,
            UserAccountUserProfileLinkState linkedWithUserProfile,
            String email,
            boolean status,
            String name,
            Long availableBalance,
            Long reservedFunds,
            Long employeeId,
            Long creationData,
            Long expirationDate,
            String currencyCode,
            Double maintenanceFee,
            String PIB,
            String identificationNumber
    ) {
        super(id, accountNumber, linkedWithUserProfile, email, status, name, availableBalance, reservedFunds, employeeId, creationData,
                expirationDate, currencyCode, maintenanceFee);
        this.PIB = PIB;
        this.identificationNumber = identificationNumber;
    }
}
