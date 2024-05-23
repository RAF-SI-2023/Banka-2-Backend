package rs.edu.raf.BankService.data.entities.credit;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import rs.edu.raf.BankService.data.enums.CreditRequestStatus;
import rs.edu.raf.BankService.data.enums.CreditType;

@Entity
@Table(name = "credit_request")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CreditRequest {
    //TODO ova klasa ce se verovatno menjati u skladu sa specifikacijom/frontendom po dogovoru
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private CreditRequestStatus status;
    @Enumerated(EnumType.STRING)
    private CreditType creditType;
    private Double creditAmount;
    private String creditPurpose;
    private Long monthlySalary;
    private Boolean permanentEmployment;
    private Long employmentPeriod;
    private Long maturity;
    private String branch;
    private String mobileNumber;
    @Column(nullable = false)
    private String accountNumber;
    private String note;
    private String currency;
    private String educationLevel;
    private String maritalStatus;
    private String housingStatus;
    private Boolean ownCar;
    private Long paymentPeriodMonths;
}
