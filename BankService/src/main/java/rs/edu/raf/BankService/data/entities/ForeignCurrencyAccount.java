package rs.edu.raf.BankService.data.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

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
    private Set<ForeignCurrencyHolder> foreignCurrencyHolders;
}
