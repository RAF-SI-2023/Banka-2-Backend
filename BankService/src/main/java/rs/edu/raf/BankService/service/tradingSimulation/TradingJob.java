package rs.edu.raf.BankService.service.tradingSimulation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.edu.raf.BankService.data.dto.ExchangeDto;
import rs.edu.raf.BankService.data.entities.Order;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TradingJob {
    private Order order;
    private ExchangeDto exchangeDto;
    private String tradingAccountNumber;
    private String userRole;
}
