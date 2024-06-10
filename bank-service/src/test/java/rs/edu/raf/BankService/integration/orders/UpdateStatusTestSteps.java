package rs.edu.raf.BankService.integration.orders;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import rs.edu.raf.BankService.data.entities.Order;
import rs.edu.raf.BankService.data.enums.OrderStatus;
import rs.edu.raf.BankService.exception.OrderNotFoundException;
import rs.edu.raf.BankService.repository.OrderRepository;
import rs.edu.raf.BankService.service.OrderService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdateStatusTestSteps extends OrderServiceIntegrationTestConfig {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

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
    @Given("an order exists with ID {string} and status {string}")
    public void anOrderExistsWithIDAndStatus(String arg0, String status) {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.valueOf(status));
        order.setQuantity(10);
        savedOrder = orderRepository.save(order);
        savedOrderId = savedOrder.getId();
    }

    @Transactional
    @When("the client requests to update the order status to {string}")
    public void theClientRequestsToUpdateTheOrderStatusTo(String newStatus) {
        try {
            orderService.updateOrderStatus(savedOrderId, OrderStatus.valueOf(newStatus));
        } catch (Exception e) {
            exception = e;
        }
    }

    @Transactional
    @Then("the order status should be updated to {string}")
    public void theOrderStatusShouldBeUpdatedTo(String status) {
        savedOrder = orderRepository.findById(savedOrderId).get();
        assertEquals(OrderStatus.valueOf(status), savedOrder.getOrderStatus());
    }

    @Given("no order exists with ID {string}")
    public void noOrderExistsWithID(String id) {
        savedOrderId = Long.parseLong(id);

    }

    @Then("an OrderNotFoundException should be thrown")
    public void anOrderNotFoundExceptionShouldBeThrown() {
        assertTrue(exception instanceof OrderNotFoundException, "Exception should be OrderNotFoundException");

    }

    @When("the client requests to update the order status to {string} for non-existent order")
    public void theClientRequestsToUpdateTheOrderStatusToForNonExistentOrder(String status) {
        try {
            orderService.updateOrderStatus(savedOrderId, OrderStatus.valueOf(status));
        } catch (Exception e) {
            exception = e;
        }
    }
}
