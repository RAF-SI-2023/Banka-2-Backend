package rs.edu.raf.BankService.data.entities.profit;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bank_profit")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankProfit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double profit;

}
