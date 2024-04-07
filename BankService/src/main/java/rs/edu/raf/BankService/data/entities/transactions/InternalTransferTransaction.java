package rs.edu.raf.BankService.data.entities.transactions;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("InternalTransferTransaction")
@Data
@NoArgsConstructor
public class InternalTransferTransaction extends TransferTransaction {

    private Long amount;

}
