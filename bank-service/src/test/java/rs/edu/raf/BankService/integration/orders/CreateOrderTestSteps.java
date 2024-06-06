package rs.edu.raf.BankService.integration.orders;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import rs.edu.raf.BankService.data.dto.ListingDto;
import rs.edu.raf.BankService.data.dto.OrderDto;
import rs.edu.raf.BankService.data.entities.Order;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.data.enums.ListingType;
import rs.edu.raf.BankService.data.enums.OrderActionType;
import rs.edu.raf.BankService.data.enums.OrderStatus;
import rs.edu.raf.BankService.mapper.OrderMapper;
import rs.edu.raf.BankService.repository.CashAccountRepository;
import rs.edu.raf.BankService.repository.OrderRepository;
import rs.edu.raf.BankService.service.CurrencyExchangeService;
import rs.edu.raf.BankService.service.OrderService;
import rs.edu.raf.BankService.service.StockService;
import rs.edu.raf.BankService.springSecurityUtil.SpringSecurityUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

public class CreateOrderTestSteps extends OrderServiceIntegrationTestConfig {
    boolean res;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CashAccountRepository cashAccountRepository;
    @Autowired
    private StockService stockService;
    @Autowired
    private CurrencyExchangeService currencyExchangeService;
    @Autowired
    private OrderMapper orderMapper;
    private Exception exception;
    private OrderDto orderDto;
    private Order createdOrder;
    private CashAccount cashAccount;
    private ListingDto listingDto;
    private Order mappedOrder;
    private MockedStatic<SpringSecurityUtil> mockedStatic;

    @After
    public void cleanUp() {
        // Clean up the created order and cash account
        if (res) {
            orderRepository.delete(mappedOrder);
        }
        if (cashAccount != null) {
            cashAccountRepository.delete(cashAccount);
        }
        mockedStatic.close();
    }

    @Before
    public void before() {
        MockitoAnnotations.openMocks(this);
        mockedStatic = mockStatic(SpringSecurityUtil.class);
        mockedStatic.when(SpringSecurityUtil::isAgent).thenReturn(false);
        mockedStatic.when(SpringSecurityUtil::isSupervisor).thenReturn(false);
        mockedStatic.when(SpringSecurityUtil::getPrincipalEmail).thenReturn("test@test.rs");
    }

    @Given("non existent cash account")
    public void nonExistentCashAccount() {
        orderDto = new OrderDto();
        orderDto.setListingType(ListingType.STOCK);
        orderDto.setOrderActionType(OrderActionType.BUY);
        orderDto.setOrderStatus(OrderStatus.APPROVED);
        ;
        mappedOrder = orderMapper.orderDtoToOrder(orderDto);
        mappedOrder.setListingSymbol("GOOG");
        mappedOrder.setQuantity(5);

        CashAccount cashAccount1 = new CashAccount();
        cashAccount1.setEmail(SpringSecurityUtil.getPrincipalEmail());
    }


    @When("user attempts to create order")
    public void userAttemptsToCreateOrder() {
        try {
            res = orderService.createOrder(orderDto);
        } catch (Exception e) {
            exception = e;
        }
    }


    @Then("service should throw an exception")
    public void serviceShouldThrowAnException() {
        assertEquals(exception.getMessage(), "Primary trading account not found");
    }

}
