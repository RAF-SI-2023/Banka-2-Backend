package rs.edu.raf.BankService.mapper;

import rs.edu.raf.BankService.data.dto.OrderDto;
import rs.edu.raf.BankService.data.entities.Order;
import rs.edu.raf.BankService.data.enums.OrderActionType;

public class OrderMapper {


    public Order orderDtoToOrder(OrderDto orderDto){
        Order order = new Order();
        order.setOrderActionType(orderDto.getOrderActionType());
        order.setSecuritiesType(orderDto.getSecuritiesType());
        order.setStockSymbol(orderDto.getStockSymbol());
        order.setQuantity(orderDto.getQuantity());
        order.setSettlementDate(orderDto.getSettlementDate());
        order.setLimitPrice(orderDto.getLimitPrice());
        order.setStopPrice(orderDto.getStopPrice());
        order.setAllOrNone(orderDto.isAllOrNone());
        order.setMargin(orderDto.isMargin());
        return order;
    }

    public OrderDto orderToOrderDto(Order order){
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderStatus(order.getOrderStatus());
        orderDto.setOrderActionType(order.getOrderActionType());
        orderDto.setSecuritiesType(order.getSecuritiesType());
        orderDto.setStockSymbol(order.getStockSymbol());
        orderDto.setQuantity(order.getQuantity());
        orderDto.setSettlementDate(order.getSettlementDate());
        orderDto.setLimitPrice(order.getLimitPrice());
        orderDto.setStopPrice(order.getStopPrice());
        orderDto.setAllOrNone(order.isAllOrNone());
        orderDto.setMargin(order.isMargin());
        return orderDto;
    }
}
