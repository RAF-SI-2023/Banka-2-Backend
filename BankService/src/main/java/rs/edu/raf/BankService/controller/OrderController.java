package rs.edu.raf.BankService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.BankService.data.dto.OrderDto;
import rs.edu.raf.BankService.data.enums.OrderStatus;
import rs.edu.raf.BankService.service.OrderService;

import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PreAuthorize("hasAnyRole('ROLE_AGENT','ROLE_SUPERVISOR','ROLE_USER','ROLE_ADMIN','ROLE_EMPLOYEE')")
    @PostMapping()
    public ResponseEntity<Boolean> createOrder(@RequestBody OrderDto orderDto) {
        return ResponseEntity.ok().body(orderService.createOrder(orderDto)); //TODO?
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_AGENT','ROLE_SUPERVISOR','ROLE_EMPLOYEE')")
    @GetMapping()
    public ResponseEntity<?> getAllOrders() {
        return ResponseEntity.ok().body(orderService.getAll());
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_AGENT','ROLE_SUPERVISOR','ROLE_EMPLOYEE')")
    @GetMapping("/non-approved")
    public ResponseEntity<?> getNonApprovedOrders() {
        return ResponseEntity.ok().body(orderService.getAll().stream().filter(orderDto -> orderDto.getOrderStatus().equals(OrderStatus.WAITING_FOR_APPROVAL)).collect(Collectors.toList()));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_AGENT','ROLE_SUPERVISOR','ROLE_EMPLOYEE')")
    @GetMapping("/approved")
    public ResponseEntity<?> getApprovedOrders() {
        return ResponseEntity.ok().body(orderService.getAll().stream().filter(orderDto -> orderDto.getOrderStatus().equals(OrderStatus.APPROVED)).collect(Collectors.toList()));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE','ROLE_SUPERVISOR')")
    @PutMapping("/approve/{id}")
    public ResponseEntity<?> approveOrder(@PathVariable Long id) {
        return ResponseEntity.ok().body(orderService.updateOrderStatus(id, OrderStatus.APPROVED));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE','ROLE_SUPERVISOR')")
    @PutMapping("/reject/{id}")
    public ResponseEntity<?> rejectOrder(@PathVariable Long id) {

        return ResponseEntity.ok().body(orderService.updateOrderStatus(id, OrderStatus.DENIED));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE','ROLE_SUPERVISOR')")
    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<?> findOrderById(@PathVariable Long id) {
        return ResponseEntity.ok().body(orderService.findDtoById(id));
    }
}

