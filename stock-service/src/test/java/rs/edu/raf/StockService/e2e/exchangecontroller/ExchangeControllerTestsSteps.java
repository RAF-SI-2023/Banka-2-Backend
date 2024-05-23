package rs.edu.raf.StockService.e2e.exchangecontroller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import rs.edu.raf.StockService.data.entities.Exchange;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ExchangeControllerTestsSteps extends ExchangeControllerTestsConfig {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private MvcResult mvcResult;

    @When("user requests all exchanges")
    public void userRequestsAllExchanges() {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get("/api/exchange/all")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
            ).andExpect(status().isOk());
            mvcResult = resultActions.andReturn();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Then("the response should contain all exchanges")
    public void theResponseShouldContainAllExchanges() {
        assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @When("user requests exchange with id {string}")
    public void userRequestsExchangeWithId(String arg0) {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get("/api/exchange/id/" + arg0)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
            ).andExpect(status().isOk());
            mvcResult = resultActions.andReturn();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Then("response contains exchange with id {string}")
    public void responseContainsExchangeWithId(String arg0) {
        try {
            Long id = Long.parseLong(arg0);
            String responseAsString = mvcResult.getResponse().getContentAsString();
            Exchange exchange = objectMapper.readValue(responseAsString, Exchange.class);
            assertEquals(id, exchange.getId());
        } catch (UnsupportedEncodingException | JsonProcessingException e) {
            fail(e.getMessage());
        }
    }

    @When("user requests exchange with non-existing id {string}")
    public void userRequestsExchangeWithNonExistingId(String id) {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get("/api/exchange/id/" + id)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
            ).andExpect(status().isNotFound());
            mvcResult = resultActions.andReturn();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Then("response has status NotFound")
    public void responseHasStatusNotFound() {
        assertEquals(404, mvcResult.getResponse().getStatus());
    }

    @When("user requests exchange with existing name {string}")
    public void userRequestsExchangeWithExistingName(String exchangeName) {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get("/api/exchange/exchange-name/" + exchangeName)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
            ).andExpect(status().isOk());
            mvcResult = resultActions.andReturn();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Then("response contains only exchange with name {string}:")
    public void responseContainsOnlyExchangeWithName(String exchangeName) {
        try {
            String content = mvcResult.getResponse().getContentAsString();
            Exchange exchange = objectMapper.readValue(content, Exchange.class);

            assertEquals(exchange.getExchangeName(), exchangeName);
        } catch (UnsupportedEncodingException e) {
            fail(e.getMessage());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @When("user requests exchange with non-existing name {string}")
    public void userRequestsExchangeWithNonExistingName(String exchangeName) {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get("/api/exchange/exchange-name/" + exchangeName)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
            ).andExpect(status().isNotFound());
            mvcResult = resultActions.andReturn();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Then("response has status NotFound by name")
    public void responseHasStatusNotFoundByName() {
        assertEquals(404, mvcResult.getResponse().getStatus());
    }

    @When("user requests exchange with existing miCode {string}")
    public void userRequestsExchangeWithExistingMiCode(String miCode) {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get("/api/exchange/mi-code/" + miCode)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
            ).andExpect(status().isOk());
            mvcResult = resultActions.andReturn();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Then("response contains only exchange with miCode {string}:")
    public void responseContainsOnlyExchangemiCode(String miCode) {
        try {
            String content = mvcResult.getResponse().getContentAsString();
            Exchange exchange = objectMapper.readValue(content, Exchange.class);

            assertEquals(exchange.getExchangeMICode(), miCode);
        } catch (UnsupportedEncodingException e) {
            fail(e.getMessage());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @When("user requests exchange with non-existing miCode {string}")
    public void userRequestsExchangeWithNonExistingMiCode(String miCode) {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get("/api/exchange/mi-code/" + miCode)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
            ).andExpect(status().isNotFound());
            mvcResult = resultActions.andReturn();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Then("response has status NotFound by miCode")
    public void responseHasStatusNotFoundByMiCode() {
        assertEquals(404, mvcResult.getResponse().getStatus());
    }
}
