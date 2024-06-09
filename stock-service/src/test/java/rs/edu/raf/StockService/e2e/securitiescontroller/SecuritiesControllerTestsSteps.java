package rs.edu.raf.StockService.e2e.securitiescontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.StockService.data.dto.SecuritiesDto;
import rs.edu.raf.StockService.services.SecuritiesService;

import java.time.LocalDate;
import java.util.List;

public class SecuritiesControllerTestsSteps extends SecuritiesControllerTestsConfig {

    private final RestTemplate restTemplate = new RestTemplateBuilder().build();
    private final String baseUrl = "http://localhost:8001/api/securities/get-by-settlement-date";
    private ResponseEntity<Object[]> response;

    @Autowired
    private ObjectMapper objectMapper;

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
        String url = baseUrl + "?futureDate=" + settlementDate;
        response = restTemplate.getForEntity(url, Object[].class);
    }

    @Then("the controller should return a list of securities")
    public void thenTheServerShouldReturnSecuritiesByDate() {
        Assertions.assertEquals(200, response.getStatusCodeValue());
    }
}