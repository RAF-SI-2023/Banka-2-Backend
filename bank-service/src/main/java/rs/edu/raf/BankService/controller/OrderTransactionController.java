package rs.edu.raf.BankService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.BankService.service.OrderTransactionService;
import rs.edu.raf.BankService.service.impl.OrderTransactionServiceImpl;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/order-transactions")
public class OrderTransactionController {
    private final OrderTransactionService orderTransactionService;


    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok().body(orderTransactionService.findAll());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> findById(@PathVariable long id) {
        return ResponseEntity.ok().body(orderTransactionService.findById(id));
    }

    @GetMapping("/order-id/{orderId}")
    public ResponseEntity<?> findByOrderId(@PathVariable long orderId) {
        return ResponseEntity.ok().body(orderTransactionService.findByOrderId(orderId));
    }

    @GetMapping("/account-number/{accountNumber}")
    public ResponseEntity<?> findAllByAccountNumber(@PathVariable String accountNumber) {
        return ResponseEntity.ok().body(orderTransactionService.findAllByAccountNumber(accountNumber));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> findAllByEmail(@PathVariable String email) {
        return ResponseEntity.ok().body(orderTransactionService.findAllByEmail(email));
    }


}
