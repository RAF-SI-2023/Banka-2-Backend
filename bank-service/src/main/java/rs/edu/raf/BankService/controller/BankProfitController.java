package rs.edu.raf.BankService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.edu.raf.BankService.service.BankProfitService;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/bank-profit", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class BankProfitController {

    private final BankProfitService bankProfitService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.ok(bankProfitService.getTotalProfit());
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
