package rs.edu.raf.BankService.integration.transactionservice;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import rs.edu.raf.BankService.data.dto.GenericTransactionDto;
import rs.edu.raf.BankService.data.entities.accounts.Account;
import rs.edu.raf.BankService.data.entities.transactions.TransferTransaction;
import rs.edu.raf.BankService.repository.AccountRepository;
import rs.edu.raf.BankService.repository.TransactionRepository;
import rs.edu.raf.BankService.service.TransactionService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class GetTransferTransactionIntegrationTest {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    private Account testAccount;
    private TransferTransaction transaction;
    private Long testAccountId;
    private List<GenericTransactionDto> transactions;

    @Transactional
    @After
    public void finish() {
        if (testAccount != null) {
            accountRepository.delete(testAccount);
        }
        if (transaction != null) {
            transactionRepository.delete(transaction);
        }
    }

    @Given("an account with number {string} has made one transfer transaction")
    public void an_account_with_number_has_made_transfer_transactions(String accountNumber) {
        testAccount = new Account();
        testAccount.setAccountNumber(accountNumber);

        transaction = new TransferTransaction();
        transaction.setSenderAccount(testAccount);

        testAccount.setSentTransferTransactions(List.of(transaction));

        testAccount = accountRepository.save(testAccount);
        testAccountId = testAccount.getId();
        transactionRepository.save(transaction);
    }

    @Transactional
    @When("I retrieve the transfer transactions for account")
    public void i_retrieve_the_transfer_transactions_for_account() {
        transactions = transactionService.getTransferTransactions(testAccountId);
    }

    @Then("I should receive a list containing {int} transaction")
    public void i_should_receive_a_list_containing_transactions(Integer expectedTransactionCount) {
        assertEquals(expectedTransactionCount.intValue(), transactions.size());
    }
}
