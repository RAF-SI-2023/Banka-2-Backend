package rs.edu.raf.OTCService.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.OTCService.data.dto.ContractDto;
import rs.edu.raf.OTCService.service.ContractService;

@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
@CrossOrigin
public class ContractController {
    private final ContractService contractService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllContracts() {
        return ResponseEntity.ok(contractService.getAllContracts());
    }

    @GetMapping("/all-waiting")
    public ResponseEntity<?> getAllWaitingContracts() {
        return ResponseEntity.ok(contractService.getAllWaitingContracts());

    }

    @GetMapping("/all-approved")
    public ResponseEntity<?> getAllApprovedContracts() {
        return ResponseEntity.ok(contractService.getAllApprovedContracts());

    }

    @GetMapping("/all-rejected")
    public ResponseEntity<?> getAllRejectedContracts() {
        return ResponseEntity.ok(contractService.getAllRejectedContracts());

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getContractById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(contractService.getContractById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createContract(@RequestBody ContractDto contractDto) {
        try {
            return ResponseEntity.ok(contractService.createContract(contractDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/approve-seller/{id}")
    public ResponseEntity<?> sellerApproveContractById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(contractService.sellerApproveContractById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @PutMapping("/approve-bank/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE','ROLE_AGENT','ROLE_SUPERVISOR')")
    public ResponseEntity<?> bankApproveContractById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(contractService.bankApproveContractById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/deny-seller/{id}")
    public ResponseEntity<?> sellerDenyContractById(@PathVariable Long id, @RequestBody String message) {
        try {
            return ResponseEntity.ok(contractService.sellerDenyContractById(id, message));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @PutMapping("/deny-bank/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE','ROLE_AGENT','ROLE_SUPERVISOR')")
    public ResponseEntity<?> bankDenyContractById(@PathVariable Long id, @RequestBody String message) {
        try {
            return ResponseEntity.ok(contractService.bankDenyContractById(id, message));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
