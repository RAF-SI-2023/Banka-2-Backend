package rs.edu.raf.BankService.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ActiveTradingJob {
    @GeneratedValue
    @Id
    private long id;
    private long orderId;
    private String exchangeDtoAcronym;
    private String tradingAccountNumber;
    private String userRole;
    private double totalPriceCalculated;
    private boolean active = true;
}
