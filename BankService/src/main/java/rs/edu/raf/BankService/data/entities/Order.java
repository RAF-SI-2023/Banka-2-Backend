package rs.edu.raf.BankService.data.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.BankService.data.enums.OrderStatus;
import rs.edu.raf.BankService.data.enums.OrderActionType;

@Entity
@Table(name = "orders")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "order_type", discriminatorType = DiscriminatorType.STRING)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stockSymbol;

    @Enumerated(EnumType.STRING)
    private OrderActionType orderActionType;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.APPROVED;

    private Long settlementDate = -1L;

    private boolean orderCompleted = false;

    private long timeOfLastModification;

    private double limitPrice = -1;

    private double stopPrice = -1;

    private boolean allOrNone = false;

    private boolean margin = false;

    private boolean ownedByBank = false;

    private Long initiatedByUserId = -1L;

    private Long approvedBySupervisorId = -1L;

    private long userId = -1;




}
