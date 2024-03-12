package rs.edu.raf.BankService.data.entities;

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

    private Double interestRate;
    private String defaultCurrencyCode;
    private Integer numberOfAllowedCurrencies;
    @OneToMany(
            mappedBy = "account",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    private List<ForeignCurrencyHolder> foreignCurrencyHolders = new ArrayList<>();

    public ForeignCurrencyAccount(
            String email,
            AccountType accountType,
            String currencyCode,
            Double maintenanceFee
    ){
        super(email, accountType, currencyCode, maintenanceFee);
        this.interestRate = 1.0;
        this.defaultCurrencyCode = currencyCode;
        this.numberOfAllowedCurrencies = 4;
    }
}
