package rs.edu.raf.BankService.e2e.card;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import rs.edu.raf.BankService.data.dto.CardDto;
import rs.edu.raf.BankService.data.dto.CreateCardDto;
import rs.edu.raf.BankService.data.entities.card.Card;
import rs.edu.raf.BankService.data.enums.CardType;
import rs.edu.raf.BankService.e2e.generators.JwtTokenGenerator;
import rs.edu.raf.BankService.mapper.CardMapper;
import rs.edu.raf.BankService.service.CardService;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CardControllerSteps extends CardControllerTestsConfig {

    // private final RestTemplate restTemplate = new RestTemplateBuilder().build();
    private final String BASE_URL = "http://localhost:8003/api/cards";

    @Autowired
    private MockMvc mockMvc;

    private CardMapper cardMapper;


    @Autowired
    private CardControllerJwtConst userControllerTestsState;
    private String authToken;
    private MockHttpServletResponse responseEntity;

    private MockHttpServletResponse responseEntityFail;

    @Autowired
    private ObjectMapper objectMapper;

    CardService cardService;

    CreateCardDto deleteCardDto;

    CardControllerSteps(CardService cardService, CardMapper cardMapper) {
        this.cardService = cardService;
        this.cardMapper = cardMapper;
    }

    @Given("employee is logged in")
    public void userIsLoggedInAsEmployee() {
        userControllerTestsState.jwt = JwtTokenGenerator.generateToken(1L, "lazar@gmail.com", "EMPLOYEE", "");
    }

    @When("the create card endpoint is called")
    public void theCreateCardEndpointIsCalled() {
        CreateCardDto cardDto = new CreateCardDto();
        cardDto.setAccountNumber("0004444999999999");
        cardDto.setLimitCard(100000L);
        cardDto.setCardType(CardType.CREDIT);

        String requestData;
        try {
            requestData = objectMapper.writeValueAsString(cardDto);
        } catch (JsonProcessingException e) {
            // Handle JSON serialization exception
            e.printStackTrace();
            requestData = ""; // Or any default value
        }

        String jwtToken = userControllerTestsState.jwt;
        try {
            ResultActions resultActions = mockMvc.perform(
                    post("http://localhost:8003/api/cards/create-card")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + jwtToken)
                            .content(objectMapper.writeValueAsString(cardDto))
            ).andExpect(status().isOk());
            MvcResult mvcResult = resultActions.andReturn();
            responseEntity = mvcResult.getResponse();

            CardDto card = objectMapper.readValue(responseEntity.getContentAsString(), CardDto.class);

            Card cards = cardMapper.cardDtoToCard(card);
            cardService.deleteCard(cards.getIdentificationCardNumber());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Then("it should return a success response with the created card details")
    public void itShouldReturnASuccessResponseWithTheCreatedCardDetails() {
        assertEquals(MockHttpServletResponse.SC_OK, responseEntity.getStatus());
    }

    @When("an invalid create card request without necessary data")
    public void anInvalidCreateCardRequestWithoutNecessaryData() {
        CreateCardDto cardDto = new CreateCardDto(/* without necessary data */);
        cardDto.setLimitCard(100000L);
        cardDto.setCardType(CardType.CREDIT);

        String jwtToken = userControllerTestsState.jwt;
        try {
            ResultActions resultActions = mockMvc.perform(
                    post("http://localhost:8003/api/cards/create-card")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + jwtToken)
                            .content(objectMapper.writeValueAsString(new CreateCardDto()))
            ).andExpect(status().isForbidden());
            MvcResult mvcResult = resultActions.andReturn();
            responseEntityFail = mvcResult.getResponse();

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    @Then("it should return a not found status with an error message")
    public void itShouldReturnAForbiddenStatusWithAnErrorMessage() {
        assertEquals(MockHttpServletResponse.SC_FORBIDDEN, responseEntityFail.getStatus());
    }

    private MockHttpServletResponse responseCardNumber;
    @When("users endpoint is called")
    public void usersEndpointIsCalled() {
        long identificationCardNumber = 1000000000000000L;
        try {
            ResultActions resultActions = mockMvc.perform(
                    get("http://localhost:8003/api/cards/id/"+identificationCardNumber)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + userControllerTestsState.jwt)
            ).andExpect(status().isOk());
            MvcResult mvcResult = resultActions.andReturn();
            responseCardNumber =mvcResult.getResponse();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    @Then("it should return a success response with the card details")
    public void itShouldReturnASuccessResponseWithTheCardDetails() {
        assertEquals(MockHttpServletResponse.SC_OK, responseCardNumber.getStatus());
    }

    @When("users endpoint put is called {string}")
    public void usersEndpointIsCalledForStatus(String url) {
        CardDto cardDto = new CardDto();
        cardDto.setIdentificationCardNumber(1000000000000000L);
        cardDto.setStatus(true);
        try {
            ResultActions resultActions = mockMvc.perform(
                    put(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + userControllerTestsState.jwt)
                            .content(objectMapper.writeValueAsString(cardDto))
            ).andExpect(status().isOk());
            MvcResult mvcResult = resultActions.andReturn();
            responseCardNumber =mvcResult.getResponse();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @When("users endpoint get is called {string}")
    public void usersEndpointIsCalledForAccountNumber(String url) {
        try {
            ResultActions resultActions = mockMvc.perform(
                    get(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + userControllerTestsState.jwt)
            ).andExpect(status().isOk());
            MvcResult mvcResult = resultActions.andReturn();
            responseCardNumber =mvcResult.getResponse();

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
//    When  users endpoint put is called "http://localhost:8003/api/cards/change-card-limit" and given body

    @When("users endpoint put is called {string} and given body")
    public void usersEndpointIsCalledForLimit(String url) {
        CardDto cardDto = new CardDto();
        cardDto.setIdentificationCardNumber(1000000000000000L);
        cardDto.setLimitCard(100000L);
        try {
            ResultActions resultActions = mockMvc.perform(
                    put(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + userControllerTestsState.jwt)
                            .content(objectMapper.writeValueAsString(cardDto))
            ).andExpect(status().isOk());
            MvcResult mvcResult = resultActions.andReturn();
            responseCardNumber =mvcResult.getResponse();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

}
