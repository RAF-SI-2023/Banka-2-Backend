package rs.edu.raf.BankService.e2e.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.eo.Do;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import rs.edu.raf.BankService.data.dto.AccountNumberDto;
import rs.edu.raf.BankService.data.dto.DepositWithdrawalDto;
import rs.edu.raf.BankService.data.dto.MoneyStatusDto;
import rs.edu.raf.BankService.e2e.generators.JwtTokenGenerator;
import rs.edu.raf.BankService.mapper.AccountMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;



public class AccountControllerSteps extends AccountControllerTestConfig{

    private final String BASE_URL = "http://localhost:8003/api/accounts";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    AccountControllerJwtConst accountControllerJwtConst;
    private MockHttpServletResponse responseEntity;
    private MockHttpServletResponse responseEntityFail;
    private String currency;
    private double available;
    private double reserved;
    private double total;

    @Autowired
    private ObjectMapper objectMapper;

    @Given("they are giving currency {string} available balance {string} reserved funds {string} and total {string}")
    public void initAccountNuberAndCheckAuth(String s, String s1, String s2, String s3){
       this.currency = s; this.available = Double.parseDouble(s1); this.reserved = Double.parseDouble(s2); this.total = Double.parseDouble(s3);
       accountControllerJwtConst.jwt = JwtTokenGenerator.generateToken(9L, "dummyAdminUser@gmail.com", "ADMIN", "");
    }

    @When("they send request for accounts informations to get")
    public void sendingForAccountInformations() throws Exception {
        MoneyStatusDto moneyStatusDto = new MoneyStatusDto(currency, total, available, reserved);

        ResultActions resultActions = mockMvc.perform(get(BASE_URL+"/cashe-account-state")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accountControllerJwtConst.jwt)
                .content(objectMapper.writeValueAsString(moneyStatusDto))
        ).andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();
        responseEntity = mvcResult.getResponse();

    }

    @Then("response is back with status ok and accounts informations")
    public void shouldReturnStatusOkWithAccountInformations(){
        assertEquals(MockHttpServletResponse.SC_OK, responseEntity.getStatus());
    }

    private long amount;
    private String accountNum;

    @Given("account number {string} and amount {string}")
    public void initDepositWithdrawalAddition(String s1, String s2){
        accountNum = s1;
        amount = Long.parseLong(s2);
    }

    @SneakyThrows
    @When("request is send for changing user money balance")
    public  void  sendingDepositWithdrawalAddition(){
        DepositWithdrawalDto depositWithdrawalDto = new DepositWithdrawalDto(accountNum, amount);

        ResultActions resultActions = mockMvc.perform(post(BASE_URL+"/deposit-withdrawal/payment-addition")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(depositWithdrawalDto))
        ).andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();
        responseEntity = mvcResult.getResponse();
    }

    @Then("response is back with status ok")
    public  void shouldReturneStatusOk(){
        assertEquals(MockHttpServletResponse.SC_OK, responseEntity.getStatus());
    }

    @Given("data of account number {string} and amount {string}")
    public void initDepositWithdrawalAdditionNotFound(String s1, String s2){
        accountNum = s1;
        amount = Long.parseLong(s2);
    }

    @SneakyThrows
    @When("request is send for changing user money balance for user")
    public  void  sendingDepositWithdrawalAdditionNotFound(){
        DepositWithdrawalDto depositWithdrawalDto = new DepositWithdrawalDto(accountNum, amount);

        ResultActions resultActions = mockMvc.perform(post(BASE_URL+"/deposit-withdrawal/payment-addition")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(depositWithdrawalDto))
        ).andExpect(status().is5xxServerError());

        MvcResult mvcResult = resultActions.andReturn();
        responseEntity = mvcResult.getResponse();
    }

    @Then("response is back with exception not found")
    public  void shouldReturneStatusNotFound(){
        assertEquals(MockHttpServletResponse.SC_INTERNAL_SERVER_ERROR, responseEntity.getStatus());
    }
    ///////
    @Given("subtraction account number {string} and amount {string}")
    public void initDepositWithdrawalSubtraction(String s1, String s2){
        accountNum = s1;
        amount = Long.parseLong(s2);
    }

    @SneakyThrows
    @When("request is send for reducing user money balance")
    public  void  sendingDepositWithdrawalSubtraction(){
        DepositWithdrawalDto depositWithdrawalDto = new DepositWithdrawalDto(accountNum, amount);

        ResultActions resultActions = mockMvc.perform(post(BASE_URL+"/deposit-withdrawal/payment-addition")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(depositWithdrawalDto))
        ).andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();
        responseEntity = mvcResult.getResponse();
    }

    @Then("response is recieved back with status ok")
    public void shouldReturneStatusOkForSubtraction(){
        assertEquals(MockHttpServletResponse.SC_OK, responseEntity.getStatus());
    }
////////
    @Given("data of account number {string} and amount {string} for subtraction")
    public void initDepositWithdrawalSubtractionNotFound(String s1, String s2){
        accountNum = s1;
        amount = Long.parseLong(s2);
    }

    @SneakyThrows
    @When("request is send for reducing user money balance for user but not found")
    public  void  sendingDepositWithdrawalSubtractionNotFound(){
        DepositWithdrawalDto depositWithdrawalDto = new DepositWithdrawalDto(accountNum, amount);

        ResultActions resultActions = mockMvc.perform(post(BASE_URL+"/deposit-withdrawal/payment-subtraction")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(depositWithdrawalDto))
        ).andExpect(status().is5xxServerError());

        MvcResult mvcResult = resultActions.andReturn();
        responseEntity = mvcResult.getResponse();
    }

    @Then("response is back with exception error message")
    public  void shouldReturneStatusNotFoundForSubtraction(){
        assertEquals(MockHttpServletResponse.SC_INTERNAL_SERVER_ERROR, responseEntity.getStatus());
    }
//////////
    @Given("data of account number {string} and amount {string} for tihs subtraction")
    public void initDepositWithdrawalSubtractionToBigAmount(String s1, String s2){
        accountNum = s1;
        amount = Long.parseLong(s2);
    }

    @SneakyThrows
    @When("request is send for reducing user money balance for user but to much")
    public  void  sendingDepositWithdrawalSubtractionToBigAmount(){
        DepositWithdrawalDto depositWithdrawalDto = new DepositWithdrawalDto(accountNum, amount);

        ResultActions resultActions = mockMvc.perform(post(BASE_URL+"/deposit-withdrawal/payment-subtraction")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(depositWithdrawalDto))
        ).andExpect(status().is5xxServerError());

        MvcResult mvcResult = resultActions.andReturn();
        responseEntity = mvcResult.getResponse();
    }

    @Then("response is back with exception run time exception")
    public  void shouldReturneStatusNotFoundForToBigAmount(){
        assertEquals(MockHttpServletResponse.SC_INTERNAL_SERVER_ERROR, responseEntity.getStatus());
    }





}
