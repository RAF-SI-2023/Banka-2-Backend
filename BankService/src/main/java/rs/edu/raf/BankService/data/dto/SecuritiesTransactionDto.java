package rs.edu.raf.BankService.data.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.data.entities.transactions.TransferTransaction;
import rs.edu.raf.BankService.data.enums.TransactionStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecuritiesTransactionDto {
    private Long id;
    private CashAccount senderCashAccount;
    private CashAccount receiverCashAccount;
    private TransactionStatus status;
    private LocalDateTime createdAt;
    private String securitiesSymbol;
    private Integer quantityToTransfer;
    private Double amount;

}
