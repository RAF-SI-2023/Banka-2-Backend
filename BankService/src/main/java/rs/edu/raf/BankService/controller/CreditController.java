package rs.edu.raf.BankService.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.models.annotations.OpenAPI30;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.BankService.data.dto.CreditDto;
import rs.edu.raf.BankService.data.dto.CreditRequestDto;
import rs.edu.raf.BankService.service.CreditService;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/credit", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "bankApi")
@OpenAPIDefinition(info = @Info(title = "Bank", version = "1.0", description = "For credit controller-you must sign in with authorize button to use it with swagger, you can get token from" +
        "IAM service and use it to authorize."))
public class CreditController {

    private final CreditService creditService;

    @GetMapping("/all/account-number/{accountNumber}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE','ROLE_USER','ROLE_AGENT','ROLE_SUPERVISOR')")
    @ApiResponse(responseCode = "200", description = "Returns all credits for account number, any role can do this")
    public ResponseEntity<?> findCreditsByAccountNumber(@PathVariable String accountNumber) {
        try {
            return ResponseEntity.ok(creditService.getCreditsByAccountNumber(accountNumber));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/credit-number/{creditNumber}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE','ROLE_USER','ROLE_AGENT','ROLE_SUPERVISOR')")
    @ApiResponse(responseCode = "200", description = "Returns credit by credit number, any role can do this")
    public ResponseEntity<?> getCreditByCreditNumber(@PathVariable Long creditNumber) {
        try {
            return ResponseEntity.ok(creditService.getCreditByCreditNumber(creditNumber));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/credit-requests/create")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_EMPLOYEE','ROLE_ADMIN')")
    @ApiResponse(responseCode = "200", description = "Creates credit request, user,admin and employee can do this")
    public ResponseEntity<?> createCreditRequest(@RequestBody CreditRequestDto creditRequestDto) {
        try {
            return ResponseEntity.ok(creditService.createCreditRequest(creditRequestDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/credit-requests/all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE')")
    @ApiResponse(responseCode = "200", description = "Returns all credit requests, only admin and employee roles can do this")
    public ResponseEntity<?> getAllCreditRequests() {
        try {
            return ResponseEntity.ok(creditService.getAllCreditRequests());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());

        }
    }

    @PostMapping("/credit-requests/approve-and-create/{creditRequestId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE')")
    @ApiResponse(responseCode = "200", description = "Returns all credit requests, only admin and employee roles can do this")
    public ResponseEntity<?> approveCreditRequest(@PathVariable Long creditRequestId) {
        try {
            return ResponseEntity.ok(creditService.approveCreditRequest(creditRequestId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PutMapping("/credit-requests/deny/{creditRequestId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE')")
    @ApiResponse(responseCode = "200", description = "Returns all credit requests, only admin and employee roles can do this")
    public ResponseEntity<?> denyCreditRequest(@PathVariable Long creditRequestId) {
        try {
            return ResponseEntity.ok(creditService.denyCreditRequest(creditRequestId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


}
