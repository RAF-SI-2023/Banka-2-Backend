package rs.edu.raf.BankService.e2e.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import rs.edu.raf.BankService.data.dto.ExternalTransferTransactionDto;
import rs.edu.raf.BankService.data.dto.InternalTransferTransactionDto;
import rs.edu.raf.BankService.data.entities.accounts.Account;
import rs.edu.raf.BankService.data.entities.transactions.ExternalTransferTransaction;
import rs.edu.raf.BankService.data.entities.transactions.TransferTransaction;
import rs.edu.raf.BankService.data.enums.AccountType;
import rs.edu.raf.BankService.data.enums.TransactionStatus;
import rs.edu.raf.BankService.data.enums.UserAccountUserProfileLinkState;
import rs.edu.raf.BankService.repository.AccountRepository;
import rs.edu.raf.BankService.repository.TransactionRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class TransferTransactionE2ETests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    private Long transactionId;

    @Transactional
    @Test
    public void createInternalTransferTransaction_Success() throws Exception {
        transactionSetup(10000L,
                10000L,
                "email",
                "email");

        InternalTransferTransactionDto requestDto = new InternalTransferTransactionDto();
        requestDto.setAmount(1000L);
        requestDto.setSenderAccountNumber("111");
        requestDto.setReceiverAccountNumber("222");

        ResultActions resultActions = mockMvc.perform(
                        post("http://localhost:8003/api/transaction/internal")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();
        InternalTransferTransactionDto responseDto = objectMapper.readValue(responseBody, InternalTransferTransactionDto.class);

        assertEquals(responseDto.getStatus(), TransactionStatus.CONFIRMED);

        Account updatedSenderAccount = accountRepository.findByAccountNumber("111");
        Account updatedReceiverAccount = accountRepository.findByAccountNumber("222");

        assertEquals(updatedSenderAccount.getAvailableBalance(), 9000L);
        assertEquals(updatedReceiverAccount.getAvailableBalance(), 11000L);

        Optional<TransferTransaction> transaction = transactionRepository.findById(Long.valueOf(responseDto.getId()));
        assertTrue(transaction.isPresent());
        assertEquals(transaction.get().getStatus(), TransactionStatus.CONFIRMED);
    }

    @Transactional
    @Test
    public void createInternalTransferTransaction_InsufficientFunds() throws Exception {
        transactionSetup(10000L,
                10000L,
                "email",
                "email");

        InternalTransferTransactionDto requestDto = new InternalTransferTransactionDto();
        requestDto.setAmount(20000L);
        requestDto.setSenderAccountNumber("111");
        requestDto.setReceiverAccountNumber("222");

        ResultActions resultActions = mockMvc.perform(
                        post("http://localhost:8003/api/transaction/internal")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();
        InternalTransferTransactionDto responseDto = objectMapper.readValue(responseBody, InternalTransferTransactionDto.class);

        assertEquals(responseDto.getStatus(), TransactionStatus.DECLINED);

        Account updatedSenderAccount = accountRepository.findByAccountNumber("111");
        Account updatedReceiverAccount = accountRepository.findByAccountNumber("222");

        assertEquals(updatedSenderAccount.getAvailableBalance(), 10000);
        assertEquals(updatedReceiverAccount.getAvailableBalance(), 10000L);

        Optional<TransferTransaction> transaction = transactionRepository.findById(Long.valueOf(responseDto.getId()));
        assertTrue(transaction.isPresent());
        assertEquals(transaction.get().getStatus(), TransactionStatus.DECLINED);
    }

    @Transactional
    @Test
    public void createExternalTransferTransaction_happyFlow() throws Exception {
        transactionSetup(10000L,
                10000L,
                "email1",
                "email2");

        ExternalTransferTransactionDto requestDto = new ExternalTransferTransactionDto();
        requestDto.setAmount(5000L);
        requestDto.setSenderAccountNumber("111");
        requestDto.setReceiverAccountNumber("222");

        ResultActions resultActions = mockMvc.perform(
                        post("http://localhost:8003/api/transaction/external")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();
        ExternalTransferTransactionDto responseDto = objectMapper.readValue(responseBody, ExternalTransferTransactionDto.class);

        assertEquals(responseDto.getStatus(), TransactionStatus.PENDING);

        Account updatedSenderAccount = accountRepository.findByAccountNumber("111");
        Account updatedReceiverAccount = accountRepository.findByAccountNumber("222");

        assertEquals(updatedSenderAccount.getAvailableBalance(), 10000);
        assertEquals(updatedReceiverAccount.getAvailableBalance(), 10000L);

        Optional<TransferTransaction> transaction = transactionRepository.findById(Long.valueOf(responseDto.getId()));
        assertTrue(transaction.isPresent());
        assertEquals(transaction.get().getStatus(), TransactionStatus.PENDING);
    }

    @Transactional
    @Test
    public void createExternalTransferTransaction_insufficientFunds() throws Exception {
        transactionSetup(1000L,
                10000L,
                "email1",
                "email2");

        ExternalTransferTransactionDto requestDto = new ExternalTransferTransactionDto();
        requestDto.setAmount(5000L);
        requestDto.setSenderAccountNumber("111");
        requestDto.setReceiverAccountNumber("222");

        ResultActions resultActions = mockMvc.perform(
                        post("http://localhost:8003/api/transaction/external")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();

        InternalTransferTransactionDto responseDto = objectMapper.readValue(responseBody, InternalTransferTransactionDto.class);

        assertEquals(responseDto.getStatus(), TransactionStatus.DECLINED);

        Account updatedSenderAccount = accountRepository.findByAccountNumber("111");
        Account updatedReceiverAccount = accountRepository.findByAccountNumber("222");

        assertEquals(updatedSenderAccount.getAvailableBalance(), 1000L);
        assertEquals(updatedReceiverAccount.getAvailableBalance(), 10000L);

        Optional<TransferTransaction> transaction = transactionRepository.findById(Long.valueOf(responseDto.getId()));
        assertTrue(transaction.isPresent());
        assertEquals(transaction.get().getStatus(), TransactionStatus.DECLINED);
    }

    @Transactional
    @Test
    public void verifyTransaction_validToken() throws Exception {
        verificationSetup();

        ResultActions resultActions = mockMvc.perform(
                        patch("http://localhost:8003/api/transaction/verify/"
                                + transactionId + "?verificationToken=11111")
                )
                .andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();

        TransactionStatus responseDto = objectMapper.readValue(responseBody, TransactionStatus.class);

        assertEquals(responseDto, TransactionStatus.CONFIRMED);

        Account updatedSenderAccount = accountRepository.findByAccountNumber("111");
        Account updatedReceiverAccount = accountRepository.findByAccountNumber("222");

        assertEquals(updatedSenderAccount.getAvailableBalance(), 5000L);
        assertEquals(updatedReceiverAccount.getAvailableBalance(), 15000L);

        Optional<TransferTransaction> transaction = transactionRepository.findById(transactionId);
        assertTrue(transaction.isPresent());
        assertEquals(transaction.get().getStatus(), TransactionStatus.CONFIRMED);
    }

    @Transactional
    @Test
    public void verifyTransaction_invalidToken() throws Exception {
        verificationSetup();

        ResultActions resultActions = mockMvc.perform(
                        patch("http://localhost:8003/api/transaction/verify/"
                                + transactionId + "?verificationToken=invalidToken")
                )
                .andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();

        TransactionStatus responseDto = objectMapper.readValue(responseBody, TransactionStatus.class);

        assertEquals(responseDto, TransactionStatus.DECLINED);

        Account updatedSenderAccount = accountRepository.findByAccountNumber("111");
        Account updatedReceiverAccount = accountRepository.findByAccountNumber("222");

        assertEquals(updatedSenderAccount.getAvailableBalance(), 10000L);
        assertEquals(updatedReceiverAccount.getAvailableBalance(), 10000L);

        Optional<TransferTransaction> transaction = transactionRepository.findById(transactionId);
        assertTrue(transaction.isPresent());
        assertEquals(transaction.get().getStatus(), TransactionStatus.DECLINED);
    }

    private void transactionSetup(Long senderBalance,
                                          Long receiverBalance,
                                          String senderEmail,
                                          String receiverEmail) {
        Account testSenderAccount = createTestAccount(
                "111",
                senderEmail,
                AccountType.DOMESTIC_CURRENCY_ACCOUNT,
                "USD",
                0.0,
                senderBalance);

        Account testReceiverAccount = createTestAccount(
                "222",
                receiverEmail,
                AccountType.DOMESTIC_CURRENCY_ACCOUNT,
                "USD",
                0.0,
                receiverBalance);

        accountRepository.saveAll(List.of(testSenderAccount, testReceiverAccount));
    }

    private Account createTestAccount(String accountNumber,
                                      String email,
                                      AccountType accountType,
                                      String currencyCode,
                                      Double maintenanceFee,
                                      Long balance) {
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setEmail(email);
        account.setAvailableBalance(balance);
        account.setAccountType(accountType);
        account.setCurrencyCode(currencyCode);
        account.setMaintenanceFee(maintenanceFee);
        account.setAvailableBalance(balance);
        account.setLinkState(UserAccountUserProfileLinkState.NOT_ASSOCIATED);
        account.setEmployeeId(1L);
        return account;
    }

    private void verificationSetup() {
        ExternalTransferTransaction transaction = new ExternalTransferTransaction();

        Account senderAccount = createTestAccount(
                "111",
                "email1",
                AccountType.DOMESTIC_CURRENCY_ACCOUNT,
                "USD",
                0.0,
                10000L);

        Account receiverAccount = createTestAccount(
                "222",
                "email2",
                AccountType.DOMESTIC_CURRENCY_ACCOUNT,
                "USD",
                0.0,
                10000L);

        accountRepository.saveAll(List.of(senderAccount, receiverAccount));

        transaction.setSenderAccount(senderAccount);
        transaction.setReceiverAccount(receiverAccount);
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setVerificationToken("11111");
        transaction.setAmount(5000L);

        transactionId = transactionRepository.save(transaction).getId();
    }
}
