package rs.edu.raf.BankService.data.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.BankService.data.enums.AccountType;
@Entity
@Table(name = "accounts")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean linkedWithUserProfile;
    private String email;
    private boolean status;
    private AccountType accountType;
    private Long availableBalance;
    private Long reservedFunds;
    private Long employeeId;
    private Long creationData;
    private Long expirationDate;
    private String currencyCode;
    private Double maintenanceFee;
}
