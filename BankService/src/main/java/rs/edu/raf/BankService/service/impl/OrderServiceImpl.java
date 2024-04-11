package rs.edu.raf.BankService.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import rs.edu.raf.BankService.data.dto.AgentDto;
import rs.edu.raf.BankService.data.dto.OrderDto;
import rs.edu.raf.BankService.data.entities.Order;
import rs.edu.raf.BankService.data.enums.OrderStatus;
import rs.edu.raf.BankService.exception.AgentLimitReachedException;
import rs.edu.raf.BankService.exception.OrderNotFoundException;
import rs.edu.raf.BankService.filters.principal.CustomUserPrincipal;
import rs.edu.raf.BankService.mapper.OrderMapper;
import rs.edu.raf.BankService.repository.OrderRepository;
import rs.edu.raf.BankService.service.OrderService;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private RestClient iamServiceRestClient = RestClient.create("http://localhost:8000/api");
    private OrderMapper orderMapper;
    private OrderRepository orderRepository;

    @Override
    public boolean create(OrderDto orderDto) {
        // obzirom da je specifikacija za ordere uzas i kod za order je uzas
        Order order = orderMapper.orderDtoToOrder(orderDto);
        order.setInitiatedByUserId(((CustomUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId());
        if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains("ROLE_AGENT")){

            order.setOwnedByBank(true);

            Long agentId = ((CustomUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
            AgentDto agentDto = iamServiceRestClient.get()
                    .uri("/users/" + agentId)
                    .retrieve()
                    .body(AgentDto.class);

            if(agentDto == null){
                return false;
            }

            if(agentDto.isOrderApprovalRequired()) {
                order.setOrderStatus(OrderStatus.WAITING_FOR_APPROVAL);
            }

            Double stockPrice = new Random().nextDouble(100,150);
            double totalPrice = stockPrice * order.getQuantity();

            if(totalPrice > agentDto.getLeftOfLimit()){
                throw new AgentLimitReachedException();
            }

            iamServiceRestClient.get().uri("/users/decrease-limit/" + agentId + "/" + totalPrice);
        }

        orderRepository.save(order);
        return true;
    }

    @Override
    public List<OrderDto> getAll() {
        return orderRepository.findAll().stream().map(orderMapper::orderToOrderDto).collect(Collectors.toList());
    }

    @Override
    public boolean updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
        order.setOrderStatus(status);
        return true;
    }

    @Override
    public List<Order> findAllByUserId(Long id) {
        return null;
    }
}
