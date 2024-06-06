package rs.edu.raf.BankService.data.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.BankService.data.enums.ListingType;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MarginsAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "marginsAccount")
    private List<MarginsTransaction> marginsTransactions;

    private Long userId;

    // Nadam se da postoji email na frontu, mnogo je lakse nego da ga jurim po drugim servisima na osnovu userId
    private String email;
    private String currencyCode;
    private String accountNumber;
    private ListingType listingType;
    private Double balance;
    private Double loanValue;
    private Double maintenanceMargin;
    private boolean marginCall;

    public void setFallbackValues() {
        if (this.balance < 0) {
            this.balance = 0.0;
        }

        if (this.maintenanceMargin < 0) {
            this.maintenanceMargin = 0.0;
        }
    }

    public void addTransaction(MarginsTransaction transaction) {
        this.marginsTransactions.add(transaction);
    }
}
