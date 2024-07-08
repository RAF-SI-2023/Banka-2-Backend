package rs.edu.raf.BankService.e2e.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import rs.edu.raf.BankService.data.dto.DepositWithdrawalDto;
import rs.edu.raf.BankService.data.dto.AccountValuesDto;
import rs.edu.raf.BankService.e2e.generators.JwtTokenGenerator;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class AccountControllerSteps extends AccountControllerTestConfig {

    private final String BASE_URL = "http://localhost:8003/api/accounts";
    @Autowired
    AccountControllerJwtConst accountControllerJwtConst;
    @Autowired
    private MockMvc mockMvc;
    private MockHttpServletResponse responseEntity;

    @Autowired
    private ObjectMapper objectMapper;
    private long amount;
    private String accountNum;

    @Given("no paramethers to give")
    public void initAccountNuberAndCheckAuth() {
        accountControllerJwtConst.jwt = JwtTokenGenerator.generateToken(9L, "dummyAdminUser@gmail.com", "ADMIN", "");
    }

    @When("they send request for accounts informations to get account paramethers")
    public void sendingForAccountInformations() throws Exception {

        ResultActions resultActions = mockMvc.perform(get(BASE_URL + "/cash-account-state")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accountControllerJwtConst.jwt)
        ).andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();
        responseEntity = mvcResult.getResponse();

    }

    @Then("response is back with status ok and accounts informations")
    public void shouldReturnStatusOkWithAccountInformations() {
        assertEquals(MockHttpServletResponse.SC_OK, responseEntity.getStatus());
    }

    @Given("account number {string} and amount {string}")
    public void initDepositWithdrawalAddition(String s1, String s2) {
        accountNum = s1;
        amount = Long.parseLong(s2);
    }

    @SneakyThrows
    @When("request is send for changing user money balance")
    public void sendingDepositWithdrawalAddition() {
        DepositWithdrawalDto depositWithdrawalDto = new DepositWithdrawalDto(accountNum, amount);

        ResultActions resultActions = mockMvc.perform(post(BASE_URL + "/deposit-withdrawal/payment-addition")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(depositWithdrawalDto))
        ).andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();
        responseEntity = mvcResult.getResponse();
    }

    @Then("response is back with status ok")
    public void shouldReturneStatusOk() {
        assertEquals(MockHttpServletResponse.SC_OK, responseEntity.getStatus());
    }

    @Given("data of account number {string} and amount {string}")
    public void initDepositWithdrawalAdditionNotFound(String s1, String s2) {
        accountNum = s1;
        amount = Long.parseLong(s2);
    }

    @SneakyThrows
    @When("request is send for changing user money balance for user")
    public void sendingDepositWithdrawalAdditionNotFound() {
        DepositWithdrawalDto depositWithdrawalDto = new DepositWithdrawalDto(accountNum, amount);

        ResultActions resultActions = mockMvc.perform(post(BASE_URL + "/deposit-withdrawal/payment-addition")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(depositWithdrawalDto))
        ).andExpect(status().is5xxServerError());

        MvcResult mvcResult = resultActions.andReturn();
        responseEntity = mvcResult.getResponse();
    }

    @Then("response is back with exception not found")
    public void shouldReturneStatusNotFound() {
        assertEquals(MockHttpServletResponse.SC_INTERNAL_SERVER_ERROR, responseEntity.getStatus());
    }

    ///////
    @Given("subtraction account number {string} and amount {string}")
    public void initDepositWithdrawalSubtraction(String s1, String s2) {
        accountNum = s1;
        amount = Long.parseLong(s2);
    }

    @SneakyThrows
    @When("request is send for reducing user money balance")
    public void sendingDepositWithdrawalSubtraction() {
        DepositWithdrawalDto depositWithdrawalDto = new DepositWithdrawalDto(accountNum, amount);

        ResultActions resultActions = mockMvc.perform(post(BASE_URL + "/deposit-withdrawal/payment-addition")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(depositWithdrawalDto))
        ).andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();
        responseEntity = mvcResult.getResponse();
    }

    @Then("response is recieved back with status ok")
    public void shouldReturneStatusOkForSubtraction() {
        assertEquals(MockHttpServletResponse.SC_OK, responseEntity.getStatus());
    }

    ////////
    @Given("data of account number {string} and amount {string} for subtraction")
    public void initDepositWithdrawalSubtractionNotFound(String s1, String s2) {
        accountNum = s1;
        amount = Long.parseLong(s2);
    }

    @SneakyThrows
    @When("request is send for reducing user money balance for user but not found")
    public void sendingDepositWithdrawalSubtractionNotFound() {
        DepositWithdrawalDto depositWithdrawalDto = new DepositWithdrawalDto(accountNum, amount);

        ResultActions resultActions = mockMvc.perform(post(BASE_URL + "/deposit-withdrawal/payment-subtraction")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(depositWithdrawalDto))
        ).andExpect(status().is5xxServerError());

        MvcResult mvcResult = resultActions.andReturn();
        responseEntity = mvcResult.getResponse();
    }

    @Then("response is back with exception error message")
    public void shouldReturneStatusNotFoundForSubtraction() {
        assertEquals(MockHttpServletResponse.SC_INTERNAL_SERVER_ERROR, responseEntity.getStatus());
    }

    @Given("data of account number {string} and amount {string} for tihs subtraction")
    public void initDepositWithdrawalSubtractionToBigAmount(String s1, String s2) {
        accountNum = s1;
        amount = Long.parseLong(s2);
    }

    @SneakyThrows
    @When("request is send for reducing user money balance for user but to much")
    public void sendingDepositWithdrawalSubtractionToBigAmount() {
        DepositWithdrawalDto depositWithdrawalDto = new DepositWithdrawalDto(accountNum, amount);

        ResultActions resultActions = mockMvc.perform(post(BASE_URL + "/deposit-withdrawal/payment-subtraction")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(depositWithdrawalDto))
        ).andExpect(status().is5xxServerError());

        MvcResult mvcResult = resultActions.andReturn();
        responseEntity = mvcResult.getResponse();
    }

    @Then("response is back with exception run time exception")
    public void shouldReturneStatusNotFoundForToBigAmount() {
        assertEquals(MockHttpServletResponse.SC_INTERNAL_SERVER_ERROR, responseEntity.getStatus());
    }


}
