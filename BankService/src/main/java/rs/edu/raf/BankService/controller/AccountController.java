package rs.edu.raf.BankService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.BankService.data.dto.AccountNumberDto;
import rs.edu.raf.BankService.data.dto.DomesticCurrencyAccountDto;
import rs.edu.raf.BankService.data.dto.ForeignCurrencyAccountDto;
import rs.edu.raf.BankService.exception.UserAccountAlreadyAssociatedWithUserProfileException;
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
            boolean canAssociateProfile = accountService.userAccountUserProfileConnectionAttempt(accountNumberDto);
            if(canAssociateProfile){
                return ResponseEntity.ok(true);
            }
            return ResponseEntity.badRequest().body("Association is not possible.");
        } catch (UserAccountAlreadyAssociatedWithUserProfileException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/code-confirmation/{accountNumber}")
    public ResponseEntity<?> confirmActivationCode(@PathVariable String accountNumber, @RequestBody Integer code) {
        boolean isConfirmed = accountService.confirmActivationCode(accountNumber, code);
        if(isConfirmed){
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.badRequest().body("Code is not valid.");
    }

    @PostMapping("/create-account/domestic")
    public ResponseEntity<?> createDomesticAccount(@RequestBody DomesticCurrencyAccountDto domesticCurrencyAccountDto) {

        return ResponseEntity.badRequest().body("Account is not created.");
    }

    @PostMapping("/create-account/foreign")
    public ResponseEntity<?> createForeignAccount(@RequestBody ForeignCurrencyAccountDto foreignCurrencyAccountDto) {

        return ResponseEntity.badRequest().body("Account is not created.");
    }



}
