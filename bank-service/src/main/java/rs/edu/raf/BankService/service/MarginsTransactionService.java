package rs.edu.raf.BankService.service;

import rs.edu.raf.BankService.data.dto.MarginsTransactionDto;

public interface MarginsTransactionService {

    MarginsTransactionDto createTransaction(MarginsTransactionDto marginsTransactionDto);
}
