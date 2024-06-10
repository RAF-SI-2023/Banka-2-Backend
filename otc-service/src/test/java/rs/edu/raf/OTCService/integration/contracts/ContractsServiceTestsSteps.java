package rs.edu.raf.OTCService.integration.contracts;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

import java.util.ArrayList;
import java.util.List;

import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import rs.edu.raf.OTCService.data.dto.ContractDto;
import rs.edu.raf.OTCService.data.entity.Contract;
import rs.edu.raf.OTCService.data.enums.ContractStatus;
import rs.edu.raf.OTCService.data.enums.ContractType;
import rs.edu.raf.OTCService.e2e.generators.JwtTokenGenerator;
import rs.edu.raf.OTCService.integration.contracts.generators.JwtConstant;
import rs.edu.raf.OTCService.repositories.ContractRepository;
import rs.edu.raf.OTCService.service.ContractService;
import rs.edu.raf.OTCService.util.SpringSecurityUtil;

public class ContractsServiceTestsSteps extends ContractsServiceIntegrationTestsConfig{
    
    @Autowired
    private ContractService contractService;

    @Autowired
    private ContractRepository contractRepository;

    private MockedStatic<SpringSecurityUtil> mockedStatic;
    List<Contract> testContracts;
    List<ContractDto> allContractsDto; // used in get all and get all waiting.
    ContractDto contractDto; // used in getById
    ContractDto temporaryContractDto; // delete after using
    @Autowired
    JwtConstant jwt;
    @Before    
    public void beforeScenario()
    {
        testContracts = new ArrayList<>();
        var contract1 = new Contract();
        contract1.setSellersEmail("1testSeller@gmail.com");
        contract1.setBuyersEmail("1testBuyer@gmail.com");
        contract1.setContractStatus(ContractStatus.WAITING);

        var contract2 = new Contract();
        contract2.setSellersEmail("2testSeller@gmail.com");
        contract2.setBuyersEmail("2testBuyer@gmail.com");
        contract2.setContractStatus(ContractStatus.WAITING);

        var contract3 = new Contract();
        contract3.setSellersEmail("3testSeller@gmail.com");
        contract3.setBuyersEmail("3testBuyer@gmail.com");
        contract3.setContractStatus(ContractStatus.APPROVED);

        testContracts.add(contract1);
        testContracts.add(contract2);
        testContracts.add(contract3);

        testContracts = contractRepository.saveAll(testContracts);
        // for (Contract contract : testContracts) {
        //     System.out.println("Test contract " + contract.getId() + " exists");
        // }
        MockitoAnnotations.openMocks(this);
        mockedStatic = mockStatic(SpringSecurityUtil.class);
        mockedStatic.when(SpringSecurityUtil::isAgent).thenReturn(false);
        mockedStatic.when(SpringSecurityUtil::isSupervisor).thenReturn(false);
        mockedStatic.when(SpringSecurityUtil::isUser).thenReturn(false);
        mockedStatic.when(SpringSecurityUtil::getPrincipalEmail).thenReturn("lukapavlovic032@gmail.com");

    }


    @After
    public void afterScenario()
    {
        contractRepository.deleteAll(testContracts);
        // var res = contractRepository.findAll();
        // for (Contract contract : res) {
        //     System.out.println(contract.getId());
        // }
        mockedStatic.close();
        // allContractsDto.clear(); not needed
        testContracts.clear();
        if (temporaryContractDto != null && contractRepository.existsById(temporaryContractDto.getId()))
            contractRepository.deleteById(temporaryContractDto.getId());
    }

    @Given("Contracts exist")
    public void contractsExist()
    {
        try{
            for (Contract contract : testContracts) {
                assertTrue(contractRepository.existsById(contract.getId()));
            }
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

    @When("Calling getAllContracts")
    public void callGetAllContracts()
    {
        try
        {
            allContractsDto = contractService.getAllContracts();
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Then("Get list of all contracts")
    public void getListOfAllContracts()
    {
        try{
            assertTrue(allContractsDto.size() >= 3);
            for (ContractDto contractDto : allContractsDto) {
                assertTrue(contractRepository.existsById(contractDto.getId()));
            }
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

    @When("Calling getAllWaitingContracts")
    public void callGetAllWaitingContracts()
    {
        try
        {
            allContractsDto = contractService.getAllWaitingContracts();
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Then("Get list of all waiting contracts")
    public void getListOfAllWaitingContracts()
    {
        try{
            assertTrue(allContractsDto.size() > 0);
            for (ContractDto contractDto : allContractsDto) {
                assertEquals(contractDto.getContractStatus(), ContractStatus.WAITING);
            }
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Given("Contract with id {long} exists")
    public void contractWithIdExists(long id)
    {
        try{
            assertTrue(contractRepository.existsById(id));
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

    @When("Calling getById {long}")
    public void callGetContractById(long id)
    {
        try{
            contractDto = contractService.getContractById(id);
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Then("Get contract with id {long}")
    public void getContractWithId(long id)
    {
        try{
            assertEquals(contractDto.getId(), id);
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

    
    @Given("Contract with seller's email {string}")
    public void makeContractWithSellersEmail(String email)
    {
        try{
            // make contract dto (sellers email is only important thing for this test)
            temporaryContractDto = new ContractDto(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                email,
                null);
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

    @When("Calling createContract")
    public void callCreateContract()
    {
        try{
            temporaryContractDto = contractService.createContract(temporaryContractDto);
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Then("Create Private Contract")
    public void checkIsContractPrivate()
    {
        try{
            assertEquals(temporaryContractDto.getContractType(), ContractType.PRIVATE_CONTRACT);
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Then("Create Legal Entity Contract")
    public void checkIsContractLegalEntity()
    {
        try{
            assertEquals(temporaryContractDto.getContractType(), ContractType.LEGAL_ENTITY_CONTRACT);
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Given("Non approved contract exists")
    public void makeNonApprovedContract()
    {
        try{
            // not approved, that is important
            temporaryContractDto = new ContractDto(null, null, null,
            null, null,
            null, null,
            null, null,
            null, null, null,
            null, null, null,
            null, null) ;
            
            // save
            temporaryContractDto = contractService.createContract(temporaryContractDto);
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

    @When("Calling bankApproveContract")
    public void callBankApprove()
    {
        try{
            temporaryContractDto = contractService.bankApproveContractById(temporaryContractDto.getId());
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Then("Contract gets bank approved")
    public void checkIfApprovedByBank()
    {
        try{
            assertTrue(temporaryContractDto.getBankConfirmation());
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Given("Approved contract exists")
    public void makeApprovedContract()
    {
        try{
            // not approved, that is important
            temporaryContractDto = new ContractDto(null, true, null,
            null, null,
            null, null,
            null, null,
            null, null, null,
            null, null, null,
            null, null);

            temporaryContractDto = contractService.createContract(temporaryContractDto);
            
            // make approved status true, so it can be denied
            contractService.bankApproveContractById(temporaryContractDto.getId());
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

    @When("Calling bankDenyContract")
    public void callBankDeny()
    {
        try{
            temporaryContractDto.setBankConfirmation(true); // make sure it isn't false
            temporaryContractDto = contractService.bankDenyContractById(temporaryContractDto.getId(), "test");
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Then("Contract gets bank denied")
    public void checkIfDeniedByBank()
    {
        try{
            assertFalse(temporaryContractDto.getBankConfirmation());
            System.out.println("12345");
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }
}