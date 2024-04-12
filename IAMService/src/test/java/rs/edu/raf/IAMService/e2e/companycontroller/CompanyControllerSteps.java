package rs.edu.raf.IAMService.e2e.companycontroller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import rs.edu.raf.IAMService.data.dto.CompanyDto;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CompanyControllerSteps extends CompanyControllerTestsConfig {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private MvcResult mvcResult;
    /*
    @When("user requests all companies")
    public void userRequestsAllCompanies() {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get("/api/companies/all")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
            ).andExpect(status().isOk());
            mvcResult = resultActions.andReturn();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Then("the response should contain all companies")
    public void theResponseShouldContainAllCompanies() {
        assertEquals(200, mvcResult.getResponse().getStatus());
    }


    @When("user requests company with id {string}")
    public void userRequestsCompanyWithId(String arg0) {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get("/api/companies/id/" + arg0)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
            ).andExpect(status().isOk());
            mvcResult = resultActions.andReturn();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Then("response contains company with id {string}")
    public void responseContainsCompanyWithId(String arg0) {
        try {
            Long id = Long.parseLong(arg0);
            String responseAsString = mvcResult.getResponse().getContentAsString();
            CompanyDto companyDto = objectMapper.readValue(responseAsString, CompanyDto.class);
            assertEquals(id, companyDto.getId());
        } catch (UnsupportedEncodingException | JsonProcessingException e) {
            fail(e.getMessage());
        }
    }

    @When("user requests company with non-existing id {string}")
    public void userRequestsCompanyWithNonExistingId(String arg0) {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get("/api/companies/id/" + arg0)
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

     */
}
