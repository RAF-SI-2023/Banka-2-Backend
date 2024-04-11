package rs.edu.raf.BankService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.BankService.data.enums.OrderStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private OrderStatus orderStatus;

    private String orderType;

    private String stockSymbol;

    private Integer quantity;

    private Long settlementDate = -1L;

    private double limitPrice = -1;

    private double stopPrice = -1;

    private boolean allOrNone = false;

    private boolean margin = false;

}
