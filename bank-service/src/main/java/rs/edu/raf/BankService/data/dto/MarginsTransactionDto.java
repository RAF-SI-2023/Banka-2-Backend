package rs.edu.raf.BankService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.BankService.data.enums.TransactionDirection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarginsTransactionDto {

    private Long orderId;
    private Long marginsAccountId;
    private String description;
    private String currencyCode;
    private TransactionDirection type;
    private Double initialMargin;
}
