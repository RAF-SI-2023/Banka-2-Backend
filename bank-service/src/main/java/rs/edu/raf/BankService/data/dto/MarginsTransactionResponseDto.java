package rs.edu.raf.BankService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.BankService.data.enums.TransactionDirection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarginsTransactionResponseDto {

    private Long id;
    private Long orderId;
    private Long userId;
    private Long createdAt;
    private Long marginsAccountId;
    private String accountNumber;
    private String description;
    private String currencyCode;
    private TransactionDirection type;
    private Double loanValue;
    private Double initialMargin;
    private Double maintenanceMargin;
}
