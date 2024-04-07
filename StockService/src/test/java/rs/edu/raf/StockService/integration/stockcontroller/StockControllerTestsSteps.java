package rs.edu.raf.StockService.integration.stockcontroller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import rs.edu.raf.StockService.data.entities.Stock;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StockControllerTestsSteps extends StockControllerTestsConfig{
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private MvcResult mvcResult;
    @When("user requests all stocks")
    public void userRequestsAllStocks() {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get("/api/stock/all")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
            ).andExpect(status().isOk());
            mvcResult = resultActions.andReturn();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Then("the response should contain all stocks")
    public void theResponseShouldContainAllStocks() {
        assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @When("user requests stock with id {string}")
    public void userRequestsStockWithId(String arg0) {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get("/api/stock/id/" + arg0)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
            ).andExpect(status().isOk());
            mvcResult = resultActions.andReturn();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Then("response contains stock with id {string}")
    public void responseContainsStockWithId(String arg0) {
        try {
            Long id = Long.parseLong(arg0);
            String responseAsString = mvcResult.getResponse().getContentAsString();
            Stock stock = objectMapper.readValue(responseAsString, Stock.class);
            assertEquals(id, stock.getId());
        } catch (UnsupportedEncodingException | JsonProcessingException e) {
            fail(e.getMessage());
        }
    }

    @When("user requests stock with non-existing id {string}")
    public void userRequestsStockWithNonExistingId(String id) {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get("/api/stock/id/" + id)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
            ).andExpect(status().isOk());
            mvcResult = resultActions.andReturn();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Then("response is empty with status ok")
    public void responseIsEmptyWithStatusOk() {
        try {
            String responseAsString = mvcResult.getResponse().getContentAsString();
            assertTrue(responseAsString.isEmpty());
            assertEquals(200, mvcResult.getResponse().getStatus());
        } catch (UnsupportedEncodingException e) {
            fail(e.getMessage());
        }
    }
    @When("user requests stock with non-existing symbol {string}")
    public void userRequestsStockWithNonExistingSymbol(String symbol) {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get("/api/stock/stockSymbol/" + symbol)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
            ).andExpect(status().isOk());
            mvcResult = resultActions.andReturn();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
