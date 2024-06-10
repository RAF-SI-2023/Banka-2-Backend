package rs.edu.raf.OTCService.e2e.contracts;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.internal.verification.Calls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import rs.edu.raf.OTCService.controllers.ContractController;
import rs.edu.raf.OTCService.data.dto.ContractDto;
import rs.edu.raf.OTCService.data.entity.Contract;
import rs.edu.raf.OTCService.data.enums.ContractStatus;
import rs.edu.raf.OTCService.e2e.generators.JwtTokenGenerator;
import rs.edu.raf.OTCService.repositories.ContractRepository;
import rs.edu.raf.OTCService.service.ContractService;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ContractsE2ETestsSteps extends ContractsE2ETestsConfig{
    
    @Autowired
    private ContractsE2ETestsState state;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private ContractService contractService;

    @Autowired
    private ContractController contractController;

    private List<Contract> testContracts;
    // index : 0 -> for bank approve, 1 -> for seller approve, 2,3 same but for deny

    private ContractDto temporaryContractDto;
    private String sellerEmail = "testSeller@gmail.com";

    private final String BASE_URL = "http://localhost:8004/api/contracts";

    MvcResult result; // result from requests

    @Before    
    public void beforeScenario()
    {
        testContracts = new ArrayList<>();
        var contract1 = new Contract();
        contract1.setSellersEmail("1testSeller@gmail.com");
        contract1.setBuyersEmail("1testBuyer@gmail.com");
        contract1.setContractStatus(ContractStatus.WAITING);
        contract1.setBankConfirmation(false);
        contract1.setSellerConfirmation(false);

        var contract2 = new Contract();
        //contract2.setSellersEmail("2testSeller@gmail.com");
        contract2.setBuyersEmail("2testBuyer@gmail.com");
        contract2.setContractStatus(ContractStatus.WAITING);
        contract2.setBankConfirmation(false);
        contract2.setSellersEmail(sellerEmail);
        contract2.setSellerConfirmation(false);

        var contract3 = new Contract();
        contract3.setSellersEmail("3testSeller@gmail.com");
        contract3.setBuyersEmail("3testBuyer@gmail.com");
        contract3.setContractStatus(ContractStatus.WAITING);
        contract3.setBankConfirmation(true);
        
        var contract4 = new Contract();
        //contract4.setSellersEmail("4testSeller@gmail.com");
        contract4.setBuyersEmail("4testBuyer@gmail.com");
        contract4.setContractStatus(ContractStatus.WAITING);
        contract4.setSellerConfirmation(true);
        contract4.setSellersEmail(sellerEmail);

        testContracts.add(contract1);
        testContracts.add(contract2);
        testContracts.add(contract3);
        testContracts.add(contract4);

        testContracts = contractRepository.saveAll(testContracts);
        // for (Contract contract : testContracts) {
        //     System.out.println("Test contract " + contract.getId() + " exists");
        // }
    }

    @After
    public void afterScenario()
    {
        contractRepository.deleteAll(testContracts);
        // var res = contractRepository.findAll();
        // for (Contract contract : res) {
        //     System.out.println(contract.getId());
        // }
        
        // allContractsDto.clear(); not needed
        testContracts.clear();
        if (temporaryContractDto != null && contractRepository.existsById(temporaryContractDto.getId()))
            contractRepository.deleteById(temporaryContractDto.getId());
    }

    @Given("Admin logs in")
    public void adminLogsIn()
    {
        try{
           state.jwtToken = JwtTokenGenerator.generateToken(1L, "lazar@gmail.com", "ADMIN", "");
        }
        catch (Exception e){
           fail(e.getMessage());
        }
    }

    @When("Calls Bank Approve Contract")
    public void bankApprove()
    {
        try{
            long id = testContracts.get(0).getId();
            result = mockMvc.perform(put(BASE_URL + "/approve-bank/" + id)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + state.jwtToken))
            .andExpect(status().isOk()).andReturn();
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Then("Contract is approved by bank")
    public void checkBankApproved()
    {
        try{
            var response = objectMapper.readValue(result.getResponse().getContentAsString(), ContractDto.class);
            assertTrue(response.getBankConfirmation());
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Given("Seller logs in")
    public void sellerLogsIn()
    {
        try{
            state.jwtToken = JwtTokenGenerator.generateToken(1L, sellerEmail, "USER", "");
         }
         catch (Exception e){
            fail(e.getMessage());
         }
    }

    @When("Calls Seller Approve Contract")
    public void sellerApproves()
    {
        try{
            long id = testContracts.get(1).getId();
            result = mockMvc.perform(put(BASE_URL + "/approve-seller/" + id)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + state.jwtToken))
            .andExpect(status().isOk()).andReturn();
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }
    @Then("Contract is approved by seller")
    public void checkSellerApproved()
    {
        try{
            var response = objectMapper.readValue(result.getResponse().getContentAsString(), ContractDto.class);
            assertTrue(response.getSellerConfirmation());
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @When("Calls Bank Deny Contract")
    public void callBankDeny()
    {
        try{
            long id = testContracts.get(2).getId();
            result = mockMvc.perform(put(BASE_URL + "/deny-bank/" + id)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + state.jwtToken)
            .content("testMessage"))
            .andExpect(status().isOk()).andReturn();
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Then("Contract is denied by bank")
    public void checkBankDenied()
    {
        try{
            var response = objectMapper.readValue(result.getResponse().getContentAsString(), ContractDto.class);
            assertFalse(response.getBankConfirmation());
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @When("Calls Seller Deny Contract")
    public void callSellerDeny()
    {
        try{
            long id = testContracts.get(3).getId();
            result = mockMvc.perform(put(BASE_URL + "/deny-seller/" + id)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + state.jwtToken)
            .content("testMessage"))
            .andExpect(status().isOk()).andReturn();
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }
    @Then("Contract is denied by seller")
    public void checkSellerDenied()
    {
        try{
            var response = objectMapper.readValue(result.getResponse().getContentAsString(), ContractDto.class);
            assertFalse(response.getSellerConfirmation());
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Given("Wrong Seller logs in")
    public void wrongSellerLogIn()
    {
        try{
            state.jwtToken = JwtTokenGenerator.generateToken(0L, sellerEmail, "USER", "");
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }
    @When("Attempts to approve contract")
    public void attemptToApprove()
    {
        try{
            long id = testContracts.get(0).getId();
            result = mockMvc.perform(put(BASE_URL + "/approve-seller/" + id)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + state.jwtToken)
            .content("testMessage"))
            .andExpect(status().isNotFound()).andReturn(); // all errors return not found
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }
    @Then("Error pops up")
    public void checkIfThereIsError()
    {
        try{
            var response = result.getResponse().getContentAsString();
            assertEquals("You are not the seller", response);
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }
}
