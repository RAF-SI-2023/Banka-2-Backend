package rs.edu.raf.BankService.data.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.BankService.data.enums.ListingType;
import rs.edu.raf.BankService.data.enums.TransactionDirection;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MarginsTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private MarginsAccount marginsAccount;

    private LocalDateTime createdAt;
    private Long orderId;
    private Long userId;
    private String description;
    private String currencyCode;
    private TransactionDirection type;
    private Double investmentAmount;
    private Double loanValue;
    private Double maintenanceMargin;
    private Double interest;

    // ovo predstavlja koliko je placen ovaj order, a to cu da upotrebim u scheduler-u za cenu akcije
    // u specifikaciji stoji da treba da se uzme trenutna cena akcije, a ne koliko je placena (ne znam kako to da uradim)
    private Double orderPrice;

    public boolean isTransactionDeposit() {
        return TransactionDirection.DEPOSIT.equals(this.type);
    }

    public void setFallbackValues(ListingType listingType) {
        if (this.loanValue < 0) {
            this.loanValue = 0.0;
        }

        if (this.interest < 0) {
            this.interest = 0.0;
        }

        if (ListingType.FUTURE.equals(listingType)) {
            this.interest = 0.0;
        }
    }
}
