package rs.edu.raf.BankService.e2e.orders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import rs.edu.raf.BankService.data.dto.ListingDto;
import rs.edu.raf.BankService.data.dto.OrderDto;
import rs.edu.raf.BankService.data.entities.Order;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.data.enums.ListingType;
import rs.edu.raf.BankService.data.enums.OrderActionType;
import rs.edu.raf.BankService.data.enums.OrderStatus;
import rs.edu.raf.BankService.e2e.generators.JwtTokenGenerator;
import rs.edu.raf.BankService.mapper.OrderMapper;
import rs.edu.raf.BankService.repository.CashAccountRepository;
import rs.edu.raf.BankService.repository.OrderRepository;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderControllerTestSteps extends OrderControllerConfigTests {
    private final String URL = "http://localhost:8003/api/orders/";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private OrderControllerStateTests userControllerTestsState;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CashAccountRepository cashAccountRepository;
    @Autowired
    private OrderMapper orderMapper;
    private MockHttpServletResponse httpServletResponse;

    private Order order;
    private OrderDto createOrderDto;
    private Order createOrder;
    private CashAccount createCashAccount;
    private ListingDto listingDto;

    @After
    public void clean() {
        if (order != null) {
            orderRepository.delete(order);
        }
    }

    @And("order exist in the repository")
    public void orderExistInTheRepository() {
        order = new Order();
        order.setOrderStatus(OrderStatus.APPROVED);
        order.setQuantity(1);
        order = orderRepository.save(order);
    }

    @When("user calls reject order endpoint")
    public void userCallsRejectOrderEndpoint() {
        try {
            ResultActions resultActions = mockMvc.perform(
                    put(URL + "reject/" + order.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + userControllerTestsState.jwt)
            ).andExpect(status().isOk());
            MvcResult mvcResult = resultActions.andReturn();
            httpServletResponse = mvcResult.getResponse();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @And("it should return true")
    public void itShouldReturnTrue() {
        try {
            boolean response = objectMapper.readValue(httpServletResponse.getContentAsString(), Boolean.class);
            assertTrue(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @And("status should be DENIED")
    public void statusShouldBeDENIED() {
        Order o = orderRepository.findById(order.getId()).get();
        assertTrue(o.getOrderStatus().equals(OrderStatus.DENIED));
    }

    @Given("user is logged in")
    public void userIsLoggedIn() {
        userControllerTestsState.jwt = JwtTokenGenerator.generateToken(1L, "pnesic6219rn@raf.rs", "EMPLOYEE", "");
    }

    @When("user calls find order by id endpoint {string}")
    public void userCallsFindOrderByIdEndpoint(String id) {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get(URL + "/id/" + id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + userControllerTestsState.jwt)

            ).andExpect(status().isOk());
            MvcResult mvcResult = resultActions.andReturn();
            httpServletResponse = mvcResult.getResponse();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Then("it should return success response")
    public void itShouldReturnSuccessResponse() {
        assertEquals(MockHttpServletResponse.SC_OK, httpServletResponse.getStatus());
    }

    @When("user calls get denied orders endpoint")
    public void userCallsGetDeniedOrdersEndpoint() {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get(URL + "denied")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + userControllerTestsState.jwt)
            ).andExpect(status().isOk());
            MvcResult mvcResult = resultActions.andReturn();
            httpServletResponse = mvcResult.getResponse();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Then("it should return a list of denied orders")
    public void itShouldReturnAListOfDeniedOrders() {
        try {
            List<OrderDto> deniedOrders = objectMapper.readValue(httpServletResponse.getContentAsString(), new TypeReference<List<OrderDto>>() {
            });
            assertNotNull(deniedOrders);
            if (!deniedOrders.isEmpty()) {
                for (OrderDto orderDto : deniedOrders) {
                    if (!orderDto.getOrderStatus().equals(OrderStatus.DENIED)) {
                        fail(orderDto.getOrderStatus().toString());
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Then("it should return a list of approved orders")
    public void itShouldReturnAListOfApprovedOrders() {
        try {
            List<OrderDto> approvedOrders = objectMapper.readValue(httpServletResponse.getContentAsString(), new TypeReference<List<OrderDto>>() {
            });
            assertNotNull(approvedOrders);

            if (!approvedOrders.isEmpty()) {
                for (OrderDto orderDto : approvedOrders) {
                    if (!orderDto.getOrderStatus().equals(OrderStatus.APPROVED)) {
                        fail(orderDto.getOrderStatus().toString());
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @When("user calls get approved orders endpoint")
    public void userCallsGetApprovedOrdersEndpoint() {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get(URL + "approved")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + userControllerTestsState.jwt)
            ).andExpect(status().isOk());
            MvcResult mvcResult = resultActions.andReturn();
            httpServletResponse = mvcResult.getResponse();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @When("user calls get non approved orders endpoint")
    public void userCallsGetNonApprovedOrdersEndpoint() {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get(URL + "non-approved")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + userControllerTestsState.jwt)
            ).andExpect(status().isOk());
            MvcResult mvcResult = resultActions.andReturn();
            httpServletResponse = mvcResult.getResponse();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Then("it should return a list of non approved orders")
    public void itShouldReturnAListOfNonApprovedOrders() {
        try {
            List<OrderDto> nonApprovedOrders = objectMapper.readValue(httpServletResponse.getContentAsString(), new TypeReference<List<OrderDto>>() {
            });
            assertNotNull(nonApprovedOrders);

            if (!nonApprovedOrders.isEmpty()) {
                for (OrderDto orderDto : nonApprovedOrders) {
                    if (orderDto.getOrderStatus().equals(OrderStatus.APPROVED)) {
                        fail(orderDto.getOrderStatus().toString());
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @When("user calls get all  orders endpoint")
    public void userCallsGetAllOrdersEndpoint() {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get("http://localhost:8003/api/orders")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + userControllerTestsState.jwt)
            ).andExpect(status().isOk());
            MvcResult mvcResult = resultActions.andReturn();
            httpServletResponse = mvcResult.getResponse();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @When("user calls approve order endpoint")
    public void userCallsApproveOrderEndpoint() {
        try {
            ResultActions resultActions = mockMvc.perform(
                    put(URL + "approve/" + order.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + userControllerTestsState.jwt)
            ).andExpect(status().isOk());
            MvcResult mvcResult = resultActions.andReturn();
            httpServletResponse = mvcResult.getResponse();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @And("status should be APPROVED")
    public void statusShouldBeAPPROVED() {
        Order o = orderRepository.findById(order.getId()).get();
        assertTrue(o.getOrderStatus().equals(OrderStatus.APPROVED));
    }


    private OrderDto prepareDto() {
        OrderDto createOrderDto = new OrderDto();
        createOrderDto.setOrderActionType(OrderActionType.BUY);
        createOrderDto.setListingType(ListingType.STOCK);
        createOrderDto.setQuantity(10);
        createOrderDto.setOrderStatus(OrderStatus.WAITING_FOR_APPROVAL);
        createOrderDto.setSecuritiesSymbol("NYSE");
        return createOrderDto;
    }


}
