package rs.edu.raf.BankService.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.data.dto.OrderDto;
import rs.edu.raf.BankService.data.entities.Order;
import rs.edu.raf.BankService.data.enums.OrderStatus;
import rs.edu.raf.BankService.filters.principal.CustomUserPrincipal;
import rs.edu.raf.BankService.mapper.OrderMapper;
import rs.edu.raf.BankService.repository.OrderRepository;
import rs.edu.raf.BankService.service.OrderService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private OrderMapper orderMapper;
    private OrderRepository orderRepository;

    @Override
    public OrderDto save(OrderDto orderDto) {

        Order order = orderMapper.orderDtoToOrder(orderDto);
        order.setInitiatedByUserId(((CustomUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId());
        if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains("ROLE_AGENT")){
            // proveriti da li je agent prekoracio limit
            // proveriti da li je potrebno odobrenje ordera od strane supervisora

        }

        return null;
    }



    @Override
    public List<OrderDto> findAll() {

        return null;
    }



    @Override
    public OrderDto updateOrderStatus(Long orderId, OrderStatus status) {
        return null;
    }



    @Override
    public List<Order> findAllByUserId(Long id) {
        return null;
    }
}
