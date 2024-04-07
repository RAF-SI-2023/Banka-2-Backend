package rs.edu.raf.StockService.integration.forexservice;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import rs.edu.raf.StockService.data.entities.Forex;
import rs.edu.raf.StockService.services.ForexService;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ForexServiceImplTestsSteps extends ForexServiceImplTestsConfig{
    @Autowired
    private ForexService forexService;
    private List<Forex> returnedForexes;
    private Forex returnedForex;
    @When("fetching all forex pairs")
    public void fetchingAllForexPairs() {
        returnedForexes = forexService.findAll();
    }

    @Then("returns list of forex pairs")
    public void returnsListOfForexPairs() {
        assertNotNull(returnedForexes);
    }

    @When("Fetching forex pair with id {string}")
    public void fetchingForexPairWithId(String arg0) {
        Long id = Long.parseLong(arg0);
        returnedForex = forexService.findById(id);
    }

    @Then("Returns forex pair with id {string}")
    public void returnsForexPairWithId(String arg0) {
        Long id = Long.parseLong(arg0);
        assertEquals(id, returnedForex.getId());
    }

    @When("Fetching forex pair with non-existing id {string}")
    public void fetchingForexPairWithNonExistingId(String arg0) {
        Long id = Long.parseLong(arg0);
        returnedForex = forexService.findById(id);
    }

    @Then("Returns null")
    public void returnsNull() {
        assertNull(returnedForex);
    }
}
