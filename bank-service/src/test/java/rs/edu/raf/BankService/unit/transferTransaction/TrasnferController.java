package rs.edu.raf.BankService.unit.transferTransaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rs.edu.raf.BankService.controller.TransferTransactionController;
import rs.edu.raf.BankService.mapper.TransactionMapper;
import rs.edu.raf.BankService.repository.CashAccountRepository;
import rs.edu.raf.BankService.repository.CashTransactionRepository;
import rs.edu.raf.BankService.service.impl.TransactionServiceImpl;


public class TrasnferController {

    @InjectMocks
    private TransactionServiceImpl transactionService;
    @Mock
    private CashAccountRepository cashAccountRepository;
    @Mock
    private CashTransactionRepository cashTransactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransferTransactionController trasnferController;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


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

}
