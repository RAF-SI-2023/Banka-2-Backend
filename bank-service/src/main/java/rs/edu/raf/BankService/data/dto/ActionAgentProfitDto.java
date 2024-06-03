package rs.edu.raf.BankService.data.dto;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.BankService.data.entities.transactions.ExternalTransferTransaction;
import rs.edu.raf.BankService.data.entities.transactions.TransferTransaction;
import rs.edu.raf.BankService.data.enums.TransactionProfitType;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionAgentProfitDto {

    private Long id;
    private String userEmail;
    private double profit;
    private TransactionProfitType transactionType;
    private Long transactionId;
    private Long createdAt;

}
