package rs.edu.raf.BankService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.BankService.data.dto.CardDto;
import rs.edu.raf.BankService.service.CardService;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/cards", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class CardController {


    private final CardService cardService;

    @PostMapping("/create-card")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE')")
    public ResponseEntity<?> createCard(@RequestBody CardDto cardDto) {
        try {
            return ResponseEntity.ok(cardService.createCard(cardDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }

    }

    @GetMapping(value = "/id/{identificationCardNumber}",consumes = MediaType.ALL_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE','ROLE_USER')")
    public ResponseEntity<?> getCardByIdentificationCardNumber(@PathVariable Long identificationCardNumber) {
        try {
            return ResponseEntity.ok(cardService.getCardByIdentificationCardNumber(identificationCardNumber));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping(value = "/change-status/{identificationCardNumber}",consumes = MediaType.ALL_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE')")
    public ResponseEntity<?> changeCardStatus(@PathVariable Long identificationCardNumber) {
        try {
            return ResponseEntity.ok(cardService.changeCardStatus(identificationCardNumber));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }


    }

    @GetMapping(value = "/account-number/{accountNumber}",consumes = MediaType.ALL_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE','ROLE_USER','ROLE_AGENT','ROLE_SUPERVISOR')")
    public ResponseEntity<?> getCardsByAccountNumber(@PathVariable String accountNumber) {
        try {
            return ResponseEntity.ok(cardService.getCardsByAccountNumber(accountNumber));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping(value = "/change-card-limit",consumes = MediaType.ALL_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE')")
    public ResponseEntity<?> changeCardLimit(@RequestBody CardDto cardDto) {
        CardDto cardDto1 = cardService.changeCardLimit(cardDto);
        if(cardDto1 == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Card with identification card number " + cardDto.getIdentificationCardNumber() + " not found");
        }
            return ResponseEntity.ok(cardDto1);

    }


}
