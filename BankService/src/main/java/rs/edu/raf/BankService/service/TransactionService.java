package rs.edu.raf.BankService.service;

import rs.edu.raf.BankService.data.dto.ExternalTransferTransactionDto;
import rs.edu.raf.BankService.data.dto.GenericTransactionDto;
import rs.edu.raf.BankService.data.dto.InternalTransferTransactionDto;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.data.enums.TransactionStatus;

import java.util.List;

public interface TransactionService {

    InternalTransferTransactionDto createInternalTransferTransaction(InternalTransferTransactionDto internalTransferTransactionDto);
    ExternalTransferTransactionDto createExternalTransferTransaction(ExternalTransferTransactionDto externalTransferTransactionDto);
    TransactionStatus verifyTransaction(Long transactionId, String verificationToken);
    List<GenericTransactionDto> getTransferTransactions(Long senderId);
    boolean reserveFunds(String accountNumber, double amount);
    boolean reserveFunds(CashAccount cashAccount, double amount);
    boolean releaseFunds(String accountNumber, double amount);
    boolean releaseFunds(CashAccount cashAccount, double amount);
    boolean addFunds(CashAccount cashAccount, double amount);
    boolean transferFunds(String senderAccountNumber, String receiverAccountNumber, double amount);
}
