package rs.edu.raf.BankService.e2e.securityOwnership;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import rs.edu.raf.BankService.data.dto.SecuritiesOwnershipDto;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.data.enums.ListingType;
import rs.edu.raf.BankService.e2e.card.CardControllerTestsConfig;
import rs.edu.raf.BankService.e2e.generators.JwtTokenGenerator;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SecurityOwnershipControllerSteps extends CardControllerTestsConfig {

    private final String BASE_URL = "http://localhost:8003/api/securities-ownerships/";

    @Value("${MY_EMAIL_1:lukapavlovic032@gmail.com}")
    private String myEmail1;

    private MockHttpServletResponse responseEntity;
    private MockHttpServletResponse responseEntityFail;

    @Autowired
    private SecurityOwnershipControllerJwtConst securityOwnershipControllerJwtConst;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Given("employee is logged in")
    public void userIsLoggedInAsEmployee() {
        securityOwnershipControllerJwtConst.jwt = JwtTokenGenerator.generateToken(1L, "lazar@gmail.com", "EMPLOYEE",
                "PERMISSION_4");
    }

    @When("I visit the securities ownerships page")
    public void iVisitTheSecuritiesOwnershipsPage() {
        String jwtToken = securityOwnershipControllerJwtConst.jwt;
        try {
            ResultActions resultActions = mockMvc.perform(
                    get(BASE_URL + "account-number/3334444999999999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isOk());
            MvcResult mvcResult = resultActions.andReturn();
            responseEntity = mvcResult.getResponse();

            assertEquals(MockHttpServletResponse.SC_OK, responseEntity.getStatus());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @When("It should return a success response")
    public void itShouldReturnASuccessResponse() {
        assertEquals(200, responseEntity.getStatus());
    }

    @When("I visit the securities ownerships page with security")
    public void iVisitTheSecuritiesOwnershipsPageWithSecurity() {

        String jwtToken = securityOwnershipControllerJwtConst.jwt;
        try {
            ResultActions resultActions = mockMvc.perform(
                    get(BASE_URL + "security-name/Z")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isOk());
            MvcResult mvcResult = resultActions.andReturn();
            responseEntity = mvcResult.getResponse();

            assertEquals(MockHttpServletResponse.SC_OK, responseEntity.getStatus());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @When("I visit the securities ownerships page to find all aviable")
    public void iVisitTheSecuritiesOwnershipsPageToFindAllAviable() {

        String jwtToken = securityOwnershipControllerJwtConst.jwt;
        try {
            ResultActions resultActions = mockMvc.perform(
                    get(BASE_URL + "all-available")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isOk());
            MvcResult mvcResult = resultActions.andReturn();
            responseEntity = mvcResult.getResponse();

            assertEquals(MockHttpServletResponse.SC_OK, responseEntity.getStatus());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    // When I visit the securities ownerships page to get all publicily available
    // from companies

    @When("I visit the securities ownerships page to get all publicily available from companies")
    public void iVisitTheSecuritiesOwnershipsPageToGetAllFromCompanies() {

        String jwtToken = securityOwnershipControllerJwtConst.jwt;
        try {
            ResultActions resultActions = mockMvc.perform(
                    get(BASE_URL + "all-available-companies")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isOk());
            MvcResult mvcResult = resultActions.andReturn();
            responseEntity = mvcResult.getResponse();

            assertEquals(MockHttpServletResponse.SC_OK, responseEntity.getStatus());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @When("I visit the securities ownerships page to get all publicily available from private")
    public void iVisitTheSecuritiesOwnershipsPageToGetAllFromPrivate() {
        String jwtToken = securityOwnershipControllerJwtConst.jwt;
        try {
            ResultActions resultActions = mockMvc.perform(
                    get(BASE_URL + "all-available-private")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isOk());
            MvcResult mvcResult = resultActions.andReturn();
            responseEntity = mvcResult.getResponse();

            assertEquals(MockHttpServletResponse.SC_OK, responseEntity.getStatus());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    // Scenario: update publicily available quantity
    // Given employee is logged in
    // When update publicily available quantity
    // Then It should return a success response

    @When("update publicily available quantity")
    public void updatePublicilyAviableQuntatiy() {
        SecuritiesOwnershipDto inputDto = new SecuritiesOwnershipDto(/* Initialize with valid data */);
        inputDto.setId(1L); // Set a valid ID here
        inputDto.setQuantityOfPubliclyAvailable(10);
        inputDto.setQuantity(50);
        inputDto.setSecuritiesSymbol("AAPL");
        inputDto.setEmail(myEmail1);
        inputDto.setAccountNumber("3334444999999999");
        inputDto.setReservedQuantity(0);
        inputDto.setOwnedByBank(false);

        String jwtToken = securityOwnershipControllerJwtConst.jwt;
        try {
            ResultActions resultActions = mockMvc.perform(
                    put(BASE_URL + "update-publicly-available")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + jwtToken)
                            .content(objectMapper.writeValueAsString(inputDto)))
                    .andExpect(status().isOk());
            MvcResult mvcResult = resultActions.andReturn();
            responseEntity = mvcResult.getResponse();

            assertEquals(MockHttpServletResponse.SC_OK, responseEntity.getStatus());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @When("the user requests the values of securities for account number {string}")
    public void theUserRequestsTheValuesOfSecuritiesForAccountNumber(String accountNumber) {
        String jwtToken = securityOwnershipControllerJwtConst.jwt;
        try {
            ResultActions resultActions = mockMvc.perform(
                    get(BASE_URL + "securities-values/" + accountNumber)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isOk());
            MvcResult mvcResult = resultActions.andReturn();
            responseEntity = mvcResult.getResponse();

            assertEquals(MockHttpServletResponse.SC_OK, responseEntity.getStatus());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Then("the user should receive a response with status code {int}")
    public void theUserShouldReceiveAResponseWithStatusCode(int statusCode) {
        assertEquals(statusCode, responseEntity.getStatus());
    }

    @Then("the response should contain the values of securities")
    public void theResponseShouldContainTheValuesOfSecurities() {
        try {
            Map<ListingType, BigDecimal> values = objectMapper.readValue(responseEntity.getContentAsString(),
                    new TypeReference<Map<ListingType, BigDecimal>>() {
                    });
            // Add assertions here
        } catch (JsonProcessingException e) {
            fail(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
