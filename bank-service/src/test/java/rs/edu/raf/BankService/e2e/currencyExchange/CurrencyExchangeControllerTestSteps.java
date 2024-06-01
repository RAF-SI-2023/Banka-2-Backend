package rs.edu.raf.BankService.e2e.currencyExchange;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import rs.edu.raf.BankService.data.dto.ExchangeRatesDto;
import rs.edu.raf.BankService.data.dto.ExchangeRequestDto;
import rs.edu.raf.BankService.data.entities.accounts.DomesticCurrencyCashAccount;
import rs.edu.raf.BankService.data.enums.AccountType;
import rs.edu.raf.BankService.data.enums.DomesticCurrencyAccountType;
import rs.edu.raf.BankService.e2e.generators.JwtTokenGenerator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CurrencyExchangeControllerTestSteps extends CurrencyExchangeControllerConfigTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CurrencyExchangeControllerStateTests currencyExchangeControllerStateTests;

    private MockHttpServletResponse responseEntity;
    DomesticCurrencyCashAccount domesticCurrencyAccount1;

    @Value("${MY_EMAIL_1:lukapavlovic032@gmail.com}")
    private String myEmail1;

    @Given("user is logged in as employee;")
    public void userIsLoggedInAsEmployee() {
        currencyExchangeControllerStateTests.jwt = JwtTokenGenerator.generateToken(1L, "lazar@gmail.com", "EMPLOYEE", "");
    }


//    When the getAllExchangeRates endpoint is called from  "3334444999999999" to account "3334444888888888" and from "RSD" to "USD" amount of "1000"


    @When("the getAllExchangeRates endpoint is called to get exchange rates for the currency {string}")
    public void theGetAllExchangeRatesEndpointIsCalled(String fromCurrency) {

        String jwtToken = currencyExchangeControllerStateTests.jwt;
        try {
            ResultActions resultActions = mockMvc.perform(
                    get("http://localhost:8003/api/currency-exchange/exchange-rate/from/" + fromCurrency)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + jwtToken)
            ).andExpect(status().isOk());
            MvcResult mvcResult = resultActions.andReturn();
            responseEntity = mvcResult.getResponse();


        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

    @Then("it should return a success response with the exchange rates for the currency {string}")
    public void itShouldReturnASuccessResponseWithTheExchangeRatesForTheCurrency(String fromCurrency) {

        String jwtToken = currencyExchangeControllerStateTests.jwt;
        try {
            ResultActions resultActions = mockMvc.perform(
                    get("http://localhost:8003/api/currency-exchange/exchange-rate/from/" + fromCurrency)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + jwtToken)
            ).andExpect(status().isOk());

            MvcResult mvcResult = resultActions.andReturn();

            List<ExchangeRatesDto> response = objectMapper.
                    readValue(mvcResult.getResponse().getContentAsString(),
                            new TypeReference<List<ExchangeRatesDto>>() {
                            });

            assertTrue(response.size() > 0);
            assertEquals(MockHttpServletResponse.SC_OK, responseEntity.getStatus());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }


    @Then("it should return empty response with the exchange rates for the currency {string}")
    public void itShouldFailToReturnAResponseWithTheExchangeRatesForTheCurrency(String fromCurrency) {

        String jwtToken = currencyExchangeControllerStateTests.jwt;
        try {
            ResultActions resultActions = mockMvc.perform(
                    get("http://localhost:8003/api/currency-exchange/exchange-rate/from/" + fromCurrency)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + jwtToken)
            ).andExpect(status().isOk());

            MvcResult mvcResult = resultActions.andReturn();

            List<ExchangeRatesDto> response = objectMapper.
                    readValue(mvcResult.getResponse().getContentAsString(),
                            new TypeReference<List<ExchangeRatesDto>>() {
                            });

            assertTrue(response.size() == 0);


        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

    @Given("a valid account number {string}")
    public void aValidAccountNumber(String accountNumber) {
        domesticCurrencyAccount1 = new DomesticCurrencyCashAccount();
        domesticCurrencyAccount1.setAccountNumber(accountNumber);
        domesticCurrencyAccount1.setEmail(myEmail1);
        domesticCurrencyAccount1.setAccountType(AccountType.DOMESTIC_CURRENCY_ACCOUNT);
        domesticCurrencyAccount1.setEmployeeId(2L);
        domesticCurrencyAccount1.setMaintenanceFee(220.00);
        domesticCurrencyAccount1.setCurrencyCode("RSD");
        domesticCurrencyAccount1.setAvailableBalance(100000L);
        domesticCurrencyAccount1.setDomesticCurrencyAccountType(DomesticCurrencyAccountType.RETIREMENT);
        domesticCurrencyAccount1.setInterestRate(2.5);


        // Implementation for validating the account number
    }

    @When("the putExchangeCurrency endpoint is called from account {string} to account {string} and from {string} to {string} amount of {long}")
    public void theGetAllExchangeRatesEndpointIsCalled(String fromAccount, String toAccount, String fromCurrency, String toCurrency, Long amount) {
        String jwtToken = currencyExchangeControllerStateTests.jwt;

        ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto();
        exchangeRequestDto.setFromAccount(fromAccount);
        exchangeRequestDto.setToAccount(toAccount);
        exchangeRequestDto.setAmount(amount);

        try {
            ResultActions resultActions = mockMvc.perform(
                    post("http://localhost:8003/api/currency-exchange/exchange-currency")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + jwtToken)
                            .content(objectMapper.writeValueAsString(exchangeRequestDto))
            ).andExpect(status().isOk());
            MvcResult mvcResult = resultActions.andReturn();
            responseEntity = mvcResult.getResponse();


        } catch (Exception e) {
            fail(e.getMessage());
        }
    }


    @Then("it should return a success response for account {string} to account {string} and from {string} to {string} amount of {long}")
    public void itShouldReturnASuccessResponseWithTheExchangeRatesForTheCurrency(String fromAccount, String toAccount, String fromCurrency, String toCurrency, Long amount) {

        ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto();
        exchangeRequestDto.setFromAccount(fromAccount);
        exchangeRequestDto.setToAccount(toAccount);

        exchangeRequestDto.setAmount(amount);

        String jwtToken = currencyExchangeControllerStateTests.jwt;
        try {
            ResultActions resultActions = mockMvc.perform(
                    post("http://localhost:8003/api/currency-exchange/exchange-currency")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + jwtToken)
                            .content(objectMapper.writeValueAsString(exchangeRequestDto))
            ).andExpect(status().isOk());

            MvcResult mvcResult = resultActions.andReturn();
            responseEntity = mvcResult.getResponse();


            assertEquals(MockHttpServletResponse.SC_OK, responseEntity.getStatus());

        } catch (Exception e) {
            fail(e.getMessage());
        }

    }


    @Then("it should fail to return a response for account {string} to account {string} and from {string} to {string} amount of {long}")
    public void itShouldReturnAFailedResponseWithTheExchangeRatesForTheCurrency(String fromAccount, String toAccount, String fromCurrency, String toCurrency, Long amount) {
        String jwtToken = currencyExchangeControllerStateTests.jwt;
        ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto();
        exchangeRequestDto.setFromAccount(fromAccount);
        exchangeRequestDto.setToAccount(toAccount);
        exchangeRequestDto.setAmount(amount);
        try {
            ResultActions resultActions = mockMvc.perform(
                    post("http://localhost:8003/api/currency-exchange/exchange-currency")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + jwtToken)
                            .content(objectMapper.writeValueAsString(exchangeRequestDto))
            ).andExpect(status().isBadRequest());
            MvcResult mvcResult = resultActions.andReturn();
            responseEntity = mvcResult.getResponse();

            assertEquals(MockHttpServletResponse.SC_BAD_REQUEST, responseEntity.getStatus());

        } catch (Exception e) {
            fail(e.getMessage());
        }

    }
}
