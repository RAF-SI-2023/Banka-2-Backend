package rs.edu.raf.BankService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.BankService.data.dto.MarginsAccountDto;
import rs.edu.raf.BankService.service.MarginsAccountService;

@RestController
@RequestMapping("/api/margins-account")
@RequiredArgsConstructor
@CrossOrigin
public class MarginsAccountController {

    private final MarginsAccountService marginsAccountService;

    @PostMapping
    public ResponseEntity<MarginsAccountDto> createMarginsAccount(@RequestBody MarginsAccountDto marginsAccountDto) {
        MarginsAccountDto createdAccount = marginsAccountService.createMarginsAccount(marginsAccountDto);
        return ResponseEntity.ok(createdAccount);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MarginsAccountDto> updateMarginsAccount(@PathVariable Long id, @RequestBody MarginsAccountDto marginsAccountDto) {
        MarginsAccountDto updatedAccount = marginsAccountService.updateMarginsAccount(id, marginsAccountDto);
        return ResponseEntity.ok(updatedAccount);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMarginsAccount(@PathVariable Long id) {
        marginsAccountService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarginsAccountDto> getMarginsAccountById(@PathVariable Long id) {
        MarginsAccountDto marginsAccount = marginsAccountService.findById(id);
        return ResponseEntity.ok(marginsAccount);
    }
}