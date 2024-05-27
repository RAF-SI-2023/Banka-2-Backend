package rs.edu.raf.BankService.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.BankService.data.dto.CreateCardDto;
import rs.edu.raf.BankService.service.BankTransferTransactionDetailsService;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/bank-transactions", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class BankTransferTransactionDetailsController {

    private final BankTransferTransactionDetailsService bankTransferTransactionDetailsService;


    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.ok(bankTransferTransactionDetailsService.getAllBankExchangeRates());
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }

    }

    @GetMapping("/total-profit")
    public ResponseEntity<?> getTotalProfit() {
        try {
            return ResponseEntity.ok(bankTransferTransactionDetailsService.getTotalProfit());
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }

    }


}
