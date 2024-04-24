package rs.edu.raf.BankService.controller;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//import rs.edu.raf.BankService.data.dto.OrderDto;
//import rs.edu.raf.BankService.data.enums.OrderStatus;
//import rs.edu.raf.BankService.service.OrderService;
//
//import java.util.stream.Collectors;

//@RestController
//@CrossOrigin
//@RequestMapping("/api/orders")
//public class OrderController {
//
//    private final OrderService orderService;
//
//    @Autowired
//    public OrderController(OrderService orderService) {
//        this.orderService = orderService;
//    }
//
//    @PreAuthorize("hasAnyRole('ROLE_AGENT','ROLE_SUPERVISOR')")
//    @PostMapping()
//    public ResponseEntity<Boolean> createOrder(OrderDto orderDto){
//        return ResponseEntity.ok().body(orderService.create(orderDto));
//    }
//
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_AGENT','ROLE_SUPERVISOR')")
//    @GetMapping()
//    public ResponseEntity<?> getAllOrders(){
//        return ResponseEntity.ok().body(orderService.getAll());
//    }
//
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_AGENT','ROLE_SUPERVISOR')")
//    @GetMapping("/non-approved")
//    public ResponseEntity<?> getNonApprovedOrders(){
//        return ResponseEntity.ok().body(orderService.getAll().stream().filter(orderDto -> orderDto.getOrderStatus().equals(OrderStatus.WAITING_FOR_APPROVAL)).collect(Collectors.toList()));
//    }
//
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_AGENT','ROLE_SUPERVISOR')")
//    @GetMapping("/approved")
//    public ResponseEntity<?> getApprovedOrders(){
//        return ResponseEntity.ok().body(orderService.getAll().stream().filter(orderDto -> orderDto.getOrderStatus().equals(OrderStatus.APPROVED)).collect(Collectors.toList()));
//    }
//
//    @PreAuthorize("hasRole('ROLE_SUPERVISOR')")
//    @PutMapping("/approve/{id}")
//    public ResponseEntity<?> approveOrder(@PathVariable Long id){
//        return ResponseEntity.ok().body(orderService.updateOrderStatus(id, OrderStatus.APPROVED));
//    }
//
//    @PreAuthorize("hasRole('ROLE_SUPERVISOR')")
//    @PutMapping("/reject/{id}")
//    public ResponseEntity<?> rejectOrder(@PathVariable Long id){
//        return ResponseEntity.ok().body(orderService.updateOrderStatus(id, OrderStatus.REJECTED));
//    }
//}

