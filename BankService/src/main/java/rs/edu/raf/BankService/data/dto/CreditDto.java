package rs.edu.raf.BankService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditDto {

    private String creditName;                  // naziv kredita kao npr gotovinski kredit, stambeni kredit etc
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
}
