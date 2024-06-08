package rs.edu.raf.BankService.integration.orderTransaction;

import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import rs.edu.raf.BankService.data.entities.transactions.OrderTransaction;
import rs.edu.raf.BankService.repository.OrderTransactionRepository;
import rs.edu.raf.BankService.service.OrderTransactionService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FindAllOrderTransactionsByAccountNumberIntegrationTest extends OrderTransactionIntegrationTestConfig{
    @Autowired
    private OrderTransactionRepository orderTransactionRepository;

    @Autowired
    private OrderTransactionService orderTransactionService;

    private OrderTransaction testOrderTransaction;
    private List<OrderTransaction> foundOrderTransactions;

    @Transactional
    @After
    public void finish() {
        if (testOrderTransaction != null && testOrderTransaction.getId() != null) {
            orderTransactionRepository.delete(testOrderTransaction);
        }
    }

    @Given("the account with number {string} has one order transaction")
    public void thereIsOrderTransactions(String accountNumber) {
        testOrderTransaction=new OrderTransaction();
        testOrderTransaction.setAccountNumber(accountNumber);
        orderTransactionRepository.save(testOrderTransaction);
    }

    @When("the user requests to list all order transactions by account number {string}")
    public void iRequestAllOrderTransactions(String accountNumber) {
        foundOrderTransactions=orderTransactionService.findAllByAccountNumber(accountNumber);
    }

    @Then("the response should contain one order transaction")
    public void theResponseShouldContainOneOrderTransaction() {
        assertEquals(testOrderTransaction, foundOrderTransactions.get(0));
    }
}
