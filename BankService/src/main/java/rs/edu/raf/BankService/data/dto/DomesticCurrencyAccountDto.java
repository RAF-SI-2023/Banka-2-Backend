package rs.edu.raf.BankService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.BankService.data.enums.UserAccountUserProfileLinkState;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DomesticCurrencyAccountDto extends AccountDto {

    private String domesticCurrencyAccountType;
    private Double interestRate; // vazi za retirement i student

    public DomesticCurrencyAccountDto(
            Long id,
            String accountNumber,
            UserAccountUserProfileLinkState linkedWithUserProfile,
            String email,
            boolean status,
            String name,
            double availableBalance,
            double reservedFunds,
            Long employeeId,
            Long creationData,
            Long expirationDate,
            String currencyCode,
            Double maintenanceFee,
            String domesticCurrencyAccountType,
            Double interestRate
    ) {
        super(id, accountNumber, linkedWithUserProfile, email, status, name, availableBalance, reservedFunds, employeeId, creationData,
                expirationDate, currencyCode, maintenanceFee);
        this.domesticCurrencyAccountType = domesticCurrencyAccountType;
        this.interestRate = interestRate;
    }
}
