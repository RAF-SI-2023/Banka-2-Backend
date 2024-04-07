package rs.edu.raf.BankService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.BankService.data.dto.OrderDto;
import rs.edu.raf.BankService.service.OrderService;

@RestController
@CrossOrigin
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping()
    public ResponseEntity<?> createOrder(OrderDto orderDto){
        return null;
    }

    public ResponseEntity<?> getAllOrders(){
        return null;
    }

    public ResponseEntity<?> getNonApprovedOrders(){
        return null;
    }

    public ResponseEntity<?> getApprovedOrders(){
        return null;
    }

    @PutMapping("/approve/{id}")
    public ResponseEntity<?> approveOrder(@PathVariable String id){
        return null;
    }

    @PutMapping("/reject/{id}")
    public ResponseEntity<?> rejectOrder(@PathVariable String id){
        return null;
    }
}

