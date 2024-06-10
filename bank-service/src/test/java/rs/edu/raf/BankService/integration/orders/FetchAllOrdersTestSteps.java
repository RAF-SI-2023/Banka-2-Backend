package rs.edu.raf.BankService.integration.orders;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import rs.edu.raf.BankService.data.dto.OrderDto;
import rs.edu.raf.BankService.data.entities.Order;
import rs.edu.raf.BankService.mapper.OrderMapper;
import rs.edu.raf.BankService.repository.OrderRepository;
import rs.edu.raf.BankService.service.OrderService;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class FetchAllOrdersTestSteps extends OrderServiceIntegrationTestConfig {

    Long count;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderMapper orderMapper;
    private Order order1;
    private Order order2;
    private List<OrderDto> orderDtos;

    @Given("orders exist in the repository")
    public void ordersExistInTheRepository() {
        count = orderRepository.count();
        assertTrue(count > 0);
    }

    @When("the user requests all orders")
    public void theUserRequestsAllOrders() {
        orderDtos = orderService.getAll();
    }

    @Then("the user should receive a list of all orders")
    public void theUserShouldReceiveAListOfAllOrders() {
        assertNotNull(orderDtos);
        assertEquals(orderDtos.size(), count);
    }
}
