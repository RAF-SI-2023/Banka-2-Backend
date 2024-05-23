package rs.edu.raf.BankService.data.entities.transactions;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("ExternalTransferTransaction")
@Data
@NoArgsConstructor
public class ExternalTransferTransaction extends TransferTransaction {

    private String transactionPurpose;
    private Long amount;
    private String transactionCode;
    private String referenceNumber;
    private String verificationToken;
}
