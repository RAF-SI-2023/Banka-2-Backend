package rs.edu.raf.BankService.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.BankService.data.dto.SecuritiesOwnershipDto;
import rs.edu.raf.BankService.data.enums.ListingType;
import rs.edu.raf.BankService.service.SecuritiesOwnershipService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/securities-ownerships")
@AllArgsConstructor
public class SecuritiesOwnershipsController {
    private final SecuritiesOwnershipService securitiesOwnershipService;

    //TODO dodati security u nekom momentu
    @GetMapping("/account-number/{accountNumber}")
    public ResponseEntity<List<SecuritiesOwnershipDto>> getSecurityOwnershipsForAccountNumber(@PathVariable String accountNumber) {
        return ResponseEntity.ok(securitiesOwnershipService.getSecurityOwnershipsForAccountNumber(accountNumber));
    }

    @GetMapping("/security-name/{securitySymbol}")
    public ResponseEntity<List<SecuritiesOwnershipDto>> getSecurityOwnershipsBySecurity(@PathVariable String securitySymbol) {
        return ResponseEntity.ok(securitiesOwnershipService.getSecurityOwnershipsBySecurity(securitySymbol));
    }

    @GetMapping("/all-available")
    public ResponseEntity<List<SecuritiesOwnershipDto>> getAllPubliclyAvailableSecurityOwnerships() {
        return ResponseEntity.ok(securitiesOwnershipService.getAllPubliclyAvailableSecurityOwnerships());
    }


    @PutMapping("/update-publicly-available")
    public ResponseEntity<?> updatePubliclyAvailableQuantity(@RequestBody SecuritiesOwnershipDto soDto) {
        try {
            SecuritiesOwnershipDto dto = securitiesOwnershipService.updatePubliclyAvailableQuantity(soDto);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @GetMapping("/all-available-companies")
    public ResponseEntity<List<SecuritiesOwnershipDto>> getAllPubliclyAvailableSecurityOwnershipsFromCompanies() {
        return ResponseEntity.ok(securitiesOwnershipService.getAllPubliclyAvailableSecurityOwnershipsFromCompanies());
    }


    @GetMapping("/all-available-private")
    public ResponseEntity<List<SecuritiesOwnershipDto>> getAllPubliclyAvailableSecurityOwnershipsFromPrivate() {
        return ResponseEntity.ok(securitiesOwnershipService.getAllPubliclyAvailableSecurityOwnershipsFromPrivates());
    }

    @GetMapping("/securities-values/{accountNumber}")
    public ResponseEntity<Map<ListingType, BigDecimal>> getValuesOfSecurities(@PathVariable String accountNumber) {
        return ResponseEntity.ok(securitiesOwnershipService.getValuesOfSecurities(accountNumber));
    }
}
