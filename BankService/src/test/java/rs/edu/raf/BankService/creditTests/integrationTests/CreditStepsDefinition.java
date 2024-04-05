package rs.edu.raf.BankService.creditTests.integrationTests;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import rs.edu.raf.BankService.controller.AccountController;
import rs.edu.raf.BankService.controller.CreditController;
import rs.edu.raf.BankService.data.dto.CreditDto;
import rs.edu.raf.BankService.service.CreditService;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class CreditStepsDefinition {
    private CreditService creditService;
    private AccountController accountController;
    @MockBean
    SecurityContext securityContextHolder;
    @MockBean
    MockMvc mockMvc;

    public CreditStepsDefinition(CreditService creditService, AccountController accountController) {
        this.creditService = creditService;
        this.accountController = accountController;
    }

    String accountNumber;
    List<CreditDto> credits;


    @Given("user has an account with account number {string}")
    public void userHasAnAccount(String string) {
        accountNumber = string;
    }


    @When("user checks his credits\\/loans")
    @WithMockUser(username = "admin", authorities = {"ADMIN", "USER"}, roles = "ADMIN")
    public void userLogsInAndChecksTheCredits() throws Exception {
        credits = creditService.getCreditsByAccountNumber(accountNumber);
    }

    @Then("he should see his own credits")
    public void heShouldSeeHisOwnCredits() {
        assertEquals(3, credits.size());

    }
}
