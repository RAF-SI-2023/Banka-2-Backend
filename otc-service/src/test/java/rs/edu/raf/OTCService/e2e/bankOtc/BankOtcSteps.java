package rs.edu.raf.OTCService.e2e.bankOtc;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import rs.edu.raf.OTCService.data.dto.testing.FrontendOfferDto;
import rs.edu.raf.OTCService.data.dto.testing.OfferDto;
import rs.edu.raf.OTCService.repositories.MyOfferRepository;
import rs.edu.raf.OTCService.repositories.OfferRepository;
import rs.edu.raf.OTCService.service.test.BankOtcService;
import rs.edu.raf.OTCService.generator.JwtTokenGenerator;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BankOtcSteps extends BankOtcTestsConfig {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BankOtcStateTests bankOtcStateTests;
    @Autowired
    private BankOtcService bankOtcService;
    @Autowired
    private MyOfferRepository myOfferRepository;
    @Autowired
    private OfferRepository offerRepository;

    private MvcResult mvcResult;

    FrontendOfferDto frontendOfferDto = new FrontendOfferDto();
    OfferDto offerDto = new OfferDto();

    @Given("generate jwt")
    public void generateJWT(){
        bankOtcStateTests.jwt = JwtTokenGenerator.generateToken(1L, "user@gmail.com", "USER", "");
    }

    @When("Make an offer")
    public void makeOffer() {
        frontendOfferDto.setTicker("NVDA");
        frontendOfferDto.setAmount(5);
        frontendOfferDto.setPrice(1.0);

        ResultActions resultActions = null;
        try {
            resultActions = mockMvc.perform(post("/api/v1/otcTrade/makeOffer")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "Bearer " + bankOtcStateTests.jwt)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(frontendOfferDto))
            ).andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        mvcResult = resultActions.andReturn();
    }

    @When("Get all offers from another bank")
    public void getAllOffersFromAnotherBank() {
        ResultActions resultActions = null;
        try {
            resultActions = mockMvc.perform(get("/api/v1/otcTrade/getBanksStocks")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "Bearer " + bankOtcStateTests.jwt)
            ).andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        mvcResult = resultActions.andReturn();
    }

    @When("Get all offers")
    public void getAllOffers() {
        ResultActions resultActions = null;
        try {
            resultActions = mockMvc.perform(get("/api/v1/otcTrade/getOffers")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "Bearer " + bankOtcStateTests.jwt)
            ).andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        mvcResult = resultActions.andReturn();
    }

    @When("Get all our offers")
    public void getAllOurOffers() {
        ResultActions resultActions = null;
        try {
            resultActions = mockMvc.perform(get("/api/v1/otcTrade/getOurOffers")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "Bearer " + bankOtcStateTests.jwt)
            ).andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        mvcResult = resultActions.andReturn();
    }

    @When("Refresh offers")
    public void refreshOffers() {
        ResultActions resultActions = null;
        try {
            resultActions = mockMvc.perform(put("/api/v1/otcTrade/refresh")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "Bearer " + bankOtcStateTests.jwt)
            ).andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        mvcResult = resultActions.andReturn();
    }

    @Then("response status ok and body user")
    public void responseStatusOk(){
        assert HttpStatus.OK.value() == mvcResult.getResponse().getStatus();
    }

}
