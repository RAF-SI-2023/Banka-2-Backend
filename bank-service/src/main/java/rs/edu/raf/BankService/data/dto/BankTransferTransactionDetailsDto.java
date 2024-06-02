package rs.edu.raf.BankService.data.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.BankService.data.entities.exchangeCurrency.ExchangeTransferTransactionDetails;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankTransferTransactionDetailsDto {

    private Long id;
    private double fee;  //provizija banke
    private String boughtCurrency;
    private String soldCurrency;
    private double amount; //kolicina novca
    private double totalProfit; //ukupan profit
    private Long TransferTransactionDetailsId;



}
