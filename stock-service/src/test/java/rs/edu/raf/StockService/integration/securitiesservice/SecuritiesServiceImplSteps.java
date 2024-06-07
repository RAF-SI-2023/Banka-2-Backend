package rs.edu.raf.StockService.integration.securitiesservice;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import rs.edu.raf.StockService.data.dto.SecuritiesDto;
import rs.edu.raf.StockService.services.SecuritiesService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class SecuritiesServiceImplSteps {

    @MockBean
    private SecuritiesService securitiesService;

    private LocalDate settlementDate;
    private List<SecuritiesDto> securities;

    @Given("a settlement date exists")
    public void settlementDateExists() {
        settlementDate = LocalDate.now().plusDays(1);
    }

    @When("the client requests securities for the given date")
    public void whenTheClientRequestsSecuritiesByDate() {
        securities = securitiesService.getSecuritiesBySettlementDate(Optional.of(settlementDate));
    }

    @Then("the service should return a list of securities")
    public void thenTheServerShouldReturnSecuritiesByDate() {
        Assertions.assertNotNull(securities);
    }
}