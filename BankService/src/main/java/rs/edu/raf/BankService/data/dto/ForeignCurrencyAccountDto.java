package rs.edu.raf.BankService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.BankService.data.enums.UserAccountUserProfileLinkState;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForeignCurrencyAccountDto extends AccountDto {

    private Double interestRate;
    private String defaultCurrencyCode;
    private Integer numberOfAllowedCurrencies;
    private List<ForeignCurrencyHolderDto> foreignCurrencyHolderDto = new ArrayList<>();


    public ForeignCurrencyAccountDto(
            Long id,
            String accountNumber,
            UserAccountUserProfileLinkState linkState,
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
            Double interestRate,
            String defaultCurrencyCode,
            Integer numberOfAllowedCurrencies,
            List<ForeignCurrencyHolderDto> foreignCurrencyHolderDto
    ) {
        super(id, accountNumber, linkState, email, status, name, availableBalance, reservedFunds, employeeId, creationData,
                expirationDate, currencyCode, maintenanceFee);
        this.interestRate = interestRate;
        this.defaultCurrencyCode = defaultCurrencyCode;
        this.numberOfAllowedCurrencies = numberOfAllowedCurrencies;
        this.foreignCurrencyHolderDto = foreignCurrencyHolderDto;
    }

}
