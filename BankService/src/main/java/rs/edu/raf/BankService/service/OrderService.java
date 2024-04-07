package rs.edu.raf.BankService.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.data.dto.OrderDto;
import rs.edu.raf.BankService.data.entities.Order;
import rs.edu.raf.BankService.data.enums.OrderStatus;

import java.util.List;

@Service
public interface OrderService {

    OrderDto save(OrderDto orderDto);

    List<OrderDto> findAll();

    OrderDto updateOrderStatus(Long orderId, OrderStatus status);

    List<Order> findAllByUserId(Long id);
}
