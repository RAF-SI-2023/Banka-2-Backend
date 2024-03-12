package rs.edu.raf.BankService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DomesticCurrencyAccountDto extends AccountDto {

    private String domesticCurrencyAccountType;
    private Double interestRate; // vazi za retirement i student

    public DomesticCurrencyAccountDto(
            Long id,
            boolean linkedWithUserProfile,
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
            String domesticCurrencyAccountType,
            Double interestRate
    ) {
        super(id, linkedWithUserProfile, email, status, name, availableBalance, reservedFunds, employeeId, creationData,
                expirationDate, currencyCode, maintenanceFee);
        this.domesticCurrencyAccountType = domesticCurrencyAccountType;
        this.interestRate = interestRate;
    }
}
