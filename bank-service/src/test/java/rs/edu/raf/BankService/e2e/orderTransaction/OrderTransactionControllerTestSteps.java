package rs.edu.raf.BankService.e2e.orderTransaction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import rs.edu.raf.BankService.data.entities.transactions.OrderTransaction;
import rs.edu.raf.BankService.e2e.generators.JwtTokenGenerator;
import rs.edu.raf.BankService.repository.OrderTransactionRepository;
import rs.edu.raf.BankService.service.OrderTransactionService;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderTransactionControllerTestSteps extends OrderTransactionControllerConfigTests {
    private final String URL = "http://localhost:8003/api/order-transactions";

    @Value("${MY_EMAIL_1:istosic10921rn@raf.rs}")
    private String myEmail1;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderTransactionRepository orderTransactionRepository;
    @Autowired
    private OrderTransactionService orderTransactionService;
    @Autowired
    private OrderTransactionControllerStateTest jwtHolder;
    private MockHttpServletResponse response;

    private OrderTransaction testOrderTransaction;
    private long testOrderTransactionId;

    @Transactional
    @After
    public void finish() {
        if (testOrderTransaction != null && testOrderTransaction.getId() != null) {
            orderTransactionRepository.delete(testOrderTransaction);
        }
    }

    @Given("I am logged in")
    public void userIsLoggedIn() {
        jwtHolder.jwt = JwtTokenGenerator.generateToken(1L, myEmail1, "ROLE_USER", "");
    }

    @And("one order transaction exists with a specific id")
    public void oneOrderTransactionExists() {
        testOrderTransaction = new OrderTransaction();
        orderTransactionRepository.save(testOrderTransaction);
        testOrderTransactionId = testOrderTransaction.getId();
    }

    @When("I visit the order transactions page")
    public void iVisitTheOrderTransactionsPage() {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get(URL)
                            .header("Authorization", "Bearer " + jwtHolder.jwt)
            ).andExpect(status().isOk());
            MvcResult mvcResult = resultActions.andReturn();
            response = mvcResult.getResponse();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Then("I receive a success response")
    public void iReceiveASuccessResponse() {
        assertEquals(200, response.getStatus());
    }

    @And("I receive a list of order transactions")
    public void iShouldSeeAListOfOrderTransactions() {
        List<OrderTransaction> responseList = null;
        try {
            responseList = objectMapper.
                    readValue(response.getContentAsString(),
                            new TypeReference<List<OrderTransaction>>() {
                            });
        } catch (JsonProcessingException e) {
            fail(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            fail(e.getMessage());
        }
        assertEquals(testOrderTransaction, responseList.get(0));
        assertEquals(1, responseList.size());
    }

    @When("I visit the order transaction page with that id")
    public void iVisitTheOrderTransactionPageWithId() {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get(URL + "/id/" + testOrderTransactionId)
                            .header("Authorization", "Bearer " + jwtHolder.jwt)
            ).andExpect(status().isOk());
            MvcResult mvcResult = resultActions.andReturn();
            response = mvcResult.getResponse();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @And("I should see the order transaction with that id")
    public void iShouldSeeTheOrderTransactionWithId() {
        OrderTransaction responseOrderTransaction = null;
        try {
            responseOrderTransaction = objectMapper.
                    readValue(response.getContentAsString(),
                            new TypeReference<OrderTransaction>() {
                            });
        } catch (JsonProcessingException e) {
            fail(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            fail(e.getMessage());
        }
        assertEquals(testOrderTransaction, responseOrderTransaction);
    }

    @When("I visit the order transaction page with orderId {long}")
    public void iVisitTheOrderTransactionPageWithOrderId(long orderId) {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get(URL + "/order-id/" + orderId)
                            .header("Authorization", "Bearer " + jwtHolder.jwt)
            ).andExpect(status().isOk());
            MvcResult mvcResult = resultActions.andReturn();
            response = mvcResult.getResponse();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @And("I should see the order transaction with orderId {long}")
    public void iShouldSeeTheOrderTransactionWithOrderId(long orderId) {
        OrderTransaction responseOrderTransaction = null;
        try {
            responseOrderTransaction = objectMapper.
                    readValue(response.getContentAsString(),
                            new TypeReference<OrderTransaction>() {
                            });
        } catch (JsonProcessingException e) {
            fail(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            fail(e.getMessage());
        }

        assertEquals(orderId, responseOrderTransaction.getOrderId());
    }

    @And("one order transaction exists with orderId {long}")
    public void oneOrderTransactionExistsWithOrderId(long orderId) {
        testOrderTransaction = new OrderTransaction();
        testOrderTransaction.setOrderId(orderId);
        orderTransactionRepository.save(testOrderTransaction);
        testOrderTransactionId = testOrderTransaction.getId();
    }

    @And("one order transaction exists that belongs to an account with account number {string}")
    public void oneOrderTransactionExistsThatBelongsToAnAccountWithAccountNumber(String accountNumber) {
        testOrderTransaction = new OrderTransaction();
        testOrderTransaction.setAccountNumber(accountNumber);
        orderTransactionRepository.save(testOrderTransaction);
        testOrderTransactionId = testOrderTransaction.getId();
    }

    @When("I visit the order transaction page with account number {string}")
    public void iVisitTheOrderTransactionPageWithAccountNumber(String accountNumber) {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get(URL + "/account-number/" + accountNumber)
                            .header("Authorization", "Bearer " + jwtHolder.jwt)
            ).andExpect(status().isOk());
            MvcResult mvcResult = resultActions.andReturn();
            response = mvcResult.getResponse();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @And("I should see the order transaction with account number {string}")
    public void iShouldSeeTheOrderTransactionWithAccountNumber(String accountNumber) {
        List<OrderTransaction> responseList = null;
        try {
            responseList = objectMapper.
                    readValue(response.getContentAsString(),
                            new TypeReference<List<OrderTransaction>>() {
                            });
        } catch (JsonProcessingException e) {
            fail(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            fail(e.getMessage());
        }
        assertEquals(accountNumber, responseList.get(0).getAccountNumber());
        assertEquals(1, responseList.size());
    }
}
