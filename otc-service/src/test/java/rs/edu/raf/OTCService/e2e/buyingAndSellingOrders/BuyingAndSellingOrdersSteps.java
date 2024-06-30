package rs.edu.raf.OTCService.e2e.buyingAndSellingOrders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
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
import rs.edu.raf.OTCService.data.dto.ContractDto;
import rs.edu.raf.OTCService.data.enums.ContractStatus;
import rs.edu.raf.OTCService.data.enums.ContractType;
import rs.edu.raf.OTCService.generator.JwtTokenGenerator;

import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BuyingAndSellingOrdersSteps extends BuyingAndSellingOrdersTestsConfigTest {

    @Autowired
    private BuyingAndSellingOrdersJwtConst buyingAndSellingOrdersJwtConst;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private MockHttpServletResponse responseEntity;
    private MockHttpServletResponse responseEntityCreateContract;

    @Value("${otc.service.url:http://localhost:8004/api}")
    private String OTC_SERVICE_URL;

    @Value("${bank.service.url:http://bank-service:8003/api}")
    private String BANK_SERVICE_URL;

    private Long contractId; // Shared field to store contract ID

    @Given("Buyer with email {string} is logged in")
    public void userIsLoggedIn(String email) {
        buyingAndSellingOrdersJwtConst.jwt = JwtTokenGenerator.generateToken(16L, email, "USER", "");
    }

    @Given("Seller with email {string} is logged in")
    public void sellerIsLoggedIn(String email) {
        buyingAndSellingOrdersJwtConst.jwt = JwtTokenGenerator.generateToken(16L, email, "USER", "");
    }

    @Given("Admin with email {string} is logged in")
    public void adminIsLoggedIn(String email) {
        buyingAndSellingOrdersJwtConst.jwt = JwtTokenGenerator.generateToken(16L, email, "ADMIN", "");
    }

    @When("User buys from another user {int} shares of {string} stock")
    public void userBuysSharesOfStockFromAnother(int shares, String stock) {
        String jwtToken = buyingAndSellingOrdersJwtConst.jwt;

        ContractDto contractDto = new ContractDto();
        contractDto.setContractStatus(ContractStatus.WAITING);
        contractDto.setContractType(ContractType.PRIVATE_CONTRACT);
        contractDto.setDateTimeCreated(System.currentTimeMillis());
        contractDto.setDateTimeRealized(null);
        contractDto.setDescription(null);
        contractDto.setSellerConfirmation(false);
        contractDto.setSellersEmail("lpavlovic11521rn@raf.rs");
        contractDto.setBuyersEmail("lukapa369@gmail.com");
        contractDto.setSellersPIB(null);
        contractDto.setTicker(stock);
        contractDto.setTotalPrice(10D);
        contractDto.setVolume(shares);
        contractDto.setBankConfirmation(false);
        contractDto.setComment(null);
        contractDto.setContractNumber(null);

        try {
            ResultActions resultActions = mockMvc.perform(
                    post(OTC_SERVICE_URL + "/contracts/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + jwtToken)
                            .content(objectMapper.writeValueAsString(contractDto)))
                    .andExpect(status().isOk());
            MvcResult mvcResult = resultActions.andReturn();
            responseEntityCreateContract = mvcResult.getResponse();

            ContractDto createdContract = objectMapper.readValue(responseEntityCreateContract.getContentAsString(),
                    ContractDto.class);
            contractId = createdContract.getId();

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @When("Admin approves {int} shares of {string} stock for contract for user {string}")
    public void adminApprovesSharesOfStockForContract(int shares, String stock, String email) {
        String jwtToken = buyingAndSellingOrdersJwtConst.jwt;

        buyingAndSellingOrdersJwtConst.jwt2 = JwtTokenGenerator.generateToken(2L, email, "USER", "");
        List<ContractDto> responseList = null;
        try {
            ResultActions resultActions = mockMvc.perform(
                    get(OTC_SERVICE_URL + "/contracts/all-waiting")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + buyingAndSellingOrdersJwtConst.jwt2))
                    .andExpect(status().isOk());
            MvcResult mvcResult = resultActions.andReturn();
            responseEntity = mvcResult.getResponse();

            responseList = objectMapper.readValue(responseEntity.getContentAsString(),
                    new TypeReference<List<ContractDto>>() {
                    });

        } catch (Exception e) {
            fail(e.getMessage());
        }

        try {
            ResultActions resultActions = mockMvc.perform(
                    put(OTC_SERVICE_URL + "/contracts/approve-bank/"
                            + responseList.get(responseList.size() - 1).getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isOk());
            MvcResult mvcResult = resultActions.andReturn();
            responseEntity = mvcResult.getResponse();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @When("User approves {int} shares of {string} stock")
    public void userApprovesSharesOfStock(int shares, String stock) {
        String jwtToken = buyingAndSellingOrdersJwtConst.jwt;
        List<ContractDto> responseList = null;
        try {
            ResultActions resultActions = mockMvc.perform(
                    get(OTC_SERVICE_URL + "/contracts/all-waiting")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isOk());
            MvcResult mvcResult = resultActions.andReturn();
            responseEntity = mvcResult.getResponse();

            responseList = objectMapper.readValue(responseEntity.getContentAsString(),
                    new TypeReference<List<ContractDto>>() {
                    });

        } catch (Exception e) {
            fail(e.getMessage());
        }

        try {
            ResultActions resultActions = mockMvc.perform(
                    put(OTC_SERVICE_URL + "/contracts/approve-seller/"
                            + responseList.get(responseList.size() - 1).getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + jwtToken));
          
            MvcResult mvcResult = resultActions.andReturn();
            responseEntity = mvcResult.getResponse();
            assertEquals("damn", responseEntity.getContentAsString());

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @When("User checks his stock it should return a stock of {string} with {int} shares approved")
    public void userChecksHisStock(String symbol, int shares) {
        String jwtToken = buyingAndSellingOrdersJwtConst.jwt;
        List<ContractDto> responseList = null;
        try {
            ResultActions resultActions = mockMvc.perform(
                    get(OTC_SERVICE_URL + "/contracts/all")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isOk());
            MvcResult mvcResult = resultActions.andReturn();
            responseEntity = mvcResult.getResponse();

            responseList = objectMapper.readValue(responseEntity.getContentAsString(),
                    new TypeReference<List<ContractDto>>() {
                    });

            ContractDto contract = responseList.get(responseList.size() - 1);

            assertEquals(symbol, contract.getTicker());
            assertEquals(shares, contract.getVolume());
            assertEquals(ContractStatus.APPROVED, contract.getContractStatus());
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

    @Then("it should return a success response")
    public void itShouldReturnASuccessResponse() {
        // TODO: Implement success response validation if needed
    }
}
