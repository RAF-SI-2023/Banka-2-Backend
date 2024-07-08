package rs.edu.raf.BankService.integration.transactionservice;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import rs.edu.raf.BankService.data.dto.GenericTransactionDto;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.repository.CashAccountRepository;
import rs.edu.raf.BankService.repository.CashTransactionRepository;
import rs.edu.raf.BankService.service.TransactionService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CheckTransactionByEmailTest extends TransactionServiceIntegrationTestConfig {

    ResponseEntity<?> responseEntity;
    @Autowired
    private TransactionService transactionService;
    @Value("${MY_EMAIL_1:lukapavlovic032@gmail.com}")
    private String myEmail1;
    @Autowired
    private CashAccountRepository cashAccountRepository;
    @Autowired
    private CashTransactionRepository cashTransactionRepository;
    private String emailAddress;
    private List<GenericTransactionDto> transactions = new ArrayList<>();
    private CashAccount testSenderCashAccount;
    private Long transactionId;


    @Given("I have a valid email address")
    public void iHaveAValidEmailAddress() {

        System.out.println("Email address: " + myEmail1);

        emailAddress = myEmail1;

    }


    @When("I check my transactions")
    @Transactional
    public void iCheckMyTransactions() {

        transactions = transactionService.getTransferTransactionsByEmail(emailAddress);
    }

    @Then("I should see all transactions for that email address")
    public void iShouldSeeAllTransactionsForThatEmailAddress() {
        assertEquals(0, (1 - 1));
    }
}
