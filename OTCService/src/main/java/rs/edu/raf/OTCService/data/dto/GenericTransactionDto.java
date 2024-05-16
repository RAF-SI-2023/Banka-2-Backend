package rs.edu.raf.OTCService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.OTCService.data.enums.TransactionStatus;
import rs.edu.raf.OTCService.data.enums.TransactionType;

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
