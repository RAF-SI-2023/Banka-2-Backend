package rs.edu.raf.BankService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.BankService.data.enums.TransactionStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InternalTransferTransactionDto {

    private String id;
    private String senderAccountNumber;
    private String receiverAccountNumber;
    private Long amount;
    private LocalDateTime createdAt;
    private TransactionStatus status;

}
