package rs.edu.raf.BankService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.BankService.data.enums.ListingType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarginsAccountRequestDto {

    private Long userId;
    // Nadam se da postoji email na frontu, mnogo je lakse nego da ga jurim kad mi zatreba po drugim servisima na osnovu userId
    private String email;
    private String currencyCode;
    private Double balance;
    private String accountNumber;
    private Double loanValue;
    private Double maintenanceMargin;
    private boolean marginCall;
}
