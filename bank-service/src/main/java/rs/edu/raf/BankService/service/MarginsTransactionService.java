package rs.edu.raf.BankService.service;

import rs.edu.raf.BankService.data.dto.MarginsTransactionRequestDto;
import rs.edu.raf.BankService.data.dto.MarginsTransactionResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface MarginsTransactionService {

    MarginsTransactionResponseDto createTransaction(MarginsTransactionRequestDto marginsTransactionRequestDto);
    List<MarginsTransactionResponseDto> getFilteredTransactions(String currencyCode, LocalDateTime startDate, LocalDateTime endDate);
    List<MarginsTransactionResponseDto> getTransactionsByAccountId(Long marginsAccountId);
}
