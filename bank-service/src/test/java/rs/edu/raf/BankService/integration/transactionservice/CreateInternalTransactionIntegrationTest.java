package rs.edu.raf.BankService.integration.transactionservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import rs.edu.raf.BankService.data.dto.InternalTransferTransactionDto;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.data.enums.AccountType;
import rs.edu.raf.BankService.data.enums.TransactionStatus;
import rs.edu.raf.BankService.data.enums.UserAccountUserProfileLinkState;
import rs.edu.raf.BankService.e2e.account.AccountControllerJwtConst;
import rs.edu.raf.BankService.repository.CashAccountRepository;
import rs.edu.raf.BankService.repository.CashTransactionRepository;
import rs.edu.raf.BankService.service.TransactionService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CreateInternalTransactionIntegrationTest extends TransactionServiceIntegrationTestConfig {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CashAccountRepository cashAccountRepository;

    @Autowired
    private CashTransactionRepository cashTransactionRepository;

    private CashAccount testSenderCashAccount;
    private CashAccount testReceiverCashAccount;
    private TransactionStatus status;
    private Long transactionId;

    @Autowired
    private MockMvc mockMvc;
    private MockHttpServletResponse responseEntity;
    private String accountNumber;
    private long amount;
    @Autowired
    private ObjectMapper objectMapper;

    @Transactional
    @After
    public void finish() {
        if (transactionId != null) {
            cashTransactionRepository.deleteById(transactionId);
        }
        if (testSenderCashAccount != null && testSenderCashAccount.getId() != null) {
            cashAccountRepository.delete(testSenderCashAccount);
        }
        if (testReceiverCashAccount != null && testReceiverCashAccount.getId() != null) {
            cashAccountRepository.delete(testReceiverCashAccount);
        }
    }
    @Given("a sender account with number {string} with a balance of {long} for internal transaction - happyFlow")
    public void an_account_with_number_with_a_balance_of(String accountNumber, Long balance) {
        testSenderCashAccount = createTestAccount(accountNumber,
                "sender@example.com",
                AccountType.DOMESTIC_CURRENCY_ACCOUNT,
                "USD",
                0.0 ,
                balance);

        cashAccountRepository.save(testSenderCashAccount);
    }

    @Given("a receiver account with number {string} with a balance of {long} for internal transaction - happyFlow")
    public void a_receiver_account_with_number_with_a_balance_of(String accountNumber, Long balance) {
        testReceiverCashAccount = createTestAccount(accountNumber,
                "sender@example.com",
                AccountType.DOMESTIC_CURRENCY_ACCOUNT,
                "USD",
                0.0 ,
                balance);

        cashAccountRepository.save(testReceiverCashAccount);
    }

    @When("I request an internal transfer of {long} from {string} to {string} - happyFlow")
    public void i_request_an_internal_transfer_of_from_to(Long amount, String senderAccountNumber, String receiverAccountNumber) {
        InternalTransferTransactionDto transactionDto = new InternalTransferTransactionDto();
        transactionDto.setAmount(amount);
        transactionDto.setSenderAccountNumber(senderAccountNumber);
        transactionDto.setReceiverAccountNumber(receiverAccountNumber);
        InternalTransferTransactionDto result = transactionService.createInternalTransferTransaction(transactionDto);
        transactionId = Long.parseLong(result.getId());
        status = result.getStatus();
    }

    @Then("the internal transfer should be successful")
    public void the_transfer_should_be_successful() {
        assertEquals(TransactionStatus.CONFIRMED, status);
    }

    @Then("the sender's new balance should be {long} after internal transaction - happyFlow")
    public void the_sender_s_new_balance_should_be(double expectedBalance) {
        CashAccount senderCashAccount = cashAccountRepository.findByAccountNumber(testSenderCashAccount.getAccountNumber());
        assertEquals(expectedBalance, senderCashAccount.getAvailableBalance());
    }

    @Then("the receiver's new balance should be {long} after internal transaction - happyFlow")
    public void the_receiver_s_new_balance_should_be(double expectedBalance) {
        CashAccount receiverCashAccount = cashAccountRepository.findByAccountNumber(testReceiverCashAccount.getAccountNumber());
        assertEquals(expectedBalance, receiverCashAccount.getAvailableBalance());
    }

    @Given("a sender account with number {string} with a balance of {long} for internal transaction - insufficientFunds")
    public void a_sender_account_with_number_with_a_balance_of_insufficientFunds(String accountNumber, Long balance) {
        testSenderCashAccount = createTestAccount(accountNumber, "sender@example.com", AccountType.DOMESTIC_CURRENCY_ACCOUNT, "USD", 0.0, balance);
        cashAccountRepository.save(testSenderCashAccount);
    }

    @Given("a receiver account with number {string} with a balance of {long} for internal transaction - insufficientFunds")
    public void a_receiver_account_with_number_with_a_balance_of_insufficientFunds(String accountNumber, Long balance) {
        testReceiverCashAccount = createTestAccount(accountNumber, "sender@example.com", AccountType.DOMESTIC_CURRENCY_ACCOUNT, "USD", 0.0, balance);
        cashAccountRepository.save(testReceiverCashAccount);
    }

    @When("I request an internal transfer of {long} from {string} to {string} - insufficientFunds")
    public void i_request_an_internal_transfer_of_from_to_insufficientFunds(Long amount, String senderAccountNumber, String receiverAccountNumber) {
        InternalTransferTransactionDto transactionDto = new InternalTransferTransactionDto();
        transactionDto.setAmount(amount);
        transactionDto.setSenderAccountNumber(senderAccountNumber);
        transactionDto.setReceiverAccountNumber(receiverAccountNumber);
        InternalTransferTransactionDto result = transactionService.createInternalTransferTransaction(transactionDto);
        transactionId = Long.valueOf(result.getId());
        status = result.getStatus();
    }

    @Then("the internal transfer should be declined")
    public void the_transfer_should_be_declined() {
        assertEquals(TransactionStatus.DECLINED, status);
    }

    @Then("the sender's new balance should be {long} after internal transaction - insufficientFunds")
    public void the_sender_s_new_balance_should_be_insufficientFunds(double expectedBalance) {
        CashAccount senderCashAccount = cashAccountRepository.findByAccountNumber(testSenderCashAccount.getAccountNumber());
        assertEquals(expectedBalance, senderCashAccount.getAvailableBalance());
    }

    @Then("the receiver's new balance should be {long} after internal transaction - insufficientFunds")
    public void the_receiver_s_new_balance_should_be_insufficientFunds(double expectedBalance) {
        CashAccount receiverCashAccount = cashAccountRepository.findByAccountNumber(testReceiverCashAccount.getAccountNumber());
        assertEquals(expectedBalance, receiverCashAccount.getAvailableBalance());
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


    InternalTransferTransactionDto internalTransferTransactionDto = new InternalTransferTransactionDto();
    @Given("account number {string} for user and amount {string} thats taken")
    public void initAccountNumberAndAmount(String s1, String s2){
        accountNumber = s1;
        amount = Long.parseLong(s2);
        internalTransferTransactionDto.setSenderAccountNumber(accountNumber);
        internalTransferTransactionDto.setAmount(amount);
    }

    @SneakyThrows
    @When("request is send for changing user money balance")
    public void changingUserBalanceAndSavingTransaction(){

        ResultActions resultActions = mockMvc.perform(post("http://localhost:8003/api/transaction/deposit-withdrawal")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(internalTransferTransactionDto))
        ).andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();
        responseEntity = mvcResult.getResponse();
    }

    @Then("response is back with status ok")
    public void shoudGetResponseWithStatusOK(){
        assertEquals(MockHttpServletResponse.SC_OK, responseEntity.getStatus());
    }


}
