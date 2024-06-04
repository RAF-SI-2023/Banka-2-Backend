package rs.edu.raf.BankService.unit.transferTransaction;

import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rs.edu.raf.BankService.data.dto.GenericTransactionDto;
import rs.edu.raf.BankService.data.dto.InternalTransferTransactionDto;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.data.entities.transactions.InternalTransferTransaction;
import rs.edu.raf.BankService.data.entities.transactions.TransferTransaction;
import rs.edu.raf.BankService.exception.AccountNotFoundException;
import rs.edu.raf.BankService.mapper.TransactionMapper;
import rs.edu.raf.BankService.repository.CashAccountRepository;
import rs.edu.raf.BankService.repository.CashTransactionRepository;
import rs.edu.raf.BankService.service.impl.TransactionServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class TransferService {

    @InjectMocks
    private TransactionServiceImpl transactionService;
    @Mock
    private CashAccountRepository cashAccountRepository;
    @Mock
    private CashTransactionRepository cashTransactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetTransferTransactionsByEmail_UserNotFound() {
        String email = "nonexistent@example.com";

        // Mock the repository to return an empty list
        List<CashAccount> cashAccounts = new ArrayList<>();

        when(cashAccountRepository.findAllByEmail(email)).thenReturn(cashAccounts);

        // Assert that the appropriate exception is thrown
        assertThrows(AccountNotFoundException.class, () -> {
            transactionService.getTransferTransactionsByEmail(email);
        });
    }

    @Test
    void testGetTransferTransactionsByEmail_UserFound() {
        String email = "user@example.com";

        // Create mock TransferTransaction objects
        TransferTransaction sentTransaction = new TransferTransaction();
        TransferTransaction receivedTransaction = new TransferTransaction();

        // Create mock CashAccount with sent and received transactions
        CashAccount cashAccount = new CashAccount();
        cashAccount.setSentTransferTransactions(List.of(sentTransaction));
        cashAccount.setReceivedTransferTransactions(List.of(receivedTransaction));

        // Create mock GenericTransactionDto objects
        GenericTransactionDto sentTransactionDto = new GenericTransactionDto();
        GenericTransactionDto receivedTransactionDto = new GenericTransactionDto();

        // Mock the repository to return a list containing the mock CashAccount
        when(cashAccountRepository.findAllByEmail(email)).thenReturn(List.of(cashAccount));

        // Mock the transactionMapper to map TransferTransaction to GenericTransactionDto
        when(transactionMapper.toGenericTransactionDto(sentTransaction)).thenReturn(sentTransactionDto);
        when(transactionMapper.toGenericTransactionDto(receivedTransaction)).thenReturn(receivedTransactionDto);

        // Call the method
        List<GenericTransactionDto> result = transactionService.getTransferTransactionsByEmail(email);

        // Assert the results
        assertEquals(2, result.size());
        assertTrue(result.contains(sentTransactionDto));
        assertTrue(result.contains(receivedTransactionDto));
    }

    @Test
    public void depositWithdrawalTransaction_Success(){
        String accountNumber = "0932345111111111";
        Long amount = 100L;
        CashAccount cashAccount = new CashAccount();
        InternalTransferTransaction internalTransferTransaction = new InternalTransferTransaction();
        InternalTransferTransactionDto internalTransferTransactionDto = mock(InternalTransferTransactionDto.class);
        internalTransferTransactionDto.setSenderAccountNumber(accountNumber);
        internalTransferTransactionDto.setAmount(amount);

        when(cashAccountRepository.findByAccountNumber(internalTransferTransactionDto.getSenderAccountNumber())).thenReturn(cashAccount);
        when(transactionMapper.toInternalTransferTransactionEntity(internalTransferTransactionDto)).thenReturn(internalTransferTransaction);

        assertEquals(transactionService.depositWithdrawalTransaction(internalTransferTransactionDto), internalTransferTransactionDto);
        verify(cashAccountRepository, times(1)).findByAccountNumber(internalTransferTransactionDto.getSenderAccountNumber());
        verify(cashAccountRepository, times(1)).save(cashAccount);
        verify(cashTransactionRepository, times(1)).save(transactionMapper.toInternalTransferTransactionEntity(internalTransferTransactionDto));
    }


    @Test
    public void depositWithdrawalTransaction_NotFound(){
        InternalTransferTransactionDto internalTransferTransactionDto = new InternalTransferTransactionDto();

        when(cashAccountRepository.findByAccountNumber(internalTransferTransactionDto.getSenderAccountNumber())).thenReturn(null);

        assertThrows(AccountNotFoundException.class, () -> transactionService.depositWithdrawalTransaction(internalTransferTransactionDto));
        verify(cashAccountRepository, times(1)).findByAccountNumber(internalTransferTransactionDto.getSenderAccountNumber());
    }

    @Test
    public void depositWithdrawalTransaction_ToBigAmount(){
        InternalTransferTransactionDto internalTransferTransactionDto = new InternalTransferTransactionDto();
        String accountNumber = "0932345111111111";
        Long amount = 999990000000000000L;
        internalTransferTransactionDto.setSenderAccountNumber(accountNumber);
        internalTransferTransactionDto.setAmount(amount);

        assertThrows(RuntimeException.class, () -> transactionService.depositWithdrawalTransaction(internalTransferTransactionDto));
    }




}
