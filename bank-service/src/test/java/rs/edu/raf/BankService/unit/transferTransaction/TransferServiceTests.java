package rs.edu.raf.BankService.unit.transferTransaction;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.webjars.NotFoundException;
import rs.edu.raf.BankService.data.dto.*;
import rs.edu.raf.BankService.data.entities.SecuritiesOwnership;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.data.entities.transactions.ExternalTransferTransaction;
import rs.edu.raf.BankService.data.entities.transactions.InternalTransferTransaction;
import rs.edu.raf.BankService.data.entities.transactions.SecuritiesTransaction;
import rs.edu.raf.BankService.data.entities.transactions.TransferTransaction;
import rs.edu.raf.BankService.data.enums.AccountType;
import rs.edu.raf.BankService.data.enums.TransactionStatus;
import rs.edu.raf.BankService.exception.AccountNotFoundException;
import rs.edu.raf.BankService.exception.InvalidInternalTransferException;
import rs.edu.raf.BankService.exception.TransactionNotFoundException;
import rs.edu.raf.BankService.mapper.TransactionMapper;
import rs.edu.raf.BankService.repository.CashAccountRepository;
import rs.edu.raf.BankService.repository.CashTransactionRepository;
import rs.edu.raf.BankService.repository.SecuritiesOwnershipRepository;
import rs.edu.raf.BankService.service.ActionAgentProfitService;
import rs.edu.raf.BankService.service.CurrencyExchangeService;
import rs.edu.raf.BankService.service.impl.TransactionServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class TransferServiceTests {

    @InjectMocks
    private TransactionServiceImpl transactionService;
    @Mock
    private CashAccountRepository cashAccountRepository;
    @Mock
    private CashTransactionRepository cashTransactionRepository;
    @Mock
    private TransactionMapper transactionMapper;
    @Mock
    private RabbitTemplate rabbitTemplate;
    @Mock
    private CurrencyExchangeService currencyExchangeService;
    @Mock
    private SecuritiesOwnershipRepository securitiesOwnershipRepository;
    @Mock
    private ActionAgentProfitService actionAgentProfitService;


//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }


    @Test
    void testCreateInternalTransferTransaction_Confirmed(){
        InternalTransferTransactionDto internalTransferTransactionDto = new InternalTransferTransactionDto();
        internalTransferTransactionDto.setSenderAccountNumber("123");
        internalTransferTransactionDto.setReceiverAccountNumber("456");
        internalTransferTransactionDto.setAmount(100L);

        CashAccount cashAccountSender = new CashAccount();
        cashAccountSender.setAvailableBalance(150L);
        cashAccountSender.setEmail("email@example.com");
        cashAccountSender.setCurrencyCode("RSD");
        cashAccountSender.setAccountNumber("123");
        cashAccountSender.setAccountType(AccountType.BANK_ACCOUNT);
        CashAccount cashAccountReceiver = new CashAccount();
        cashAccountReceiver.setAvailableBalance(50L);
        cashAccountReceiver.setEmail("email@example.com");
        cashAccountReceiver.setCurrencyCode("RSD");
        cashAccountReceiver.setAccountNumber("456");
        cashAccountReceiver.setAccountType(AccountType.BANK_ACCOUNT);

        when(cashAccountRepository.findByAccountNumber(internalTransferTransactionDto.getSenderAccountNumber())).thenReturn(cashAccountSender);
        when(cashAccountRepository.findByAccountNumber(internalTransferTransactionDto.getReceiverAccountNumber())).thenReturn(cashAccountReceiver);

        InternalTransferTransaction transaction = new InternalTransferTransaction();
        transaction.setAmount(internalTransferTransactionDto.getAmount());
        when(transactionMapper.toInternalTransferTransactionEntity(internalTransferTransactionDto)).thenReturn(transaction);


        when(cashTransactionRepository.save(transaction)).thenReturn(transaction);

        when(transactionMapper.toInternalTransferTransactionDto(any(InternalTransferTransaction.class)))
                .thenAnswer(new Answer<InternalTransferTransactionDto>() {
                    @Override
                    public InternalTransferTransactionDto answer(InvocationOnMock invocation) throws Throwable {
                        InternalTransferTransaction transaction = (InternalTransferTransaction) invocation.getArguments()[0];
                        InternalTransferTransactionDto dto = new InternalTransferTransactionDto();
                        dto.setStatus(transaction.getStatus());
                        return dto;
                    }
                });

        InternalTransferTransactionDto result = transactionService.createInternalTransferTransaction(internalTransferTransactionDto);

        assertEquals(TransactionStatus.CONFIRMED, result.getStatus());
    }

    @Test
    void testCreateInternalTransferTransaction_Declined(){
        InternalTransferTransactionDto internalTransferTransactionDto = new InternalTransferTransactionDto();
        internalTransferTransactionDto.setSenderAccountNumber("123");
        internalTransferTransactionDto.setReceiverAccountNumber("456");
        internalTransferTransactionDto.setAmount(200L);

        CashAccount cashAccountSender = new CashAccount();
        cashAccountSender.setAvailableBalance(150L);
        cashAccountSender.setEmail("email@example.com");
        cashAccountSender.setCurrencyCode("RSD");
        cashAccountSender.setAccountNumber("123");
        cashAccountSender.setAccountType(AccountType.BANK_ACCOUNT);
        CashAccount cashAccountReceiver = new CashAccount();
        cashAccountReceiver.setAvailableBalance(50L);
        cashAccountReceiver.setEmail("email@example.com");
        cashAccountReceiver.setCurrencyCode("RSD");
        cashAccountReceiver.setAccountNumber("456");
        cashAccountReceiver.setAccountType(AccountType.BANK_ACCOUNT);

        when(cashAccountRepository.findByAccountNumber(internalTransferTransactionDto.getSenderAccountNumber())).thenReturn(cashAccountSender);
        when(cashAccountRepository.findByAccountNumber(internalTransferTransactionDto.getReceiverAccountNumber())).thenReturn(cashAccountReceiver);

        InternalTransferTransaction transaction = new InternalTransferTransaction();
        transaction.setAmount(internalTransferTransactionDto.getAmount());
        when(transactionMapper.toInternalTransferTransactionEntity(internalTransferTransactionDto)).thenReturn(transaction);


        when(cashTransactionRepository.save(transaction)).thenReturn(transaction);

        when(transactionMapper.toInternalTransferTransactionDto(any(InternalTransferTransaction.class)))
                .thenAnswer(new Answer<InternalTransferTransactionDto>() {
                    @Override
                    public InternalTransferTransactionDto answer(InvocationOnMock invocation) throws Throwable {
                        InternalTransferTransaction transaction = (InternalTransferTransaction) invocation.getArguments()[0];
                        InternalTransferTransactionDto dto = new InternalTransferTransactionDto();
                        dto.setStatus(transaction.getStatus());
                        return dto;
                    }
                });

        InternalTransferTransactionDto result = transactionService.createInternalTransferTransaction(internalTransferTransactionDto);

        assertEquals(TransactionStatus.DECLINED, result.getStatus());
    }

    @Test
    void testCreateInternalTransferTransaction_AccNotFound(){
        InternalTransferTransactionDto internalTransferTransactionDto = new InternalTransferTransactionDto();
        internalTransferTransactionDto.setSenderAccountNumber("123");
        internalTransferTransactionDto.setReceiverAccountNumber("456");

        CashAccount cashAccountSender = new CashAccount();

        when(cashAccountRepository.findByAccountNumber(internalTransferTransactionDto.getSenderAccountNumber())).thenReturn(cashAccountSender);
        when(cashAccountRepository.findByAccountNumber(internalTransferTransactionDto.getReceiverAccountNumber())).thenReturn(null);

        assertThrows(AccountNotFoundException.class, () -> {
            transactionService.createInternalTransferTransaction(internalTransferTransactionDto);
        });
    }

    @Test
    void testCreateInternalTransferTransaction_AccNotFromSameUser(){
        InternalTransferTransactionDto internalTransferTransactionDto = new InternalTransferTransactionDto();
        internalTransferTransactionDto.setSenderAccountNumber("123");
        internalTransferTransactionDto.setReceiverAccountNumber("456");
        internalTransferTransactionDto.setAmount(200L);

        CashAccount cashAccountSender = new CashAccount();
        cashAccountSender.setEmail("email@example.com");
        CashAccount cashAccountReceiver = new CashAccount();
        cashAccountReceiver.setEmail("different@example.com");

        when(cashAccountRepository.findByAccountNumber(internalTransferTransactionDto.getSenderAccountNumber())).thenReturn(cashAccountSender);
        when(cashAccountRepository.findByAccountNumber(internalTransferTransactionDto.getReceiverAccountNumber())).thenReturn(cashAccountReceiver);

        assertThrows(InvalidInternalTransferException.class, () -> {
            transactionService.createInternalTransferTransaction(internalTransferTransactionDto);
        });
    }

    @Test
    void testCreateInternalTransferTransaction_SameAccNumber(){
        InternalTransferTransactionDto internalTransferTransactionDto = new InternalTransferTransactionDto();
        internalTransferTransactionDto.setSenderAccountNumber("123");
        internalTransferTransactionDto.setReceiverAccountNumber("123");

        CashAccount cashAccountSender = new CashAccount();
        cashAccountSender.setAvailableBalance(150L);
        cashAccountSender.setEmail("email@example.com");
        cashAccountSender.setAccountNumber("123");
        CashAccount cashAccountReceiver = new CashAccount();
        cashAccountReceiver.setAvailableBalance(50L);
        cashAccountReceiver.setEmail("email@example.com");
        cashAccountReceiver.setAccountNumber("123");

        when(cashAccountRepository.findByAccountNumber(internalTransferTransactionDto.getSenderAccountNumber())).thenReturn(cashAccountSender);
        when(cashAccountRepository.findByAccountNumber(internalTransferTransactionDto.getReceiverAccountNumber())).thenReturn(cashAccountReceiver);

        assertThrows(InvalidInternalTransferException.class, () -> {
            transactionService.createInternalTransferTransaction(internalTransferTransactionDto);
        });
    }

    @Test
    void testCreateInternalTransferTransaction_DiffAccTypes(){
        InternalTransferTransactionDto internalTransferTransactionDto = new InternalTransferTransactionDto();
        internalTransferTransactionDto.setSenderAccountNumber("123");
        internalTransferTransactionDto.setReceiverAccountNumber("456");

        CashAccount cashAccountSender = new CashAccount();
        cashAccountSender.setAvailableBalance(150L);
        cashAccountSender.setEmail("email@example.com");
        cashAccountSender.setAccountNumber("123");
        cashAccountSender.setAccountType(AccountType.BANK_ACCOUNT);
        CashAccount cashAccountReceiver = new CashAccount();
        cashAccountReceiver.setAvailableBalance(50L);
        cashAccountReceiver.setEmail("email@example.com");
        cashAccountReceiver.setAccountNumber("456");
        cashAccountReceiver.setAccountType(AccountType.BUSINESS_ACCOUNT);

        when(cashAccountRepository.findByAccountNumber(internalTransferTransactionDto.getSenderAccountNumber())).thenReturn(cashAccountSender);
        when(cashAccountRepository.findByAccountNumber(internalTransferTransactionDto.getReceiverAccountNumber())).thenReturn(cashAccountReceiver);

        assertThrows(InvalidInternalTransferException.class, () -> {
            transactionService.createInternalTransferTransaction(internalTransferTransactionDto);
        });
    }

    @Test
    void testCreateInternalTransferTransaction_DiffCurrency(){
        InternalTransferTransactionDto internalTransferTransactionDto = new InternalTransferTransactionDto();
        internalTransferTransactionDto.setSenderAccountNumber("123");
        internalTransferTransactionDto.setReceiverAccountNumber("456");

        CashAccount cashAccountSender = new CashAccount();
        cashAccountSender.setAvailableBalance(150L);
        cashAccountSender.setEmail("email@example.com");
        cashAccountSender.setAccountNumber("123");
        cashAccountSender.setAccountType(AccountType.BANK_ACCOUNT);
        cashAccountSender.setCurrencyCode("RSD");
        CashAccount cashAccountReceiver = new CashAccount();
        cashAccountReceiver.setAvailableBalance(50L);
        cashAccountReceiver.setEmail("email@example.com");
        cashAccountReceiver.setAccountNumber("456");
        cashAccountReceiver.setAccountType(AccountType.BANK_ACCOUNT);
        cashAccountReceiver.setCurrencyCode("EUR");

        when(cashAccountRepository.findByAccountNumber(internalTransferTransactionDto.getSenderAccountNumber())).thenReturn(cashAccountSender);
        when(cashAccountRepository.findByAccountNumber(internalTransferTransactionDto.getReceiverAccountNumber())).thenReturn(cashAccountReceiver);

        assertThrows(InvalidInternalTransferException.class, () -> {
            transactionService.createInternalTransferTransaction(internalTransferTransactionDto);
        });
    }

    @Test
    void testCreateExternalTransferTransaction_Pending(){
        ExternalTransferTransactionDto externalTransferTransactionDto = new ExternalTransferTransactionDto();
        externalTransferTransactionDto.setSenderAccountNumber("123");
        externalTransferTransactionDto.setReceiverAccountNumber("456");
        externalTransferTransactionDto.setAmount(100L);

        CashAccount cashAccountSender = new CashAccount();
        cashAccountSender.setAvailableBalance(200L);
        CashAccount cashAccountReceiver = new CashAccount();
        cashAccountReceiver.setAvailableBalance(50L);

        when(cashAccountRepository.findByAccountNumber(externalTransferTransactionDto.getSenderAccountNumber())).thenReturn(cashAccountSender);
        when(cashAccountRepository.findByAccountNumber(externalTransferTransactionDto.getReceiverAccountNumber())).thenReturn(cashAccountReceiver);

        ExternalTransferTransaction transaction = new ExternalTransferTransaction();
        transaction.setAmount(externalTransferTransactionDto.getAmount());
        when(transactionMapper.toExternalTransferTransactionEntity(externalTransferTransactionDto)).thenReturn(transaction);


        when(cashTransactionRepository.save(transaction)).thenReturn(transaction);

        when(transactionMapper.toExternalTransferTransactionDto(any(ExternalTransferTransaction.class)))
                .thenAnswer(new Answer<ExternalTransferTransactionDto>() {
                    @Override
                    public ExternalTransferTransactionDto answer(InvocationOnMock invocation) throws Throwable {
                        ExternalTransferTransaction transaction = (ExternalTransferTransaction) invocation.getArguments()[0];
                        ExternalTransferTransactionDto dto = new ExternalTransferTransactionDto();
                        dto.setStatus(transaction.getStatus());
                        return dto;
                    }
                });

        ExternalTransferTransactionDto result = transactionService.createExternalTransferTransaction(externalTransferTransactionDto);

        assertEquals(TransactionStatus.PENDING, result.getStatus());
        verify(rabbitTemplate,times(1)).convertAndSend(any(String.class),any(TransferTransactionVerificationDto.class));
    }

    @Test
    void testCreateExternalTransferTransaction_Declined(){
        ExternalTransferTransactionDto externalTransferTransactionDto = new ExternalTransferTransactionDto();
        externalTransferTransactionDto.setSenderAccountNumber("123");
        externalTransferTransactionDto.setReceiverAccountNumber("456");
        externalTransferTransactionDto.setAmount(100L);

        CashAccount cashAccountSender = new CashAccount();
        cashAccountSender.setAvailableBalance(0L);
        CashAccount cashAccountReceiver = new CashAccount();
        cashAccountReceiver.setAvailableBalance(50L);

        when(cashAccountRepository.findByAccountNumber(externalTransferTransactionDto.getSenderAccountNumber())).thenReturn(cashAccountSender);
        when(cashAccountRepository.findByAccountNumber(externalTransferTransactionDto.getReceiverAccountNumber())).thenReturn(cashAccountReceiver);

        ExternalTransferTransaction transaction = new ExternalTransferTransaction();
        transaction.setAmount(externalTransferTransactionDto.getAmount());
        when(transactionMapper.toExternalTransferTransactionEntity(externalTransferTransactionDto)).thenReturn(transaction);


        when(cashTransactionRepository.save(transaction)).thenReturn(transaction);

        when(transactionMapper.toExternalTransferTransactionDto(any(ExternalTransferTransaction.class)))
                .thenAnswer(new Answer<ExternalTransferTransactionDto>() {
                    @Override
                    public ExternalTransferTransactionDto answer(InvocationOnMock invocation) throws Throwable {
                        ExternalTransferTransaction transaction = (ExternalTransferTransaction) invocation.getArguments()[0];
                        ExternalTransferTransactionDto dto = new ExternalTransferTransactionDto();
                        dto.setStatus(transaction.getStatus());
                        return dto;
                    }
                });

        ExternalTransferTransactionDto result = transactionService.createExternalTransferTransaction(externalTransferTransactionDto);

        assertEquals(TransactionStatus.DECLINED, result.getStatus());
        verify(rabbitTemplate,never()).convertAndSend(any(String.class),any(TransferTransactionVerificationDto.class));
    }

    @Test
    void testCreateExternalTransferTransaction_AccNotFound(){
        ExternalTransferTransactionDto externalTransferTransactionDto = new ExternalTransferTransactionDto();
        externalTransferTransactionDto.setSenderAccountNumber("123");
        externalTransferTransactionDto.setReceiverAccountNumber("456");

        CashAccount cashAccountSender = new CashAccount();

        when(cashAccountRepository.findByAccountNumber(externalTransferTransactionDto.getSenderAccountNumber())).thenReturn(cashAccountSender);
        when(cashAccountRepository.findByAccountNumber(externalTransferTransactionDto.getReceiverAccountNumber())).thenReturn(null);

        assertThrows(AccountNotFoundException.class, () -> {
            transactionService.createExternalTransferTransaction(externalTransferTransactionDto);
        });
    }

    @Test
    void testVerifyTransaction_CorrectToken() {
        Long transactionId = 1L;
        String verificationToken = "correctToken";

        ExternalTransferTransaction transferTransaction = new ExternalTransferTransaction();
        transferTransaction.setId(transactionId);
        transferTransaction.setVerificationToken(verificationToken);
        transferTransaction.setStatus(TransactionStatus.PENDING);

        CashAccount senderCashAccount = new CashAccount();
        senderCashAccount.setAvailableBalance(200L);
        senderCashAccount.setAccountNumber("123");
        transferTransaction.setSenderCashAccount(senderCashAccount);

        CashAccount receiverCashAccount = new CashAccount();
        receiverCashAccount.setAvailableBalance(100L);
        receiverCashAccount.setAccountNumber("456");
        transferTransaction.setReceiverCashAccount(receiverCashAccount);

        transferTransaction.setAmount(50L);

        when(cashTransactionRepository.findById(transactionId)).thenReturn(Optional.of(transferTransaction));
        when(cashAccountRepository.findByAccountNumber(senderCashAccount.getAccountNumber())).thenReturn(senderCashAccount);
        when(cashAccountRepository.findByAccountNumber(receiverCashAccount.getAccountNumber())).thenReturn(receiverCashAccount);

        TransactionStatus result = transactionService.verifyTransaction(transactionId, verificationToken);

        assertEquals(TransactionStatus.CONFIRMED, result);
        assertEquals(150L, senderCashAccount.getAvailableBalance(), 0);
        assertEquals(150L, receiverCashAccount.getAvailableBalance(), 0);
        verify(cashAccountRepository, times(1)).saveAll(anyList());
        verify(cashTransactionRepository, times(1)).save(transferTransaction);
    }

    @Test
    void testVerifyTransaction_WrongToken() {
        Long transactionId = 1L;
        String verificationToken = "correctToken";

        ExternalTransferTransaction transferTransaction = new ExternalTransferTransaction();
        transferTransaction.setId(transactionId);
        transferTransaction.setVerificationToken("wrongToken");
        transferTransaction.setStatus(TransactionStatus.PENDING);

        CashAccount senderCashAccount = new CashAccount();
        senderCashAccount.setAvailableBalance(200L);
        senderCashAccount.setAccountNumber("123");
        transferTransaction.setSenderCashAccount(senderCashAccount);

        CashAccount receiverCashAccount = new CashAccount();
        receiverCashAccount.setAvailableBalance(100L);
        receiverCashAccount.setAccountNumber("456");
        transferTransaction.setReceiverCashAccount(receiverCashAccount);

        transferTransaction.setAmount(50L);

        when(cashTransactionRepository.findById(transactionId)).thenReturn(Optional.of(transferTransaction));
        when(cashAccountRepository.findByAccountNumber(senderCashAccount.getAccountNumber())).thenReturn(senderCashAccount);
        when(cashAccountRepository.findByAccountNumber(receiverCashAccount.getAccountNumber())).thenReturn(receiverCashAccount);

        TransactionStatus result = transactionService.verifyTransaction(transactionId, verificationToken);

        assertEquals(TransactionStatus.DECLINED, result);
        verify(cashAccountRepository, never()).saveAll(anyList());
        verify(cashTransactionRepository, times(1)).save(transferTransaction);
    }

    @Test
    void testVerifyTransaction_TransactionNotFound() {
        when(cashTransactionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> {
            transactionService.verifyTransaction(1L, "token");
        });
        verify(cashAccountRepository, never()).saveAll(anyList());
        verify(cashTransactionRepository, never()).save(any());
    }

    @Test
    void testGetTransferTransactions_UserFound() {
        Long userId = 1L;

        TransferTransaction sentTransaction1 = new TransferTransaction();
        TransferTransaction sentTransaction2 = new TransferTransaction();

        CashAccount cashAccount = new CashAccount();
        cashAccount.setSentTransferTransactions(List.of(sentTransaction1, sentTransaction2));

        GenericTransactionDto sentTransactionDto1 = new GenericTransactionDto();
        GenericTransactionDto sentTransactionDto2 = new GenericTransactionDto();

        when(cashAccountRepository.findById(userId)).thenReturn(Optional.of(cashAccount));

        when(transactionMapper.toGenericTransactionDto(sentTransaction1)).thenReturn(sentTransactionDto1);
        when(transactionMapper.toGenericTransactionDto(sentTransaction2)).thenReturn(sentTransactionDto2);

        List<GenericTransactionDto> result = transactionService.getTransferTransactions(userId);

        assertEquals(2, result.size());
        assertTrue(result.contains(sentTransactionDto1));
        assertTrue(result.contains(sentTransactionDto2));
    }

    @Test
    void testGetTransferTransactions_UserNotFound() {
        Long userId = 1L;

        when(cashAccountRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> {
            transactionService.getTransferTransactions(userId);
        });
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
    void testReserveFundsAccNumber() {
        String accountNumber = "123";
        double amount = 50.0;

        CashAccount cashAccount = new CashAccount();
        cashAccount.setAvailableBalance(100.0);
        cashAccount.setReservedFunds(0.0);

        when(cashAccountRepository.findByAccountNumber(accountNumber)).thenReturn(cashAccount);

        boolean result = transactionService.reserveFunds(accountNumber, amount);

        assertTrue(result);
        assertEquals(50.0, cashAccount.getReservedFunds(), 0.0);
    }

    @Test
    void testReserveFundsAccNumber_AccNotFound() {
        String accountNumber = "123";
        double amount = 50.0;

        when(cashAccountRepository.findByAccountNumber(accountNumber)).thenReturn(null);

        assertThrows(AccountNotFoundException.class, () -> {
            transactionService.reserveFunds(accountNumber, amount);
        });
    }

    @Test
    void testReserveFundsCashAcc() {
        double amount = 50.0;

        CashAccount cashAccount = new CashAccount();
        cashAccount.setAvailableBalance(100.0);
        cashAccount.setReservedFunds(0.0);

        boolean result = transactionService.reserveFunds(cashAccount, amount);

        assertTrue(result);
        assertEquals(50.0, cashAccount.getReservedFunds(), 0.0);
    }

    @Test
    void testReserveFundsCashAcc_InsufficientFunds() {
        double amount = 150.0;

        CashAccount cashAccount = new CashAccount();
        cashAccount.setAvailableBalance(100.0);
        cashAccount.setReservedFunds(0.0);

        assertThrows(RuntimeException.class, () -> {
            transactionService.reserveFunds(cashAccount, amount);
        });
    }

    @Test
    void testReleaseFundsAccNumber() {
        String accountNumber = "123";
        double amount = 50.0;

        CashAccount cashAccount = new CashAccount();
        cashAccount.setAvailableBalance(100.0);
        cashAccount.setReservedFunds(50.0);

        when(cashAccountRepository.findByAccountNumber(accountNumber)).thenReturn(cashAccount);

        boolean result = transactionService.releaseFunds(accountNumber, amount);

        assertTrue(result);
        assertEquals(50.0, cashAccount.getAvailableBalance(), 0.0);
        assertEquals(0.0, cashAccount.getReservedFunds(), 0.0);
    }

    @Test
    void testReleaseFundsAccNumber_AccNotFound(){
        String accountNumber = "123";
        double amount = 50.0;

        when(cashAccountRepository.findByAccountNumber(accountNumber)).thenReturn(null);

        assertThrows(AccountNotFoundException.class, () -> {
            transactionService.releaseFunds(accountNumber, amount);
        });
    }

    @Test
    void testReleaseFundsCashAcc(){
        double amount = 50.0;

        CashAccount cashAccount = new CashAccount();
        cashAccount.setAvailableBalance(100.0);
        cashAccount.setReservedFunds(50.0);

        boolean result = transactionService.releaseFunds(cashAccount, amount);

        assertTrue(result);
        assertEquals(50.0, cashAccount.getAvailableBalance(), 0.0);
        assertEquals(0.0, cashAccount.getReservedFunds(), 0.0);
    }

    @Test
    void testReleaseFundsCashAcc_InsufficientFunds(){
        double amount = 150.0;

        CashAccount cashAccount = new CashAccount();
        cashAccount.setAvailableBalance(100.0);
        cashAccount.setReservedFunds(50.0);

        assertThrows(RuntimeException.class, () -> {
            transactionService.releaseFunds(cashAccount, amount);
        });
    }

    @Test
    void testAddFunds() {
        double amount = 50.0;

        CashAccount cashAccount = new CashAccount();
        cashAccount.setAvailableBalance(100.0);

        boolean result = transactionService.addFunds(cashAccount, amount);

        assertTrue(result);
        assertEquals(150.0, cashAccount.getAvailableBalance(), 0.0);
        verify(cashAccountRepository, times(1)).save(cashAccount);
    }

    @Test
    void testTransferFunds() {
        String senderAccountNumber = "123";
        String receiverAccountNumber = "456";
        double amount = 50.0;

        CashAccount senderAccount = new CashAccount();
        senderAccount.setAvailableBalance(100.0);
        senderAccount.setReservedFunds(50.0);
        senderAccount.setCurrencyCode("USD");

        CashAccount receiverAccount = new CashAccount();
        receiverAccount.setAvailableBalance(100.0);
        receiverAccount.setCurrencyCode("EUR");

        when(cashAccountRepository.findByAccountNumber(senderAccountNumber)).thenReturn(senderAccount);
        when(cashAccountRepository.findByAccountNumber(receiverAccountNumber)).thenReturn(receiverAccount);
        when(currencyExchangeService.calculateAmountBetweenCurrencies(senderAccount.getCurrencyCode(), receiverAccount.getCurrencyCode(), amount)).thenReturn(amount);

        boolean result = transactionService.transferFunds(senderAccountNumber, receiverAccountNumber, amount);

        assertTrue(result);
        assertEquals(50.0, senderAccount.getAvailableBalance(), 0.0);
        assertEquals(150.0, receiverAccount.getAvailableBalance(), 0.0);
    }

    @Test
    void testTransferFunds_AccNotFound() {
        String senderAccountNumber = "123";
        String receiverAccountNumber = "456";
        double amount = 50.0;

        CashAccount senderAccount = new CashAccount();
        senderAccount.setAvailableBalance(100.0);
        senderAccount.setReservedFunds(50.0);

        when(cashAccountRepository.findByAccountNumber(senderAccountNumber)).thenReturn(senderAccount);
        when(cashAccountRepository.findByAccountNumber(receiverAccountNumber)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> {
            transactionService.transferFunds(senderAccountNumber, receiverAccountNumber, amount);
        });
    }

//    @Test
//    void testCreateSecuritiesTransaction() {
//        ContractDto contractDto = new ContractDto();
//        contractDto.setTotalPrice(100.0);
//        contractDto.setVolume(10);
//        contractDto.setTicker("ABC");
//        contractDto.setBuyersEmail("buyer@example.com");
//        contractDto.setSellersEmail("seller@example.com");
//
//        CashAccount buyer = new CashAccount();
//        buyer.setAvailableBalance(200.0);
//        buyer.setAccountNumber("123");
//        buyer.setEmail("buyer@example.com");
//
//        CashAccount seller = new CashAccount();
//        seller.setAvailableBalance(100.0);
//        seller.setAccountNumber("456");
//        seller.setEmail("seller@example.com");
//
//        SecuritiesOwnership buyerSecurities = new SecuritiesOwnership();
//        buyerSecurities.setAccountNumber(buyer.getAccountNumber());
//        buyerSecurities.setEmail(buyer.getEmail());
//        buyerSecurities.setSecuritiesSymbol(contractDto.getTicker());
//        buyerSecurities.setQuantity(0);
//
//        SecuritiesOwnership sellerSecurities = new SecuritiesOwnership();
//        sellerSecurities.setAccountNumber(seller.getAccountNumber());
//        sellerSecurities.setEmail(seller.getEmail());
//        sellerSecurities.setSecuritiesSymbol(contractDto.getTicker());
//        sellerSecurities.setQuantity(20);
//        sellerSecurities.setQuantityOfPubliclyAvailable(20);
//
//        SecuritiesTransaction transaction = new SecuritiesTransaction();
//        transaction.setAmount(contractDto.getTotalPrice());
//        transaction.setSecuritiesSymbol(contractDto.getTicker());
//        transaction.setQuantityToTransfer(contractDto.getVolume());
//        transaction.setReceiverCashAccount(seller);
//        transaction.setSenderCashAccount(buyer);
//        transaction.setStatus(TransactionStatus.PENDING);
//
//        when(cashAccountRepository.findPrimaryTradingAccount(contractDto.getBuyersEmail())).thenReturn(buyer);
//        when(cashAccountRepository.findPrimaryTradingAccount(contractDto.getSellersEmail())).thenReturn(seller);
//        when(securitiesOwnershipRepository.findAllByAccountNumberAndSecuritiesSymbol(buyer.getAccountNumber(), contractDto.getTicker())).thenReturn(List.of(buyerSecurities));
//        when(securitiesOwnershipRepository.findAllByAccountNumberAndSecuritiesSymbol(seller.getAccountNumber(), contractDto.getTicker())).thenReturn(List.of(sellerSecurities));
//
//        when(cashAccountRepository.findByAccountNumber(buyer.getAccountNumber())).thenReturn(buyer);
//        when(cashAccountRepository.findByAccountNumber(seller.getAccountNumber())).thenReturn(seller);
//        when(cashTransactionRepository.save(any(SecuritiesTransaction.class))).thenAnswer(new Answer<SecuritiesTransaction>() {
//            @Override
//            public SecuritiesTransaction answer(InvocationOnMock invocation) throws Throwable {
//                SecuritiesTransaction transaction = (SecuritiesTransaction) invocation.getArguments()[0];
//                return transaction;
//            }
//        });
//
//        when(transactionMapper.toGenericTransactionDto(any(SecuritiesTransaction.class)))
//                .thenAnswer(new Answer<GenericTransactionDto>() {
//                    @Override
//                    public GenericTransactionDto answer(InvocationOnMock invocation) throws Throwable {
//                        SecuritiesTransaction transaction = (SecuritiesTransaction) invocation.getArguments()[0];
//                        GenericTransactionDto dto = new GenericTransactionDto();
//
//                        dto.setStatus(transaction.getStatus());
//                        return dto;
//                    }
//                });
//
//        GenericTransactionDto result = transactionService.createSecuritiesTransaction(contractDto);
//
//        assertEquals(TransactionStatus.CONFIRMED, result.getStatus());
//        assertEquals(10L, (long)buyerSecurities.getQuantity());
//        assertEquals(10L, (long)sellerSecurities.getQuantity());
//        verify(actionAgentProfitService,times(1)).createAgentProfit(any(Object.class),any(SecuritiesOwnership.class),anyInt());
//    }

    @Test
    void testCreateSecuritiesTransaction_AccNotFound() {
        ContractDto contractDto = new ContractDto();
        contractDto.setBuyersEmail("buyer@example.com");
        contractDto.setSellersEmail("seller@example.com");

        when(cashAccountRepository.findPrimaryTradingAccount(contractDto.getBuyersEmail())).thenReturn(null);

        assertThrows(RuntimeException.class, () -> {
            transactionService.createSecuritiesTransaction(contractDto);
        });
    }

    @Test
    void testCreateSecuritiesTransaction_DoNotProcess() {
        ContractDto contractDto = new ContractDto();
        contractDto.setTotalPrice(100.0);
        contractDto.setVolume(10);
        contractDto.setTicker("ABC");
        contractDto.setBuyersEmail("buyer@example.com");
        contractDto.setSellersEmail("seller@example.com");

        CashAccount buyer = new CashAccount();
        buyer.setAvailableBalance(50.0);
        buyer.setAccountNumber("123");
        buyer.setEmail("buyer@example.com");

        CashAccount seller = new CashAccount();
        seller.setAvailableBalance(100.0);
        seller.setAccountNumber("456");
        seller.setEmail("seller@example.com");

        SecuritiesOwnership buyerSecurities = new SecuritiesOwnership();
        buyerSecurities.setAccountNumber(buyer.getAccountNumber());
        buyerSecurities.setEmail(buyer.getEmail());
        buyerSecurities.setSecuritiesSymbol(contractDto.getTicker());
        buyerSecurities.setQuantity(0);

        SecuritiesOwnership sellerSecurities = new SecuritiesOwnership();
        sellerSecurities.setAccountNumber(seller.getAccountNumber());
        sellerSecurities.setEmail(seller.getEmail());
        sellerSecurities.setSecuritiesSymbol(contractDto.getTicker());
        sellerSecurities.setQuantity(5);

        when(cashAccountRepository.findPrimaryTradingAccount(contractDto.getBuyersEmail())).thenReturn(buyer);
        when(cashAccountRepository.findPrimaryTradingAccount(contractDto.getSellersEmail())).thenReturn(seller);
        when(securitiesOwnershipRepository.findAllByAccountNumberAndSecuritiesSymbol(buyer.getAccountNumber(), contractDto.getTicker())).thenReturn(List.of(buyerSecurities));
        when(securitiesOwnershipRepository.findAllByAccountNumberAndSecuritiesSymbol(seller.getAccountNumber(), contractDto.getTicker())).thenReturn(List.of(sellerSecurities));

        assertThrows(RuntimeException.class, () -> {
            transactionService.createSecuritiesTransaction(contractDto);
        });
    }

    @Test
    void testDepositWithdrawalTransaction() {
        InternalTransferTransactionDto dto = new InternalTransferTransactionDto();
        dto.setSenderAccountNumber("123");
        dto.setAmount(100L);

        CashAccount cashAccount = new CashAccount();
        cashAccount.setAvailableBalance(200.0);
        cashAccount.setAccountNumber("123");

        when(cashAccountRepository.findByAccountNumber(dto.getSenderAccountNumber())).thenReturn(cashAccount);
        when(transactionMapper.toInternalTransferTransactionEntity(dto)).thenReturn(new InternalTransferTransaction());

        InternalTransferTransactionDto result = transactionService.depositWithdrawalTransaction(dto);

        assertEquals(dto.getAmount(), result.getAmount());
        assertEquals(300.0, cashAccount.getAvailableBalance(), 0.0);
    }

    @Test
    void testDepositWithdrawalTransaction_AccNotFound() {
        InternalTransferTransactionDto dto = new InternalTransferTransactionDto();
        dto.setSenderAccountNumber("123");

        when(cashAccountRepository.findByAccountNumber(dto.getSenderAccountNumber())).thenReturn(null);

        assertThrows(AccountNotFoundException.class, () -> {
            transactionService.depositWithdrawalTransaction(dto);
        });
    }

    @Test
    void testDepositWithdrawalTransaction_Deposit() {
        InternalTransferTransactionDto dto = new InternalTransferTransactionDto();
        dto.setSenderAccountNumber("123");
        dto.setAmount(100L);

        CashAccount cashAccount = new CashAccount();
        cashAccount.setAvailableBalance(200.0);
        cashAccount.setAccountNumber("123");

        when(cashAccountRepository.findByAccountNumber(dto.getSenderAccountNumber())).thenReturn(cashAccount);
        when(transactionMapper.toInternalTransferTransactionEntity(dto)).thenReturn(new InternalTransferTransaction());

        InternalTransferTransactionDto result = transactionService.depositWithdrawalTransaction(dto);

        assertEquals(dto.getAmount(), result.getAmount());
        assertEquals(300.0, cashAccount.getAvailableBalance(), 0.0);
    }

    @Test
    void testDepositWithdrawalTransaction_Withdrawal() {
        InternalTransferTransactionDto dto = new InternalTransferTransactionDto();
        dto.setSenderAccountNumber("123");
        dto.setAmount(-100L);

        CashAccount cashAccount = new CashAccount();
        cashAccount.setAvailableBalance(200.0);
        cashAccount.setAccountNumber("123");

        when(cashAccountRepository.findByAccountNumber(dto.getSenderAccountNumber())).thenReturn(cashAccount);
        when(transactionMapper.toInternalTransferTransactionEntity(dto)).thenReturn(new InternalTransferTransaction());

        InternalTransferTransactionDto result = transactionService.depositWithdrawalTransaction(dto);

        assertEquals(dto.getAmount(), result.getAmount());
        assertEquals(100.0, cashAccount.getAvailableBalance(), 0.0);
    }

    @Test
    void testDepositWithdrawalTransaction_WithdrawalNotEnoughBalance() {
        InternalTransferTransactionDto dto = new InternalTransferTransactionDto();
        dto.setSenderAccountNumber("123");
        dto.setAmount(-300L);

        CashAccount cashAccount = new CashAccount();
        cashAccount.setAvailableBalance(200.0);
        cashAccount.setAccountNumber("123");

        when(cashAccountRepository.findByAccountNumber(dto.getSenderAccountNumber())).thenReturn(cashAccount);

        assertThrows(RuntimeException.class, () -> {
            transactionService.depositWithdrawalTransaction(dto);
        });
    }


}
