package rs.edu.raf.BankService.data.entities.exchangeCurrency;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.BankService.data.entities.transactions.TransferTransaction;

@Entity
@DiscriminatorValue("ExchangeTransactionDetails")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeTransferTransactionDetails extends TransferTransaction {
    private String fromCurrency;
    private String toCurrency;
    private double amount;
    private double exchangeRate;
    private double fee;
    private double totalAmount;


}
