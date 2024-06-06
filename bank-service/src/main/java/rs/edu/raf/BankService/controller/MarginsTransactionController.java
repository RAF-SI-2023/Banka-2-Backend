package rs.edu.raf.BankService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.BankService.data.dto.MarginsTransactionRequestDto;
import rs.edu.raf.BankService.data.dto.MarginsTransactionResponseDto;
import rs.edu.raf.BankService.service.MarginsTransactionService;

import java.time.LocalDateTime;
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
    public List<MarginsTransactionResponseDto> getTransactions(
            @RequestParam String currencyCode,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {

        return marginsTransactionService.getTransactions(currencyCode, startDate, endDate);
    }
}
