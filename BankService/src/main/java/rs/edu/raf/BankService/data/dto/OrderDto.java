package rs.edu.raf.BankService.data.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.BankService.data.enums.OrderActionType;
import rs.edu.raf.BankService.data.enums.OrderStatus;
import rs.edu.raf.BankService.data.enums.SecuritiesType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private OrderStatus orderStatus;

    private OrderActionType orderActionType;

    private SecuritiesType securitiesType;

    private String stockSymbol;

    private Integer quantity;

    private Long settlementDate = -1L;

    private double limitPrice = -1;

    private double stopPrice = -1;

    private boolean allOrNone = false;

    private boolean margin = false;

}
