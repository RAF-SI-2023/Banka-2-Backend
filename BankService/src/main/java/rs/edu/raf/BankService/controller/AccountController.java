package rs.edu.raf.BankService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.BankService.data.dto.AccountNumberDto;
import rs.edu.raf.BankService.data.dto.BusinessAccountDto;
import rs.edu.raf.BankService.data.dto.DomesticCurrencyAccountDto;
import rs.edu.raf.BankService.data.dto.ForeignCurrencyAccountDto;
import rs.edu.raf.BankService.exception.AccountNotFoundException;
import rs.edu.raf.BankService.exception.AccountNumberAlreadyExistException;
import rs.edu.raf.BankService.exception.UserAccountAlreadyAssociatedWithUserProfileException;
import rs.edu.raf.BankService.exception.UserAccountInProcessOfBindingWithUserProfileException;
import rs.edu.raf.BankService.service.AccountService;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/accounts", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

    private final AccountService accountService;


    @PostMapping("/associate-profile-initialization")
    public ResponseEntity<?> login(@RequestBody AccountNumberDto accountNumberDto) {
        try {
            return ResponseEntity.ok(accountService.userAccountUserProfileConnectionAttempt(accountNumberDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/code-confirmation/{accountNumber}")
    public ResponseEntity<?> confirmActivationCode(@PathVariable String accountNumber, @RequestBody Integer code) {
        try {
            return ResponseEntity.ok(accountService.confirmActivationCode(accountNumber, code));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/create-account/domestic")
    public ResponseEntity<?> createDomesticAccount(@RequestBody DomesticCurrencyAccountDto domesticCurrencyAccountDto) {
        try {
            return ResponseEntity.ok(accountService.createDomesticCurrencyAccount(domesticCurrencyAccountDto));
        } catch (AccountNumberAlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/create-account/foreign")
    public ResponseEntity<?> createForeignAccount(@RequestBody ForeignCurrencyAccountDto foreignCurrencyAccountDto) {
        try {
            return ResponseEntity.ok(accountService.createForeignCurrencyAccount(foreignCurrencyAccountDto));
        } catch (AccountNumberAlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/create-account/business")
    public ResponseEntity<?> createBusinessAccount(@RequestBody BusinessAccountDto businessAccountDto) {
        try {
            return ResponseEntity.ok(accountService.createBusinessAccount(businessAccountDto));
        } catch (AccountNumberAlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping(value = "/find-by-email/{email}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<?> findAccountsByEmail(@PathVariable String email) {
        try {
            return ResponseEntity.ok(accountService.findAccountsByEmail(email));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
