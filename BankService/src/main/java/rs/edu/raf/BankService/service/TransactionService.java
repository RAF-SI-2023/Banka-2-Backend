package rs.edu.raf.BankService.service;

import rs.edu.raf.BankService.data.dto.ExternalTransferTransactionDto;
import rs.edu.raf.BankService.data.dto.GenericTransactionDto;
import rs.edu.raf.BankService.data.dto.InternalTransferTransactionDto;
import rs.edu.raf.BankService.data.enums.TransactionStatus;

import java.util.List;

public interface TransactionService {

    InternalTransferTransactionDto createInternalTransferTransaction(InternalTransferTransactionDto internalTransferTransactionDto);
    ExternalTransferTransactionDto createExternalTransferTransaction(ExternalTransferTransactionDto externalTransferTransactionDto);
    TransactionStatus verifyTransaction(Long transactionId, String verificationToken);
    List<GenericTransactionDto> getTransferTransactions(Long senderId);
}
