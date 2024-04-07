package rs.edu.raf.BankService.data.entities.credit;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "credits")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Credit {

    private String creditName;                  // naziv kredita kao npr gotovinski kredit, stambeni kredit etc
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long creditNumber;            // broj kredita
    private Double creditAmount;              // iznos kredita
    private Long paymentPeriodMonths;      // period otplate u mesecima
    private Double nominalInterestRate;   // nominalna kamatna stopa u %
    private Double effectiveInterestRate; // efektivna kamatna stopa u %
    private Long creditCreationDate;            // datum kreiranja kredita
    private Long creditExpirationDate;          // datum isteka kredita
    private Double installmentAmount;             // iznos rate  //TODO menja se zajedno sa kursom
    private Long nextInstallmentDate;           // datum sledece rate //TODO menja se nakon svake rate +1 mesec
    private Double remainingAmount;               // preostali iznos kredita //TODO menja se nakon svake rate
    private String currencyCode;                 // valuta kredita
    private String accountNumber;                // broj racuna na koji se kredit odnosi

   /* public Credit(String creditName,
                  Long creditNumber, Double creditAmount,
                  Long paymentPeriodMonths, Double nominalInterestRate,
                  Double effectiveInterestRate, Long creditCreationDate,
                  Long creditExpirationDate,
                  Double installmentAmount,
                  Long nextInstallmentDate,
                  Double remainingAmount,
                  String currencyCode,
                  String accountNumber) {
        this.creditName = creditName;
        this.creditNumber = creditNumber;
        this.creditAmount = creditAmount;
        this.paymentPeriodMonths = paymentPeriodMonths;
        this.nominalInterestRate = nominalInterestRate;
        this.effectiveInterestRate = effectiveInterestRate;
        this.creditCreationDate = creditCreationDate;
        this.creditExpirationDate = creditExpirationDate;
        this.installmentAmount = installmentAmount;
        this.nextInstallmentDate = nextInstallmentDate;
        this.remainingAmount = remainingAmount;
        this.currencyCode = currencyCode;
        this.accountNumber = accountNumber;
    }*/
}
