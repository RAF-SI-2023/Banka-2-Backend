package rs.edu.raf.BankService.e2e.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import rs.edu.raf.BankService.data.dto.ExternalTransferTransactionDto;
import rs.edu.raf.BankService.data.dto.InternalTransferTransactionDto;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.data.entities.transactions.ExternalTransferTransaction;
import rs.edu.raf.BankService.data.entities.transactions.TransferTransaction;
import rs.edu.raf.BankService.data.enums.AccountType;
import rs.edu.raf.BankService.data.enums.TransactionStatus;
import rs.edu.raf.BankService.data.enums.UserAccountUserProfileLinkState;
import rs.edu.raf.BankService.repository.CashAccountRepository;
import rs.edu.raf.BankService.repository.CashTransactionRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        private CashAccountRepository cashAccountRepository;

        @Autowired
        private CashTransactionRepository cashTransactionRepository;

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
                                                .content(objectMapper.writeValueAsString(requestDto)))
                                .andExpect(status().isOk());

                MvcResult mvcResult = resultActions.andReturn();
                String responseBody = mvcResult.getResponse().getContentAsString();
                InternalTransferTransactionDto responseDto = objectMapper.readValue(responseBody,
                                InternalTransferTransactionDto.class);

                assertEquals(responseDto.getStatus(), TransactionStatus.CONFIRMED);

                CashAccount updatedSenderCashAccount = cashAccountRepository.findByAccountNumber("111");
                CashAccount updatedReceiverCashAccount = cashAccountRepository.findByAccountNumber("222");

                assertEquals(updatedSenderCashAccount.getAvailableBalance(), 9000L);
                assertEquals(updatedReceiverCashAccount.getAvailableBalance(), 11000L);

                Optional<TransferTransaction> transaction = cashTransactionRepository
                                .findById(Long.valueOf(responseDto.getId()));
                assertTrue(transaction.isPresent());
                assertEquals(transaction.get().getStatus(), TransactionStatus.CONFIRMED);
        }

        @Value("${MY_EMAIL_1:lukapavlovic032@gmail.com}")
        private String myEmail1;

        @Transactional
        @Test
        public void checkTransactionHistoryByEmail_Success() throws Exception {
                String email = myEmail1;
                ResultActions resultActions = mockMvc.perform(
                                get("http://localhost:8003/api/transaction/funds-transfer-by-email/" + email)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());

                MvcResult mvcResult = resultActions.andReturn();
                MockHttpServletResponse response = mvcResult.getResponse();
                assertEquals(MockHttpServletResponse.SC_OK, response.getStatus());
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
                                                .content(objectMapper.writeValueAsString(requestDto)))
                                .andExpect(status().isOk());

                MvcResult mvcResult = resultActions.andReturn();
                String responseBody = mvcResult.getResponse().getContentAsString();
                InternalTransferTransactionDto responseDto = objectMapper.readValue(responseBody,
                                InternalTransferTransactionDto.class);

                assertEquals(responseDto.getStatus(), TransactionStatus.DECLINED);

                CashAccount updatedSenderCashAccount = cashAccountRepository.findByAccountNumber("111");
                CashAccount updatedReceiverCashAccount = cashAccountRepository.findByAccountNumber("222");

                assertEquals(updatedSenderCashAccount.getAvailableBalance(), 10000);
                assertEquals(updatedReceiverCashAccount.getAvailableBalance(), 10000L);

                Optional<TransferTransaction> transaction = cashTransactionRepository
                                .findById(Long.valueOf(responseDto.getId()));
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
                                                .content(objectMapper.writeValueAsString(requestDto)))
                                .andExpect(status().isOk());

                MvcResult mvcResult = resultActions.andReturn();
                String responseBody = mvcResult.getResponse().getContentAsString();
                ExternalTransferTransactionDto responseDto = objectMapper.readValue(responseBody,
                                ExternalTransferTransactionDto.class);

                assertEquals(responseDto.getStatus(), TransactionStatus.PENDING);

                CashAccount updatedSenderCashAccount = cashAccountRepository.findByAccountNumber("111");
                CashAccount updatedReceiverCashAccount = cashAccountRepository.findByAccountNumber("222");

                assertEquals(updatedSenderCashAccount.getAvailableBalance(), 10000);
                assertEquals(updatedReceiverCashAccount.getAvailableBalance(), 10000L);

                Optional<TransferTransaction> transaction = cashTransactionRepository
                                .findById(Long.valueOf(responseDto.getId()));
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
                                                .content(objectMapper.writeValueAsString(requestDto)))
                                .andExpect(status().isOk());

                MvcResult mvcResult = resultActions.andReturn();
                String responseBody = mvcResult.getResponse().getContentAsString();

                InternalTransferTransactionDto responseDto = objectMapper.readValue(responseBody,
                                InternalTransferTransactionDto.class);

                assertEquals(responseDto.getStatus(), TransactionStatus.DECLINED);

                CashAccount updatedSenderCashAccount = cashAccountRepository.findByAccountNumber("111");
                CashAccount updatedReceiverCashAccount = cashAccountRepository.findByAccountNumber("222");

                assertEquals(updatedSenderCashAccount.getAvailableBalance(), 1000L);
                assertEquals(updatedReceiverCashAccount.getAvailableBalance(), 10000L);

                Optional<TransferTransaction> transaction = cashTransactionRepository
                                .findById(Long.valueOf(responseDto.getId()));
                assertTrue(transaction.isPresent());
                assertEquals(transaction.get().getStatus(), TransactionStatus.DECLINED);
        }

        @Transactional
        @Test
        public void verifyTransaction_validToken() throws Exception {
                verificationSetup();

                ResultActions resultActions = mockMvc.perform(
                                patch("http://localhost:8003/api/transaction/verify/"
                                                + transactionId + "?verificationToken=11111"))
                                .andExpect(status().isOk());

                MvcResult mvcResult = resultActions.andReturn();
                String responseBody = mvcResult.getResponse().getContentAsString();

                TransactionStatus responseDto = objectMapper.readValue(responseBody, TransactionStatus.class);

                assertEquals(responseDto, TransactionStatus.CONFIRMED);

                CashAccount updatedSenderCashAccount = cashAccountRepository.findByAccountNumber("111");
                CashAccount updatedReceiverCashAccount = cashAccountRepository.findByAccountNumber("222");

                assertEquals(updatedSenderCashAccount.getAvailableBalance(), 5000L);
                assertEquals(updatedReceiverCashAccount.getAvailableBalance(), 15000L);

                Optional<TransferTransaction> transaction = cashTransactionRepository.findById(transactionId);
                assertTrue(transaction.isPresent());
                assertEquals(transaction.get().getStatus(), TransactionStatus.CONFIRMED);
        }

        @Transactional
        @Test
        public void verifyTransaction_invalidToken() throws Exception {
                verificationSetup();

                ResultActions resultActions = mockMvc.perform(
                                patch("http://localhost:8003/api/transaction/verify/"
                                                + transactionId + "?verificationToken=invalidToken"))
                                .andExpect(status().isOk());

                MvcResult mvcResult = resultActions.andReturn();
                String responseBody = mvcResult.getResponse().getContentAsString();

                TransactionStatus responseDto = objectMapper.readValue(responseBody, TransactionStatus.class);

                assertEquals(responseDto, TransactionStatus.DECLINED);

                CashAccount updatedSenderCashAccount = cashAccountRepository.findByAccountNumber("111");
                CashAccount updatedReceiverCashAccount = cashAccountRepository.findByAccountNumber("222");

                assertEquals(updatedSenderCashAccount.getAvailableBalance(), 10000L);
                assertEquals(updatedReceiverCashAccount.getAvailableBalance(), 10000L);

                Optional<TransferTransaction> transaction = cashTransactionRepository.findById(transactionId);
                assertTrue(transaction.isPresent());
                assertEquals(transaction.get().getStatus(), TransactionStatus.DECLINED);
        }

        private void transactionSetup(Long senderBalance,
                        Long receiverBalance,
                        String senderEmail,
                        String receiverEmail) {
                CashAccount testSenderCashAccount = createTestAccount(
                                "111",
                                senderEmail,
                                AccountType.DOMESTIC_CURRENCY_ACCOUNT,
                                "USD",
                                0.0,
                                senderBalance);

                CashAccount testReceiverCashAccount = createTestAccount(
                                "222",
                                receiverEmail,
                                AccountType.DOMESTIC_CURRENCY_ACCOUNT,
                                "USD",
                                0.0,
                                receiverBalance);

                cashAccountRepository.saveAll(List.of(testSenderCashAccount, testReceiverCashAccount));
        }

        private CashAccount createTestAccount(String accountNumber,
                        String email,
                        AccountType accountType,
                        String currencyCode,
                        Double maintenanceFee,
                        Long balance) {
                CashAccount cashAccount = new CashAccount();
                cashAccount.setAccountNumber(accountNumber);
                cashAccount.setEmail(email);
                cashAccount.setAvailableBalance(balance);
                cashAccount.setAccountType(accountType);
                cashAccount.setCurrencyCode(currencyCode);
                cashAccount.setMaintenanceFee(maintenanceFee);
                cashAccount.setAvailableBalance(balance);
                cashAccount.setLinkState(UserAccountUserProfileLinkState.NOT_ASSOCIATED);
                cashAccount.setEmployeeId(1L);
                return cashAccount;
        }

        private void verificationSetup() {
                ExternalTransferTransaction transaction = new ExternalTransferTransaction();

                CashAccount senderCashAccount = createTestAccount(
                                "111",
                                "email1",
                                AccountType.DOMESTIC_CURRENCY_ACCOUNT,
                                "USD",
                                0.0,
                                10000L);

                CashAccount receiverCashAccount = createTestAccount(
                                "222",
                                "email2",
                                AccountType.DOMESTIC_CURRENCY_ACCOUNT,
                                "USD",
                                0.0,
                                10000L);

                cashAccountRepository.saveAll(List.of(senderCashAccount, receiverCashAccount));

                transaction.setSenderCashAccount(senderCashAccount);
                transaction.setReceiverCashAccount(receiverCashAccount);
                transaction.setStatus(TransactionStatus.PENDING);
                transaction.setVerificationToken("11111");
                transaction.setAmount(5000L);

                transactionId = cashTransactionRepository.save(transaction).getId();
        }
}
