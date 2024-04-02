package rs.edu.raf.BankService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.BankService.data.dto.CreditDto;
import rs.edu.raf.BankService.data.dto.CreditRequestDto;
import rs.edu.raf.BankService.service.CreditService;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/credit", produces = MediaType.APPLICATION_JSON_VALUE)
public class CreditController {

    private final CreditService creditService;

  /*/  @PostMapping("/create") //TODO: implement
    public ResponseEntity<?> createCredit(@RequestBody CreditDto creditDto) {
        return ResponseEntity.ok(creditService.createCredit(creditDto,creditRequestDto));
    }*/

    @GetMapping("/all/account-number/{accountNumber}")
    public ResponseEntity<?> findCreditsByAccountNumber(@PathVariable String accountNumber) {
        try {
            return ResponseEntity.ok(creditService.getCreditsByAccountNumber(accountNumber));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/credit-number/{creditNumber}")
    public ResponseEntity<?> getCreditByCreditNumber(@PathVariable Long creditNumber) {
        try {
            return ResponseEntity.ok(creditService.getCreditByCreditNumber(creditNumber));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/credit-requests/create")
    public ResponseEntity<?> createCreditRequest(@RequestBody CreditRequestDto creditRequestDto) {
        try {
            return ResponseEntity.ok(creditService.createCreditRequest(creditRequestDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/credit-requests/all")
    public ResponseEntity<?> getAllCreditRequests() {
        try {
            return ResponseEntity.ok(creditService.getAllCreditRequests());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PostMapping("/credit-requests/approve-and-create/{creditRequestId}")
    public ResponseEntity<?> approveCreditRequest(@PathVariable Long creditRequestId, @RequestBody CreditDto creditDto) {
        try {
            return ResponseEntity.ok(creditService.approveCreditRequest(creditRequestId, creditDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }


}
