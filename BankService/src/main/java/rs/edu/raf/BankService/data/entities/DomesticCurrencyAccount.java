package rs.edu.raf.BankService.data.entities;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.BankService.data.enums.AccountType;
import rs.edu.raf.BankService.data.enums.DomesticCurrencyAccountType;

@Entity
@DiscriminatorValue("DomesticCurrencyAccount")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DomesticCurrencyAccount extends Account {

    private DomesticCurrencyAccountType domesticCurrencyAccountType;
    private Double interestRate; // vazi za retirement i student

    public DomesticCurrencyAccount(
            String accountNumber,
            String email,
            AccountType accountType,
            String currencyCode,
            Double maintenanceFee,
            DomesticCurrencyAccountType domesticCurrencyAccountType

    ){
        super(accountNumber, email, accountType, currencyCode, maintenanceFee);

        this.domesticCurrencyAccountType = domesticCurrencyAccountType;

        double interestRate = 0;
        if(domesticCurrencyAccountType == DomesticCurrencyAccountType.RETIREMENT) interestRate = 2.5;
        if(domesticCurrencyAccountType == DomesticCurrencyAccountType.STUDENT) interestRate = 4.0;

        this.interestRate = interestRate;
    }

}
