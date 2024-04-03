package rs.edu.raf.BankService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.BankService.data.enums.CreditRequestStatus;
import rs.edu.raf.BankService.data.enums.CreditType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditRequestDto {
    private Long id;
    private CreditRequestStatus status;
    private CreditType creditType;
    private Double creditAmount;
    private String creditPurpose;
    private Long monthlySalary;
    private Boolean permanentEmployment;
    private Long employmentPeriod;
    private Long maturity;
    private String branch;
    private String mobileNumber;
    private String accountNumber;
    private String note;
    private String currency;
    private String educationLevel;
    private String maritalStatus;
    private String housingStatus;
    private Boolean ownCar;

    private Long paymentPeriodMonths;
}
