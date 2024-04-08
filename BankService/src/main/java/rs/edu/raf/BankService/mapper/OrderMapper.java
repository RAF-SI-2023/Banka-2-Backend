package rs.edu.raf.BankService.mapper;

import rs.edu.raf.BankService.data.dto.OrderDto;
import rs.edu.raf.BankService.data.entities.Order;
import rs.edu.raf.BankService.data.enums.OrderActionType;

public class OrderMapper {


    public Order orderDtoToOrder(OrderDto orderDto){
        Order order = new Order();
        order.setOrderActionType(OrderActionType.valueOf(orderDto.getOrderType()));
        order.setStockSymbol(orderDto.getStockSymbol());
        order.setQuantity(orderDto.getQuantity());
        order.setSettlementDate(orderDto.getSettlementDate());
        order.setLimitPrice(orderDto.getLimitPrice());
        order.setStopPrice(orderDto.getStopPrice());
        order.setAllOrNone(orderDto.isAllOrNone());
        order.setMargin(orderDto.isMargin());
        return order;
    }
}
