package rs.edu.raf.BankService.data.entities;

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
    private Long availableBalance;
    private Long reservedFunds;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private ForeignCurrencyAccount account;
}
