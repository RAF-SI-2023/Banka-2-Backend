package rs.edu.raf.BankService.integration.orderTransaction;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import rs.edu.raf.BankService.data.entities.transactions.OrderTransaction;
import rs.edu.raf.BankService.repository.OrderTransactionRepository;
import rs.edu.raf.BankService.service.OrderTransactionService;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

public class FindAllOrderTransactionsIntegrationTest extends OrderTransactionIntegrationTestConfig{
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

    @Given("there is one order transaction")
    public void thereIsOrderTransactions() {
        testOrderTransaction=new OrderTransaction();
        orderTransactionRepository.save(testOrderTransaction);
    }

    @When("I request all order transactions")
    public void iRequestAllOrderTransactions() {
        foundOrderTransactions=orderTransactionService.findAll();
    }

    @Then("I should receive one order transaction")
    public void iShouldReceiveOrderTransaction() {
        assertEquals(1,foundOrderTransactions.size());
    }
}
