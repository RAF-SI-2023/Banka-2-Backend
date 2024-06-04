package rs.edu.raf.BankService.data.entities.profit;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import rs.edu.raf.BankService.data.entities.transactions.ExternalTransferTransaction;
import rs.edu.raf.BankService.data.enums.TransactionProfitType;

@Entity
@Table(name = "action_agent_profits")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionAgentProfit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_email")
    private String userEmail;
    private double profit;
    private TransactionProfitType transactionType;
    private Long transactionId;
    private Long createdAt;


}
