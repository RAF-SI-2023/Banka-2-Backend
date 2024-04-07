package rs.edu.raf.StockService.integration.forexcontroller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import rs.edu.raf.StockService.data.entities.Forex;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ForexControllerTestsSteps extends ForexControllerTestsConfig{
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private MvcResult mvcResult;

    @When("user requests all forex pairs")
    public void userRequestsAllForexPairs() {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get("/api/forex/all")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
            ).andExpect(status().isOk());
            mvcResult = resultActions.andReturn();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Then("the response should contain all forex pairs")
    public void theResponseShouldContainAllForexPairs() {
        assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @When("user requests forex pair with id {string}")
    public void userRequestsForexPairWithId(String id) {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get("/api/forex/id/" + id)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
            ).andExpect(status().isOk());
            mvcResult = resultActions.andReturn();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Then("response contains forex pair with id {string}")
    public void responseContainsForexPairWithId(String arg0) {
        try {
            Long id = Long.parseLong(arg0);
            String responseAsString = mvcResult.getResponse().getContentAsString();
            Forex forex = objectMapper.readValue(responseAsString, Forex.class);
            assertEquals(id, forex.getId());
        } catch (UnsupportedEncodingException | JsonProcessingException e) {
            fail(e.getMessage());
        }
    }

    @When("user requests forex pair with non-existing id {string}")
    public void userRequestsForexPairWithNonExistingId(String id) {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get("/api/forex/id/" + id)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
            ).andExpect(status().isOk());
            mvcResult = resultActions.andReturn();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Then("response is empty and status is ok")
    public void responseIsEmptyAndStatusIsOk() {
        try {
            String responseAsString = mvcResult.getResponse().getContentAsString();
            assertTrue(responseAsString.isEmpty());
            assertEquals(200, mvcResult.getResponse().getStatus());
        } catch (UnsupportedEncodingException e) {
            fail(e.getMessage());
        }
    }
}
