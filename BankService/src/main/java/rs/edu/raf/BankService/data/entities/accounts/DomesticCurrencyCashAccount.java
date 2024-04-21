package rs.edu.raf.BankService.data.entities.accounts;


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
public class DomesticCurrencyCashAccount extends CashAccount {

    private Long id;
    private DomesticCurrencyAccountType domesticCurrencyAccountType;
    private Double interestRate = 0.0; // vazi za retirement i student

    public DomesticCurrencyCashAccount(
            String accountNumber,
            String email,
            AccountType accountType,
            String currencyCode,
            Double maintenanceFee,
            DomesticCurrencyAccountType domesticCurrencyAccountType
    ){
        super(accountNumber, email, accountType, currencyCode, maintenanceFee);
        this.domesticCurrencyAccountType = domesticCurrencyAccountType;
        if(domesticCurrencyAccountType == DomesticCurrencyAccountType.RETIREMENT) this.interestRate = 2.5;
        if(domesticCurrencyAccountType == DomesticCurrencyAccountType.STUDENT) this.interestRate = 4.0;
    }

}
