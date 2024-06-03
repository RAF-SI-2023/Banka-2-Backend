package rs.edu.raf.BankService.e2e.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import rs.edu.raf.BankService.data.dto.AccountNumberDto;
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
    private String accountNumber;
    @Autowired
    private ObjectMapper objectMapper;


    @Given("they are giving account number {string}")
    public void initAccountNuberAndCheckAuth(String s){
        this.accountNumber = s;
        accountControllerJwtConst.jwt = JwtTokenGenerator.generateToken(9L, "dummyAdminUser@gmail.com", "ADMIN", "");
    }

    @When("they send request for account informations to get")
    public void sendingForAccountInformations() throws Exception {
       AccountNumberDto accountNumberDto = new AccountNumberDto(this.accountNumber);

        ResultActions resultActions = mockMvc.perform(get(BASE_URL+"/cashe-account-state")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accountControllerJwtConst.jwt)
                .content(objectMapper.writeValueAsString(accountNumberDto))
        ).andExpect(status().isOk());

        MvcResult mvcResult = resultActions.andReturn();
        responseEntity = mvcResult.getResponse();

    }

    @Then("response is back with status ok and account informations")
    public void shouldReturnStatusOkWithAccountInformations(){
        assertEquals(MockHttpServletResponse.SC_OK, responseEntity.getStatus());
    }


    @Given("they are giving, account number {string}")
    public void initAccountNuberAndCheckAuthNotFound(String s){
        this.accountNumber = s;
        accountControllerJwtConst.jwt = JwtTokenGenerator.generateToken(9L, "dummyAdminUser@gmail.com", "ADMIN", "");
    }

    @When("they send request for account informations")
    public void sendingForAccountInformationsNotFound() throws Exception {
        AccountNumberDto accountNumberDto = new AccountNumberDto();

        ResultActions resultActions = mockMvc.perform(get(BASE_URL+"/cashe-account-state")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accountControllerJwtConst.jwt)
                .content(objectMapper.writeValueAsString(accountNumberDto))
        ).andExpect(status().is5xxServerError());

        MvcResult mvcResult = resultActions.andReturn();
        responseEntityFail = mvcResult.getResponse();
    }

    @Then("response is back with error message")
    public void shouldReturnStatusNotFound(){
        assertEquals(MockHttpServletResponse.SC_INTERNAL_SERVER_ERROR, responseEntityFail.getStatus());
    }


}
