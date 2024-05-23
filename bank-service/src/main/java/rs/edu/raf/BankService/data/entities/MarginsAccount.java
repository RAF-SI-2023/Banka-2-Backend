package rs.edu.raf.BankService.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.BankService.data.enums.ListingType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MarginsAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String currencyCode;
    private String accountNumber;
    private ListingType listingType;
    private Double balance;
    private Double loanValue;
    private Double maintenanceMargin;
    private boolean marginCall;
}
