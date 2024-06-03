package rs.edu.raf.BankService.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.BankService.data.dto.*;
import rs.edu.raf.BankService.exception.AccountNumberAlreadyExistException;
import rs.edu.raf.BankService.service.CashAccountService;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/accounts", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

    private final CashAccountService cashAccountService;


    @PostMapping("/associate-profile-initialization")
    public ResponseEntity<?> associateProfileWithAccount(@RequestBody AccountNumberDto accountNumberDto) {
        try {
            return ResponseEntity.ok(cashAccountService.userAccountUserProfileConnectionAttempt(accountNumberDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/code-confirmation/{accountNumber}")
    public ResponseEntity<?> confirmActivationCode(@PathVariable String accountNumber, @RequestBody Integer code) {
        try {
            return ResponseEntity.ok(cashAccountService.confirmActivationCode(accountNumber, code));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/create-account/domestic")
    public ResponseEntity<?> createDomesticAccount(@RequestBody DomesticCurrencyAccountDto domesticCurrencyAccountDto) {
        try {
            return ResponseEntity.ok(cashAccountService.createDomesticCurrencyAccount(domesticCurrencyAccountDto));
        } catch (AccountNumberAlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/create-account/foreign")
    public ResponseEntity<?> createForeignAccount(@RequestBody ForeignCurrencyAccountDto foreignCurrencyAccountDto) {
        try {
            return ResponseEntity.ok(cashAccountService.createForeignCurrencyAccount(foreignCurrencyAccountDto));
        } catch (AccountNumberAlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/create-account/business")
    public ResponseEntity<?> createBusinessAccount(@RequestBody BusinessAccountDto businessAccountDto) {
        try {
            return ResponseEntity.ok(cashAccountService.createBusinessAccount(businessAccountDto));
        } catch (AccountNumberAlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping(value = "/find-by-email/{email}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<?> findAccountsByEmail(@PathVariable String email) {
        try {
            return ResponseEntity.ok(cashAccountService.findAccountsByEmail(email));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/cashe-account-state")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPERVISOR')")
    @ApiResponse(responseCode = "200", description = "Returns all account params for account number, and admin,supervisor role can do this")
    public ResponseEntity<?> findAccountByAccountNumber(@RequestBody AccountNumberDto accountNumberDto) {
        try {
            return ResponseEntity.ok(cashAccountService.findAccountByNumber(accountNumberDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @PostMapping("/savedAccount/{accountId}/")
    public SavedAccountDto createSavedAccount(@PathVariable Long accountId,
                                              @RequestBody SavedAccountDto savedAccountDto) {
        return cashAccountService.createSavedAccount(accountId, savedAccountDto);

    }

    @PutMapping("/savedAccount/{accountId}/{savedAccountNumber}")
    public SavedAccountDto updateSavedAccount(@PathVariable Long accountId,
                                              @PathVariable String savedAccountNumber,
                                              @RequestBody SavedAccountDto savedAccountDto) {
        return cashAccountService.updateSavedAccount(accountId, savedAccountNumber, savedAccountDto);
    }

    @DeleteMapping("/savedAccount/{accountId}/")
    public void deleteSavedAccount(@PathVariable Long accountId,
                                   @RequestBody String savedAccountNumber) {
        cashAccountService.deleteSavedAccount(accountId, savedAccountNumber);
    }

    @PutMapping("/set-primary-trading-account/{accountNumber}")
    public ResponseEntity<?> setIsAccountPrimaryForTrading(@PathVariable String accountNumber,
                                                           @RequestBody boolean usedForSecurities) {
        return ResponseEntity.ok(cashAccountService.setIsAccountPrimaryForTrading(accountNumber, usedForSecurities));
    }


}
