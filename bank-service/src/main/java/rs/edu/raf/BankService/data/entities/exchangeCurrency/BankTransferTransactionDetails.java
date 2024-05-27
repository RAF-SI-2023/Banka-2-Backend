package rs.edu.raf.BankService.data.entities.exchangeCurrency;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;

@Entity
@DiscriminatorValue("BankTransferTransactionDetail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankTransferTransactionDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exchange_transfer_transaction_details_id", referencedColumnName = "id")
    private ExchangeTransferTransactionDetails exchangeTransferTransactionDetails;
    private double fee;  //provizija banke
    private String boughtCurrency;
    private String soldCurrency;
    private double amount; //kolicina novca
    private double totalProfit; //ukupan profit


}
