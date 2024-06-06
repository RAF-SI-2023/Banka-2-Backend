package rs.edu.raf.BankService.integration.orders;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import rs.edu.raf.BankService.data.entities.Order;
import rs.edu.raf.BankService.exception.OrderNotFoundException;
import rs.edu.raf.BankService.repository.OrderRepository;
import rs.edu.raf.BankService.service.OrderService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FindOrderTestSteps extends OrderServiceIntegrationTestConfig {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    private Order foundOrder;
    private Order savedOrder;
    private Long savedOrderId;
    private Exception exception;

    @Transactional
    @After
    public void clean() {
        if (savedOrder != null)
            orderRepository.delete(savedOrder);
    }

    @Transactional
    @Given("the order exists in db")
    public void theOrderExistsInDb() {
        Order o = new Order();
        o.setQuantity(10);
        savedOrder = orderRepository.save(o);
        savedOrderId = savedOrder.getId();
    }

    @Transactional
    @When("the client requests to find the order")
    public void theClientRequestsToFindTheOrder() {
        try {
            foundOrder = orderService.findById(savedOrderId);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("the order details should be returned")
    public void theOrderDetailsShouldBeReturned() {
        assertEquals(foundOrder.getId(), savedOrderId);
    }

    @Then("the order should throw an exception")
    public void theOrderShouldThrowAnException() {
        assertTrue(exception instanceof OrderNotFoundException, "Order not found for this id " + savedOrderId);
    }

    @Given("the order is non existent with ID {string}")
    public void theOrderIsNonExistentWithID(String arg0) {
        savedOrderId = Long.parseLong(arg0);
    }
}
