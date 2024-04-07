package rs.edu.raf.BankService.data.entities.accounts;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.BankService.data.enums.AccountType;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("ForeignCurrencyAccount")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForeignCurrencyAccount extends Account {

    private Double interestRate = 1.0;
    private String defaultCurrencyCode;
    private Integer numberOfAllowedCurrencies = 4;
    @OneToMany(
            mappedBy = "account",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    private List<ForeignCurrencyHolder> foreignCurrencyHolders = new ArrayList<>();

    public ForeignCurrencyAccount(
            String accountNumber,
            String email,
            AccountType accountType,
            String currencyCode,
            Double maintenanceFee
    ){
        super(accountNumber, email, accountType, currencyCode, maintenanceFee);

        this.defaultCurrencyCode = currencyCode;
    }
}
