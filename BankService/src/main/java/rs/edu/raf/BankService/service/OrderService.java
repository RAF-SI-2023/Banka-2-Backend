package rs.edu.raf.BankService.service;

import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.data.dto.ListingDto;
import rs.edu.raf.BankService.data.dto.OrderDto;
import rs.edu.raf.BankService.data.entities.Order;
import rs.edu.raf.BankService.data.enums.OrderStatus;

import java.util.List;

@Service
public interface OrderService {



    boolean createOrder(OrderDto orderDto);

    List<OrderDto> getAll();

    boolean updateOrderStatus(Long orderId, OrderStatus status);

    List<Order> findAllByUserId(Long id);
    OrderDto findDtoById(Long orderId);

    Order findById(Long orderId);


}
