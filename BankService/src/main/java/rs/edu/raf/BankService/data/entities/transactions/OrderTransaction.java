package rs.edu.raf.BankService.data.entities.transactions;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("order_transactions")
@Data
@NoArgsConstructor
public class OrderTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;

    private Long date;

    private Long orderId;

    private String description;

    private String currency;

    private Double payAmount;

    private Double payoffAmount;

    private Double reservedFunds;

    private Double usedOfReservedFunds;

}
