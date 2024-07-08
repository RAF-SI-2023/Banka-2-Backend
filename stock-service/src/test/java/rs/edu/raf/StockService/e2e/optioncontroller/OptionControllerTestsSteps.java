package rs.edu.raf.StockService.e2e.optioncontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.StockService.controllers.OptionController;
import rs.edu.raf.StockService.repositories.OptionRepository;
import rs.edu.raf.StockService.services.impl.OptionServiceImpl;

import static org.junit.Assert.assertEquals;

//@AutoConfigureMockMvc
//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class OptionControllerTestsSteps extends OptionControllerTestsConfig {

    private final RestTemplate restTemplate = new RestTemplateBuilder().build();
    private final String baseUrl = "http://localhost:8001/api/options/stock-listing/";
    @Autowired
    private ObjectMapper objectMapper;
    private ResponseEntity<Object[]> response;
    @MockBean
    private OptionServiceImpl optionService;


    @MockBean
    private OptionRepository optionRepository;

    @MockBean
    private OptionController optionController;


    public OptionControllerTestsSteps(OptionRepository optionRepository, OptionServiceImpl optionService, OptionController optionController) {
        this.optionRepository = optionRepository;
        this.optionService = optionService;
        this.optionController = optionController;
    }


    @When("the client wants to requests options for root {string}")
    public void whenTheClientRequestsOptions(String stockListing) {
        String url = baseUrl + stockListing;
        response = restTemplate.getForEntity(url, Object[].class);
    }

    @Then("option should return value list by stock listing")
    public void thenTheServerShouldReturnOptions() {
        assertEquals(200, response.getStatusCodeValue()); // Assuming 200 is the expected status code for success
        // Add more assertions based on your response structure
    }
    //----------------------------------------------------------//


}