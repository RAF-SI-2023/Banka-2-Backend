package rs.edu.raf.BankService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.BankService.data.enums.TransactionDirection;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarginsTransactionResponseDto {

    private Long id;
    private Long orderId;
    private Long userId;
    private LocalDateTime createdAt;
    private Long marginsAccountId;
    private String description;
    private String currencyCode;
    private TransactionDirection type;
    private Double initialMargin;
    private Double maintenanceMargin;
}
