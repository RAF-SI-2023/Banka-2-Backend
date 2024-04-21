package rs.edu.raf.BankService.integration.transactionservice;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import rs.edu.raf.BankService.data.dto.InternalTransferTransactionDto;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.data.enums.AccountType;
import rs.edu.raf.BankService.data.enums.TransactionStatus;
import rs.edu.raf.BankService.data.enums.UserAccountUserProfileLinkState;
import rs.edu.raf.BankService.repository.AccountRepository;
import rs.edu.raf.BankService.repository.TransactionRepository;
import rs.edu.raf.BankService.service.TransactionService;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateInternalTransactionIntegrationTest extends TransactionServiceIntegrationTestConfig {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private CashAccount testSenderCashAccount;
    private CashAccount testReceiverCashAccount;
    private TransactionStatus status;
    private Long transactionId;

    @Transactional
    @After
    public void finish() {
        if (transactionId != null) {
            transactionRepository.deleteById(transactionId);
        }
        if (testSenderCashAccount != null && testSenderCashAccount.getId() != null) {
            accountRepository.delete(testSenderCashAccount);
        }
        if (testReceiverCashAccount != null && testReceiverCashAccount.getId() != null) {
            accountRepository.delete(testReceiverCashAccount);
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

        accountRepository.save(testSenderCashAccount);
    }

    @Given("a receiver account with number {string} with a balance of {long} for internal transaction - happyFlow")
    public void a_receiver_account_with_number_with_a_balance_of(String accountNumber, Long balance) {
        testReceiverCashAccount = createTestAccount(accountNumber,
                "sender@example.com",
                AccountType.DOMESTIC_CURRENCY_ACCOUNT,
                "USD",
                0.0 ,
                balance);

        accountRepository.save(testReceiverCashAccount);
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
    public void the_sender_s_new_balance_should_be(Long expectedBalance) {
        CashAccount senderCashAccount = accountRepository.findByAccountNumber(testSenderCashAccount.getAccountNumber());
        assertEquals(expectedBalance, senderCashAccount.getAvailableBalance());
    }

    @Then("the receiver's new balance should be {long} after internal transaction - happyFlow")
    public void the_receiver_s_new_balance_should_be(Long expectedBalance) {
        CashAccount receiverCashAccount = accountRepository.findByAccountNumber(testReceiverCashAccount.getAccountNumber());
        assertEquals(expectedBalance, receiverCashAccount.getAvailableBalance());
    }

    @Given("a sender account with number {string} with a balance of {long} for internal transaction - insufficientFunds")
    public void a_sender_account_with_number_with_a_balance_of_insufficientFunds(String accountNumber, Long balance) {
        testSenderCashAccount = createTestAccount(accountNumber, "sender@example.com", AccountType.DOMESTIC_CURRENCY_ACCOUNT, "USD", 0.0, balance);
        accountRepository.save(testSenderCashAccount);
    }

    @Given("a receiver account with number {string} with a balance of {long} for internal transaction - insufficientFunds")
    public void a_receiver_account_with_number_with_a_balance_of_insufficientFunds(String accountNumber, Long balance) {
        testReceiverCashAccount = createTestAccount(accountNumber, "sender@example.com", AccountType.DOMESTIC_CURRENCY_ACCOUNT, "USD", 0.0, balance);
        accountRepository.save(testReceiverCashAccount);
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
    public void the_sender_s_new_balance_should_be_insufficientFunds(Long expectedBalance) {
        CashAccount senderCashAccount = accountRepository.findByAccountNumber(testSenderCashAccount.getAccountNumber());
        assertEquals(expectedBalance, senderCashAccount.getAvailableBalance());
    }

    @Then("the receiver's new balance should be {long} after internal transaction - insufficientFunds")
    public void the_receiver_s_new_balance_should_be_insufficientFunds(Long expectedBalance) {
        CashAccount receiverCashAccount = accountRepository.findByAccountNumber(testReceiverCashAccount.getAccountNumber());
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
}
