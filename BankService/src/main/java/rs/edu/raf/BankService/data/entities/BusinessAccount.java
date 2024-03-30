package rs.edu.raf.BankService.data.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.BankService.data.enums.AccountType;
import rs.edu.raf.BankService.data.enums.DomesticCurrencyAccountType;

@Entity
@DiscriminatorValue("BusinessAccount")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessAccount extends Account {

    private String PIB;
    private String identificationNumber; // matični broj

    public BusinessAccount(
            String accountNumber,
            String email,
            AccountType accountType,
            String currencyCode,
            Double maintenanceFee,
            String PIB,
            String identificationNumber
    ){
        super(accountNumber, email, accountType, currencyCode, maintenanceFee);
        this.PIB = PIB;
        this.identificationNumber = identificationNumber;
    }

}
