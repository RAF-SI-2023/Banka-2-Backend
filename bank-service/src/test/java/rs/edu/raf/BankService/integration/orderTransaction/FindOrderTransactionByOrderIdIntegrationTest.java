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

public class FindOrderTransactionByOrderIdIntegrationTest extends OrderTransactionIntegrationTestConfig {
    @Autowired
    private OrderTransactionRepository orderTransactionRepository;

    @Autowired
    private OrderTransactionService orderTransactionService;

    private OrderTransaction testOrderTransaction;
    private OrderTransaction foundOrderTransaction;

    @Transactional
    @After
    public void finish() {
        if (testOrderTransaction != null && testOrderTransaction.getId() != null) {
            orderTransactionRepository.delete(testOrderTransaction);
        }
    }

    @Given("there is an order transaction with an order id {long}")
    public void thereIsAnOrderTransactionWithAnOrderId(long orderId) {
        testOrderTransaction = new OrderTransaction();
        testOrderTransaction.setOrderId(orderId);
        orderTransactionRepository.save(testOrderTransaction);
    }

    @When("I request order transaction with order id {long}")
    public void iRequestOrderTransactionWithOrderId(long orderId) {
        foundOrderTransaction = orderTransactionService.findByOrderId(orderId);
    }

    @Then("I should receive the order transaction with order id {long}")
    public void iShouldReceiveTheOrderTransactionWithOrderId(long orderId) {
        assertEquals(testOrderTransaction, foundOrderTransaction);
    }
}
