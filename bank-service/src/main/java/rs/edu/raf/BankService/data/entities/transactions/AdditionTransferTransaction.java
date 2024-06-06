package rs.edu.raf.BankService.data.entities.transactions;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("AdditionTransferTransaction")
@Data
@NoArgsConstructor
public class AdditionTransferTransaction extends TransferTransaction{
    private Long amount;
}
