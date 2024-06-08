package rs.edu.raf.BankService.unit.transferTransaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import rs.edu.raf.BankService.controller.TransferTransactionController;
import rs.edu.raf.BankService.data.dto.ContractDto;
import rs.edu.raf.BankService.data.dto.ExternalTransferTransactionDto;
import rs.edu.raf.BankService.data.dto.GenericTransactionDto;
import rs.edu.raf.BankService.data.dto.InternalTransferTransactionDto;
import rs.edu.raf.BankService.data.enums.TransactionStatus;
import rs.edu.raf.BankService.service.TransactionService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;


public class TransferControllerTests {

    @InjectMocks
    private TransferTransactionController transferTransactionController;

    @Mock
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateInternalTransferTransaction() {
        InternalTransferTransactionDto dto = new InternalTransferTransactionDto();
        when(transactionService.createInternalTransferTransaction(dto)).thenReturn(dto);

        InternalTransferTransactionDto result = transferTransactionController.createInternalTransferTransaction(dto);

        assertEquals(dto, result);
        verify(transactionService, times(1)).createInternalTransferTransaction(dto);
    }

    @Test
    void testCreateExternalTransferTransaction() {
        ExternalTransferTransactionDto dto = new ExternalTransferTransactionDto();
        when(transactionService.createExternalTransferTransaction(dto)).thenReturn(dto);

        ExternalTransferTransactionDto result = transferTransactionController.createExternalTransferTransaction(dto);

        assertEquals(dto, result);
        verify(transactionService, times(1)).createExternalTransferTransaction(dto);
    }

    @Test
    void testVerifyTransaction() {
        Long transactionId = 1L;
        String verificationToken = "token";
        TransactionStatus status = TransactionStatus.CONFIRMED;
        when(transactionService.verifyTransaction(transactionId, verificationToken)).thenReturn(status);

        TransactionStatus result = transferTransactionController.verifyTransaction(transactionId, verificationToken);

        assertEquals(status, result);
        verify(transactionService, times(1)).verifyTransaction(transactionId, verificationToken);
    }

    @Test
    void testGetAllTransaction() {
        Long userId = 1L;
        List<GenericTransactionDto> transactions = Collections.singletonList(new GenericTransactionDto());
        when(transactionService.getTransferTransactions(userId)).thenReturn(transactions);

        List<GenericTransactionDto> result = transferTransactionController.getAllTransaction(userId);

        assertEquals(transactions, result);
        verify(transactionService, times(1)).getTransferTransactions(userId);
    }

    @Test
    void testGetAllTransactionByEmail() {
        String email = "test@test.com";
        List<GenericTransactionDto> transactions = Collections.singletonList(new GenericTransactionDto());
        when(transactionService.getTransferTransactionsByEmail(email)).thenReturn(transactions);

        List<GenericTransactionDto> result = transferTransactionController.getAllTransactionByEmail(email);

        assertEquals(transactions, result);
        verify(transactionService, times(1)).getTransferTransactionsByEmail(email);
    }

    @Test
    void testCreateSecuritiesTransaction() {
        ContractDto contractDto = new ContractDto();
        GenericTransactionDto genericTransactionDto = new GenericTransactionDto();
        when(transactionService.createSecuritiesTransaction(contractDto)).thenReturn(genericTransactionDto);

        ResponseEntity<?> result = transferTransactionController.createSecuritiesTransaction(contractDto);

        assertEquals(ResponseEntity.ok(genericTransactionDto), result);
        verify(transactionService, times(1)).createSecuritiesTransaction(contractDto);
    }

}
