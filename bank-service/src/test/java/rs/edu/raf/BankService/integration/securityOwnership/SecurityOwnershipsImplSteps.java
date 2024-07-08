package rs.edu.raf.BankService.integration.securityOwnership;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import rs.edu.raf.BankService.data.dto.SecuritiesOwnershipDto;
import rs.edu.raf.BankService.repository.SecuritiesOwnershipRepository;
import rs.edu.raf.BankService.service.SecuritiesOwnershipService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SecurityOwnershipsImplSteps extends SecurityOwnershipsTestConfig {

    public List<SecuritiesOwnershipDto> responseListSecurity;
    public SecuritiesOwnershipDto updatedDto;
    ResponseEntity<?> responseEntity;
    String accountNumber;
    // @Autowired
    private SecuritiesOwnershipRepository repository;
    private List<SecuritiesOwnershipDto> response;
    private SecuritiesOwnershipService securitiesOwnershipService;
    private SecuritiesOwnershipRepository securitiesOwnershipRepository;
    private HttpClientErrorException exception;

    SecurityOwnershipsImplSteps(SecuritiesOwnershipService securitiesOwnershipService, SecuritiesOwnershipRepository securitiesOwnershipRepository) {
        this.securitiesOwnershipService = securitiesOwnershipService;
        this.securitiesOwnershipRepository = securitiesOwnershipRepository;
    }

    @Given("user has securities ownerships with account number {string}")
    public void userHasSecuritiesOwnerships(String accountNumber) {
        this.accountNumber = accountNumber;
        // You may initialize test data or set up mock objects here
    }

    @When("the user requests securities ownerships for account number {string}")
    public void userRequestsSecuritiesOwnerships(String accountNumber) {
        responseEntity = new ResponseEntity<>(securitiesOwnershipService.getSecurityOwnershipsBySecurity(accountNumber), null, 200);
        response = securitiesOwnershipService.getSecurityOwnershipsForAccountNumber(accountNumber);
    }

    @Then("the user should receive a response with status code {int}")
    public void userReceivesResponseWithStatusCode(int expectedStatusCode) {
        assertEquals(expectedStatusCode, responseEntity.getStatusCodeValue());
    }

    @Then("the response should contain the securities ownerships")
    public void responseContainsSecuritiesOwnerships() {
        assertNotNull(response);
    }

    @When("the user requests public securities ownerships")
    public void userRequestsPublicSecuritiesOwnerships() {
        responseEntity = new ResponseEntity<>(securitiesOwnershipService.getAllPubliclyAvailableSecurityOwnerships(), null, 200);
        response = securitiesOwnershipService.getSecurityOwnershipsForAccountNumber(accountNumber);
    }

//    Scenario: User looks up all public securities ownerships from privates
//    Given user has securities ownerships with account number "3334444999999999"
//    When the user requests public securities ownerships from privates
//    Then the user should receive a response with status code 200
//    And the response should contain the public securities ownerships from privates

    @Then("the response should contain the public securities ownerships")
    public void responseContainsPublicSecuritiesOwnerships() {
        assertNotNull(response);
    }

    @When("the user requests public securities ownerships from companies")
    public void userRequestsPublicSecuritiesOwnershipsFromCompanies() {
        responseEntity = new ResponseEntity<>(securitiesOwnershipService.getAllPubliclyAvailableSecurityOwnershipsFromCompanies(), null, 200);
        response = securitiesOwnershipService.getSecurityOwnershipsForAccountNumber(accountNumber);
    }

//    Scenario: User updates the publicly available quantity with a valid input
//    Given a securities ownership exists with id 1, quantity 100, and publicly available quantity 50
//    When the user updates the publicly available quantity to 60 for the securities ownership with id 1
//    Then the user should receive a response with status code 200
//    And the response should contain the updated publicly available quantity 60

    @Then("the response should contain the public securities ownerships from companies")
    public void responseContainsPublicSecuritiesOwnershipsFromCompanies() {
        assertNotNull(response);
    }

    @When("the user requests public securities ownerships from privates")
    public void userRequestsPublicSecuritiesOwnershipsFromPrivates() {
        responseEntity = new ResponseEntity<>(securitiesOwnershipService.getAllPubliclyAvailableSecurityOwnershipsFromPrivates(), null, 200);
        response = securitiesOwnershipService.getSecurityOwnershipsForAccountNumber(accountNumber);
    }

    @Then("the response should contain the public securities ownerships from privates")
    public void responseContainsPublicSecuritiesOwnershipsFromPrivates() {
        assertNotNull(response);
    }

    @Given("a securities ownership exists with id {long}, quantity {int}, and publicly available quantity {int}")
    public void securitiesOwnershipExists(Long id, int quantity, int publiclyAvailableQuantity) {
        SecuritiesOwnershipDto securitiesOwnershipDto = new SecuritiesOwnershipDto();
        securitiesOwnershipDto.setId(id);
        securitiesOwnershipDto.setQuantity(quantity);
        securitiesOwnershipDto.setQuantityOfPubliclyAvailable(publiclyAvailableQuantity);
        securitiesOwnershipService.updatePubliclyAvailableQuantity(securitiesOwnershipDto);
    }

    @When("the user updates the publicly available quantity to {int} for the securities ownership with id {long}")
    public void userUpdatesPubliclyAvailableQuantity(int publiclyAvailableQuantity, Long id) {
        SecuritiesOwnershipDto securitiesOwnershipDto = new SecuritiesOwnershipDto();
        securitiesOwnershipDto.setId(id);
        securitiesOwnershipDto.setQuantityOfPubliclyAvailable(publiclyAvailableQuantity);
        updatedDto = securitiesOwnershipService.updatePubliclyAvailableQuantity(securitiesOwnershipDto);
    }

    @Then("the response should contain the updated publicly available quantity {int}")
    public void responseContainsUpdatedPubliclyAvailableQuantity(int updatedPubliclyAvailableQuantity) {
        assertEquals(updatedPubliclyAvailableQuantity, updatedDto.getQuantityOfPubliclyAvailable());
    }

    @Then("the response should contain an error message {string}")
    public void responseCannotSetAmmountToMore(String updatedPubliclyAvailableQuantity) {
        System.out.println(updatedPubliclyAvailableQuantity + " ZDRAVO ");
        assertEquals(updatedPubliclyAvailableQuantity, updatedDto.getQuantityOfPubliclyAvailable());
    }

    @When("the user requests securities values for account number {string}")
    public void userRequestsSecuritiesValues(String accountNumber) {
        responseEntity = new ResponseEntity<>(securitiesOwnershipService.getValuesOfSecurities(accountNumber), null, 200);
    }

    @Then("the response should contain the securities values")
    public void responseContainsSecuritiesValues() {
        assertNotNull(responseEntity.getBody());
    }
}

