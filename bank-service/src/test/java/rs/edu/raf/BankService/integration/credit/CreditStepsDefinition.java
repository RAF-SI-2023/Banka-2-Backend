package rs.edu.raf.BankService.integration.credit;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.security.test.context.support.WithMockUser;
import rs.edu.raf.BankService.data.dto.CreditDto;
import rs.edu.raf.BankService.data.dto.CreditRequestDto;
import rs.edu.raf.BankService.data.enums.CreditRequestStatus;
import rs.edu.raf.BankService.data.enums.CreditType;
import rs.edu.raf.BankService.service.CreditService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class CreditStepsDefinition {
    private CreditService creditService;


    public CreditStepsDefinition(CreditService creditService) {
        this.creditService = creditService;
    }

    String accountNumber;
    List<CreditDto> credits = new ArrayList<>();
    CreditRequestDto creditRequestDto;
    Object response;

    @Given("user has an account with account number {string}")
    public void userHasAnAccount(String string) {
        accountNumber = string;
    }


    @When("user checks his credits\\/loans")
    @WithMockUser(username = "admin", authorities = {"ADMIN", "USER"}, roles = "ADMIN")
    public void userChecksHisCreditsLoans() throws Exception {
        credits = creditService.getCreditsByAccountNumber(accountNumber);
    }

    @Then("he should see his own credits")
    public void heShouldSeeHisOwnCredits() {
        assertNotNull(credits);
    }

    @Then("he should see a confirmation message")
    public void heShouldSeeAConfirmationMessage() {
        if (response instanceof CreditRequestDto)
            assertNotNull(((CreditRequestDto) response).getId());
        else
            fail();

    }

    @When("user creates a credit request on his account number {string}")
    public void userCreatesACreditRequestOnHisAccountNumber(String arg0) {
        creditRequestDto = new CreditRequestDto();
        creditRequestDto.setAccountNumber(arg0);
        creditRequestDto.setCreditAmount(36000.0);
        creditRequestDto.setCreditPurpose("STAMBENI");
        creditRequestDto.setCurrency("RSD");
        creditRequestDto.setEducationLevel("srednja");
        creditRequestDto.setEmploymentPeriod(5L);
        creditRequestDto.setHousingStatus("iznajmljen");
        creditRequestDto.setMaritalStatus("neozenjen");
        creditRequestDto.setMobileNumber("0655555555");
        creditRequestDto.setMonthlySalary(1000L);
        creditRequestDto.setPermanentEmployment(true);
        creditRequestDto.setOwnCar(false);
        creditRequestDto.setBranch("Novi Sad");
        creditRequestDto.setMaturity(12L);
        creditRequestDto.setCreditType(CreditType.STAMBENI);
        creditRequestDto.setNote("pls daj kredit");
        creditRequestDto.setPaymentPeriodMonths(new Random().nextLong(12, 36));
        response = creditService.createCreditRequest(creditRequestDto);
    }

    @When("employee checks credit requests")
    public void employeeChecksCreditRequests() {
        response = creditService.getAllCreditRequests();
    }

    @Then("he should see all credit requests")
    public void heShouldSeeAllCreditRequests() {
        assertNotNull(response);
        assertTrue(response instanceof List);
        assertFalse(((List<?>) response).isEmpty());
    }

}
