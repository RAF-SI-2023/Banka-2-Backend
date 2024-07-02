package rs.edu.raf.BankService.e2e.credit;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import rs.edu.raf.BankService.data.dto.CreditDto;
import rs.edu.raf.BankService.data.dto.CreditRequestDto;
import rs.edu.raf.BankService.data.enums.CreditType;
import rs.edu.raf.BankService.e2e.generators.JwtTokenGenerator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CreditControllerTestSteps extends CreditControllerConfigTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CreditControllerStateTests userControllerTestsState;

    Object response;

    @Given("user is logged in as employee;")
    public void userIsLoggedInAsEmployee() {
        userControllerTestsState.jwt = JwtTokenGenerator.generateToken(1L, "lazar@gmail.com", "EMPLOYEE", "");
    }

    @When("user creates new credit request;")
    @WithUserDetails("")
    public void userCreatesNewCreditRequest() {
        CreditRequestDto creditRequestDto = new CreditRequestDto();
        creditRequestDto.setAccountNumber("0932345666666666");
        creditRequestDto.setCurrency("RSD");
        creditRequestDto.setHousingStatus("OWNED");
        creditRequestDto.setCreditType(CreditType.GOTOVINSKI);
        creditRequestDto.setCreditAmount(100000.0);
        creditRequestDto.setPaymentPeriodMonths(12L);
        try {
            ResultActions resultActions = mockMvc.perform(
                    post("http://localhost:8003/api/credit/credit-requests/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + userControllerTestsState.jwt)
                            .content(objectMapper.writeValueAsString(creditRequestDto)));
            MvcResult mvcResult = resultActions.andReturn();
            response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CreditRequestDto.class);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Then("credit request is created; and we can see it in the list of credit requests;")
    public void creditRequestIsCreatedAndWeCanSeeItInTheListOfCreditRequests() {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get("http://localhost:8003/api/credit/credit-requests/all-pending")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + userControllerTestsState.jwt))
                    .andExpect(status().isOk());

            MvcResult mvcResult = resultActions.andReturn();

            List<CreditRequestDto> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                    new TypeReference<List<CreditRequestDto>>() {
                    });
            assertTrue(response.contains(this.response));

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Given("credit request is pending approval and is in the list of credit requests")
    public void creditRequestIsPendingApprovalAndIsInTheListOfCreditRequests() {
        CreditRequestDto creditRequestDto = new CreditRequestDto();
        creditRequestDto.setAccountNumber("0932345666666666");
        creditRequestDto.setCurrency("RSD");
        creditRequestDto.setHousingStatus("OWNED");
        creditRequestDto.setCreditType(CreditType.GOTOVINSKI);
        creditRequestDto.setCreditAmount(100000.0);
        creditRequestDto.setPaymentPeriodMonths(12L);
        try {
            ResultActions resultActions = mockMvc.perform(
                    post("http://localhost:8003/api/credit/credit-requests/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + userControllerTestsState.jwt)
                            .content(objectMapper.writeValueAsString(creditRequestDto)))
                    .andExpect(status().isOk());
            MvcResult mvcResult = resultActions.andReturn();
            response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CreditRequestDto.class);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @When("user approves credit request;")
    public void userApprovesCreditRequest() {
        try {
            ResultActions resultActions = mockMvc.perform(
                    post("http://localhost:8003/api/credit//credit-requests/approve-and-create/"
                            + ((CreditRequestDto) response).getId())

                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + userControllerTestsState.jwt)

            ).andExpect(status().isOk());
            MvcResult mvcResult = resultActions.andReturn();
            response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CreditDto.class);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Then("credit request is approved; and we can see credit in the list of approved credits for the client;")
    public void creditRequestIsApprovedAndWeCanSeeCreditInTheListOfApprovedCreditsForTheClient() {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get("http://localhost:8003/api/credit/all/account-number/"
                            + ((CreditDto) response).getAccountNumber())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + userControllerTestsState.jwt))
                    .andExpect(status().isOk());

            MvcResult mvcResult = resultActions.andReturn();

            List<CreditDto> response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                    new TypeReference<List<CreditDto>>() {
                    });
            assertTrue(response.contains(this.response));

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
