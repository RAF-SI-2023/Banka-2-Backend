package rs.edu.raf.BankService.data.entities.exchangeCurrency;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("BankTransferTransactionDetail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankTransferTransactionDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double fee;  //provizija banke
    private String boughtCurrency;
    private String soldCurrency;
    private double amount; //kolicina novca
    private double totalProfit; //ukupan profit
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exchange_transfer_transaction_details_id", referencedColumnName = "id")
    private ExchangeTransferTransactionDetails exchangeTransferTransactionDetails;

    public BankTransferTransactionDetails(Long id, double fee, String boughtCurrency, String soldCurrency, double amount, double totalProfit) {
        this.id = id;
        this.fee = fee;
        this.boughtCurrency = boughtCurrency;
        this.soldCurrency = soldCurrency;
        this.amount = amount;
        this.totalProfit = totalProfit;
    }
}
