package rs.edu.raf.BankService.unit.transferTransaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import rs.edu.raf.BankService.controller.TransferTransactionController;
import rs.edu.raf.BankService.data.dto.InternalTransferTransactionDto;
import rs.edu.raf.BankService.exception.AccountNotFoundException;
import rs.edu.raf.BankService.mapper.TransactionMapper;
import rs.edu.raf.BankService.repository.CashAccountRepository;
import rs.edu.raf.BankService.repository.CashTransactionRepository;
import rs.edu.raf.BankService.service.impl.TransactionServiceImpl;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;


@ExtendWith(MockitoExtension.class)
public class TrasnferController {

    @InjectMocks
    private TransferTransactionController trasnferController;
    @Mock
    private TransactionServiceImpl transactionService;


//    @Mock
//    private CashAccountRepository cashAccountRepository;
//    @Mock
//    private CashTransactionRepository cashTransactionRepository;
//
//    @Mock
//    private TransactionMapper transactionMapper;
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }

    @Test
    void testGetAllTransactionByEmail() throws Exception {
//        String email = "admin@gmail.com";
//
//        List<CashAccount> cashAccounts = new ArrayList<>();
//        cashAccounts.add(new CashAccount());
//        when(cashAccountRepository.findAllByEmail(email)).thenReturn(cashAccounts);
//
//        List<GenericTransactionDto> genericTransactionDtos = new ArrayList<>();
//        genericTransactionDtos.add(new GenericTransactionDto());
//
//        when(transactionService.getTransferTransactionsByEmail(email)).thenReturn(genericTransactionDtos);
//
//        List<GenericTransactionDto> responseEntity = trasnferController.getAllTransactionByEmail(email);
//
//        assertNotEquals(Collections.emptyList(), responseEntity);
    }

    @Test
    public void depositWithdrawal_Success(){
        InternalTransferTransactionDto internalTransferTransactionDto = new InternalTransferTransactionDto();

        when(transactionService.depositWithdrawalTransaction(internalTransferTransactionDto)).thenReturn(internalTransferTransactionDto);

        ResponseEntity<?> response = trasnferController.depositWithdrawal(internalTransferTransactionDto);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), internalTransferTransactionDto);
        verify(transactionService, times(1)).depositWithdrawalTransaction(internalTransferTransactionDto);
    }

    @Test
    public void depositWithdrawal_NotFound(){
        String accountNumber = "0932345111111111";
        InternalTransferTransactionDto internalTransferTransactionDto = new InternalTransferTransactionDto();

        when(transactionService.depositWithdrawalTransaction(internalTransferTransactionDto)).thenThrow(new AccountNotFoundException(accountNumber));

        ResponseEntity<?> response = trasnferController.depositWithdrawal(internalTransferTransactionDto);

        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        assertEquals(response.getBody(), "Account with account number " + accountNumber + " not found");
        verify(transactionService, times(1)).depositWithdrawalTransaction(internalTransferTransactionDto);
    }

    @Test
    public void depositWithdrawal_BigAmount(){
        InternalTransferTransactionDto internalTransferTransactionDto = new InternalTransferTransactionDto();

        when(transactionService.depositWithdrawalTransaction(internalTransferTransactionDto)).thenThrow(new RuntimeException("amount is more then balance on account"));

        ResponseEntity<?> response = trasnferController.depositWithdrawal(internalTransferTransactionDto);
        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        assertEquals(response.getBody(), "amount is more then balance on account");
        verify(transactionService, times(1)).depositWithdrawalTransaction(internalTransferTransactionDto);
    }


    
}
