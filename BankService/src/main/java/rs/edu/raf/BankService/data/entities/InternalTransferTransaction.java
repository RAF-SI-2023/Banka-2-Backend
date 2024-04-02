package rs.edu.raf.BankService.data.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.BankService.data.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("InternalTransferTransaction")
@Data
@NoArgsConstructor
public class InternalTransferTransaction extends Transaction {

    private Long amount;
}
