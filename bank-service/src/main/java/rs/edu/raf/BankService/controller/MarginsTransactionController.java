package rs.edu.raf.BankService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.BankService.data.dto.MarginsTransactionRequestDto;
import rs.edu.raf.BankService.data.dto.MarginsTransactionResponseDto;
import rs.edu.raf.BankService.service.MarginsTransactionService;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/margins-transaction")
@RestController
public class MarginsTransactionController {

    private final MarginsTransactionService marginsTransactionService;

    @PostMapping
    public MarginsTransactionResponseDto createTransaction(
            @RequestBody MarginsTransactionRequestDto dto) {
        return marginsTransactionService.createTransaction(dto);
    }

    @GetMapping
    public List<MarginsTransactionResponseDto> getFilteredTransactions(
            @RequestParam String currencyCode,
            @RequestParam Long startDate,
            @RequestParam Long endDate) {
        return marginsTransactionService.getFilteredTransactions(currencyCode, startDate, endDate);
    }

    @GetMapping("/account/{accountId}")
    public List<MarginsTransactionResponseDto> getTransactionsByAccountId(@PathVariable Long accountId) {
        return marginsTransactionService.getTransactionsByAccountId(accountId);
    }

    @GetMapping("/all-email/{email}")
    public List<MarginsTransactionResponseDto> getAllTransactionsByEmail(@PathVariable String email) {
        return marginsTransactionService.findAllByEmail(email);
    }
}
