package rs.edu.raf.BankService.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.BankService.data.dto.OrderDto;
import rs.edu.raf.BankService.data.entities.Order;

@Component
public class OrderMapper {


    public Order orderDtoToOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setId(orderDto.getId());
        order.setOrderActionType(orderDto.getOrderActionType());
        order.setListingType(orderDto.getListingType());
        order.setListingSymbol(orderDto.getSecuritiesSymbol());
        order.setQuantity(orderDto.getQuantity());
        order.setSettlementDate(orderDto.getSettlementDate());
        order.setLimitPrice(orderDto.getLimitPrice());
        order.setStopPrice(orderDto.getStopPrice());
        order.setAllOrNone(orderDto.isAllOrNone());
        order.setMargin(orderDto.isMargin());
        return order;
    }

    public OrderDto orderToOrderDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setOrderStatus(order.getOrderStatus());
        orderDto.setOrderActionType(order.getOrderActionType());
        orderDto.setListingType(order.getListingType());
        orderDto.setSecuritiesSymbol(order.getListingSymbol());
        orderDto.setQuantity(order.getQuantity());
        orderDto.setSettlementDate(order.getSettlementDate());
        orderDto.setLimitPrice(order.getLimitPrice());
        orderDto.setStopPrice(order.getStopPrice());
        orderDto.setAllOrNone(order.isAllOrNone());
        orderDto.setMargin(order.isMargin());
        return orderDto;
    }
}
