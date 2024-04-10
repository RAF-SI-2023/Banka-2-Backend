package rs.edu.raf.BankService.integration.transactionservice;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import rs.edu.raf.BankService.data.entities.accounts.Account;
import rs.edu.raf.BankService.data.entities.transactions.ExternalTransferTransaction;
import rs.edu.raf.BankService.data.enums.TransactionStatus;
import rs.edu.raf.BankService.repository.AccountRepository;
import rs.edu.raf.BankService.repository.TransactionRepository;
import rs.edu.raf.BankService.service.TransactionService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VerifyTransactionIntegrationTest extends TransactionServiceIntegrationTestConfig {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private Account testSenderAccount;
    private Account testReceiverAccount;
    private ExternalTransferTransaction transaction;
    private TransactionStatus status;
    private Long transactionId;

    @Transactional
    @After
    public void finish() {
        if (transactionId != null) {
            transactionRepository.deleteById(transactionId);
        }
        if (testSenderAccount != null && testSenderAccount.getId() != null) {
            accountRepository.delete(testSenderAccount);
        }
        if (testReceiverAccount != null && testReceiverAccount.getId() != null) {
            accountRepository.delete(testReceiverAccount);
        }
    }

    @Given("a pending external transfer transaction from {string} to {string} with token {string} and amount {string} - happyFlow")
    public void a_pending_external_transfer_transaction(String sender, String receiver, String token, String amount) {
        ExternalTransferTransaction transferTransaction = new ExternalTransferTransaction();

        testSenderAccount = new Account();
        testSenderAccount.setAvailableBalance(10000L);
        testSenderAccount.setAccountNumber(sender);

        testReceiverAccount = new Account();
        testReceiverAccount.setAvailableBalance(10000L);
        testReceiverAccount.setAccountNumber(receiver);

        accountRepository.saveAll(List.of(testReceiverAccount, testSenderAccount));

        transferTransaction.setSenderAccount(testSenderAccount);
        transferTransaction.setReceiverAccount(testReceiverAccount);
        transferTransaction.setAmount(Long.parseLong(amount));
        transferTransaction.setVerificationToken(token);

        transaction = transactionRepository.save(transferTransaction);
        status = transaction.getStatus();
        transactionId = transaction.getId();
    }

    @When("I verify the transaction with ID and token {string} - happyFlow")
    public void i_verify_the_transaction(String token) {
        status = transactionService.verifyTransaction(transactionId, token);
    }

    @Then("the transaction should be confirmed")
    public void the_transaction_should_be_confirmed() {
        assertEquals(TransactionStatus.CONFIRMED, status);
    }

    @Then("the sender's new balance should be {string} after verification - happyFlow")
    public void the_senders_new_balance_should_be(String expectedBalance) {
        refreshTestAccounts();
        assertEquals(Long.parseLong(expectedBalance), testSenderAccount.getAvailableBalance());
    }

    @Then("the receiver's new balance should be {string} after verification - happyFlow")
    public void the_receivers_new_balance_should_be(String expectedBalance) {
        refreshTestAccounts();
        assertEquals(Long.parseLong(expectedBalance), testReceiverAccount.getAvailableBalance());
    }

    @Given("a pending external transfer transaction from {string} to {string} with token {string} and amount {string} - invalidToken")
    public void a_pending_external_transfer_transaction_invalid_token(String sender, String receiver, String token, String amount) {
        ExternalTransferTransaction transferTransaction = new ExternalTransferTransaction();

        testSenderAccount = new Account();
        testSenderAccount.setAvailableBalance(10000L);
        testSenderAccount.setAccountNumber(sender);

        testReceiverAccount = new Account();
        testReceiverAccount.setAvailableBalance(10000L);
        testReceiverAccount.setAccountNumber(receiver);

        accountRepository.saveAll(List.of(testReceiverAccount, testSenderAccount));

        transferTransaction.setSenderAccount(testSenderAccount);
        transferTransaction.setReceiverAccount(testReceiverAccount);
        transferTransaction.setAmount(Long.parseLong(amount));
        transferTransaction.setVerificationToken(token);

        transaction = transactionRepository.save(transferTransaction);
        status = transaction.getStatus();
        transactionId = transaction.getId();
    }

    @When("I verify the transaction with ID and token {string} - invalidToken")
    public void i_verify_the_transaction_invalid_token(String invalidToken) {
        status = transactionService.verifyTransaction(transactionId, invalidToken);
    }

    @Then("the transaction should be declined")
    public void the_transaction_should_be_declined_invalid_token() {
        assertEquals(TransactionStatus.DECLINED, status);
    }

    @Then("the sender's new balance should be {string} after verification - invalidToken")
    public void the_senders_new_balance_should_be_invalid_token(String expectedBalance) {
        refreshTestAccounts();
        assertEquals(Long.parseLong(expectedBalance), testSenderAccount.getAvailableBalance());
    }

    @Then("the receiver's new balance should be {string} after verification - invalidToken")
    public void the_receivers_new_balance_should_be_invalid_token(String expectedBalance) {
        refreshTestAccounts();
        assertEquals(Long.parseLong(expectedBalance), testReceiverAccount.getAvailableBalance());
    }

    private void refreshTestAccounts() {
        testSenderAccount = accountRepository.findById(testSenderAccount.getId())
                .orElseThrow(() -> new AssertionError("Sender account not found"));
        testReceiverAccount = accountRepository.findById(testReceiverAccount.getId())
                .orElseThrow(() -> new AssertionError("Receiver account not found"));
    }
}
