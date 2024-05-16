package rs.edu.raf.BankService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.BankService.data.enums.TransactionStatus;
import rs.edu.raf.BankService.data.enums.TransactionType;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenericTransactionDto {

    private String id;
    private Long amount;
    private Long createdAt;
    private TransactionStatus status;
    private TransactionType type;
}
