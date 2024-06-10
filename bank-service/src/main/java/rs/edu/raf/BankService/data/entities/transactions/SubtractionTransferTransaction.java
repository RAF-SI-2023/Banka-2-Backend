package rs.edu.raf.BankService.data.entities.transactions;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("SubtractionTransferTransaction")
@Data
@NoArgsConstructor
public class SubtractionTransferTransaction extends TransferTransaction{
    private Long amount;
}
