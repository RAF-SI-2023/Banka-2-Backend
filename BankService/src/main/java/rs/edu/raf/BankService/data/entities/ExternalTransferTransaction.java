package rs.edu.raf.BankService.data.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("ExternalTransferTransaction")
@Data
@NoArgsConstructor
public class ExternalTransferTransaction extends Transaction {

    private String transactionPurpose;
    private Long amount;
    private String transactionCode;
    private String referenceNumber;
    private String verificationToken;
}
