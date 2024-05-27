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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exchangeTransferTransactionDetails")
    private ExchangeTransferTransactionDetails exchangeTransferTransactionDetails;
    private double fee;  //provizija banke
    private String boughtCurrency;
    private String soldCurrency;
    private double amount; //kolicina novca
    private double totalProfit; //ukupan profit



}
