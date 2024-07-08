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

public class FindOrderTransactionByIdIntegrationTest extends OrderTransactionIntegrationTestConfig {
    @Autowired
    private OrderTransactionRepository orderTransactionRepository;

    @Autowired
    private OrderTransactionService orderTransactionService;

    private OrderTransaction testOrderTransaction;

    private long testOrderTransactionId;

    private OrderTransaction foundOrderTransaction;

    @Transactional
    @After
    public void finish() {
        if (testOrderTransaction != null && testOrderTransaction.getId() != null) {
            orderTransactionRepository.delete(testOrderTransaction);
        }
    }

    @Given("there is an order transaction with a specific id")
    public void thereIsAnOrderTransactionWithId() {
        testOrderTransaction = new OrderTransaction();
        orderTransactionRepository.save(testOrderTransaction);
        testOrderTransactionId = testOrderTransaction.getId();
    }

    @When("I request order transaction with that id")
    public void iRequestOrderTransactionForId() {
        foundOrderTransaction = orderTransactionService.findById(testOrderTransactionId);
    }

    @Then("I should receive the order transaction with that id")
    public void iShouldReceiveTheResponseContainingOrderTransactionWithId() {
        assertEquals(testOrderTransaction, foundOrderTransaction);
    }
}
