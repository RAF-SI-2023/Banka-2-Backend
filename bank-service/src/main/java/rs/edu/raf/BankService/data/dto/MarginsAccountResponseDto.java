package rs.edu.raf.BankService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.BankService.data.enums.ListingType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarginsAccountResponseDto {

    private Long id;
    private Long userId;
    private String email;
    private String currencyCode;
    private String accountNumber;
    private ListingType type;
    private Double balance;
    private Double loanValue;
    private Double maintenanceMargin;
    private boolean marginCall;
}
