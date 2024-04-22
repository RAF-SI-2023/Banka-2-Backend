package rs.edu.raf.BankService.data.entities.accounts;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "foreign_currency_holders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForeignCurrencyHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String currencyCode;
    private Long availableBalance = 0l;
    private Long reservedFunds = 0l;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private ForeignCurrencyCashAccount account;
}
