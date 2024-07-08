package rs.edu.raf.BankService.data.entities.accounts;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.BankService.data.enums.AccountType;

@Entity
@DiscriminatorValue("BusinessAccount")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessCashAccount extends CashAccount {

    private String PIB;
    private String identificationNumber; // matični broj

    public BusinessCashAccount(
            String accountNumber,
            String email,
            AccountType accountType,
            String currencyCode,
            Double maintenanceFee,
            String PIB,
            String identificationNumber
    ) {
        super(accountNumber, email, accountType, currencyCode, maintenanceFee);
        this.PIB = PIB;
        this.identificationNumber = identificationNumber;
    }

}
