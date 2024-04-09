package rs.edu.raf.StockService.integration.optionservice;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rs.edu.raf.StockService.controllers.OptionController;
import rs.edu.raf.StockService.data.entities.Option;
import rs.edu.raf.StockService.data.enums.OptionType;
import rs.edu.raf.StockService.repositories.OptionRepository;
import rs.edu.raf.StockService.services.OptionService;
import rs.edu.raf.StockService.services.impl.OptionServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

//@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class OptionServiceImplSteps extends OptionServiceImplTestsConfig {
    private String Stockport = "8001";


    //    @Autowired
//    private TestRestTemplate restTemplate;
    private Long optionId = 1L;
    private String stockListing = "AAPL";

//    @Autowired
//    private MockMvc mockMvc;


    @MockBean
    private OptionService optionService;


    private ResponseEntity<List<Option>> responseOptionByStockListing;


    @MockBean
    private OptionRepository optionRepository;

    @MockBean
    private OptionController optionController;
    private List<Option> optionslist;


    public OptionServiceImplSteps(OptionRepository optionRepository, OptionServiceImpl optionService, OptionController optionController) {
        this.optionRepository = optionRepository;
        this.optionService = optionService;
        this.optionController = optionController;
    }


    //----------------------------------------------------------//
    @Given("a stock listing exists")
    public void stockListingExists() {
        // Mocking the service to return some sample data
        Option option = new Option();
        LocalDate currentDate = LocalDate.now();
        LocalDateTime localDateTime = currentDate.atStartOfDay();
        long milliseconds = localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
        option.setStockListing("TEST");
        option.setOptionType(OptionType.CALL);
        option.setStrikePrice(101D);
        option.setImpliedVolatility(421D);
        option.setOpenInterest(121D);
        option.setSettlementDate(milliseconds);

        Option option2 = new Option();
        option.setStockListing("TEST");
        option.setOptionType(OptionType.PUT);
        option.setStrikePrice(10D);
        option.setImpliedVolatility(42D);
        option.setOpenInterest(12D);
        option.setSettlementDate(milliseconds);

        stockListing = option.getStockListing();

        //  optionRepository.save(option);

        optionslist = List.of(option, option2);


    }

    @When("the client requests options for root {string}")
    public void whenTheClientRequestsOptionsByListing(String stock) throws Exception {

        ArrayList<Option> options = new ArrayList<>();
        options= (ArrayList<Option>) optionService.findAllByStockListing(stock);

            responseOptionByStockListing = new ResponseEntity<>(options, HttpStatus.OK);

        //     optionController = new OptionController(optionService);
        //     System.out.println(optionController.findAllOptionsByStockListing(stockListing));
        //      responseOptionByStockListing = new ResponseEntity<>(optionslist, HttpStatus.OK);
    }

    @Then("option should return list by stock listing")
    public void thenTheServerShouldReturnOptionByStockListing() {
        // Verifying that the response is not null and contains expected data
        Assertions.assertNotNull(responseOptionByStockListing);
        Assertions.assertEquals(HttpStatus.OK, responseOptionByStockListing.getStatusCode());
        List<Option> options = responseOptionByStockListing.getBody();
        Assertions.assertNotNull(options);
    }
    //----------------------------------------------------------//


}