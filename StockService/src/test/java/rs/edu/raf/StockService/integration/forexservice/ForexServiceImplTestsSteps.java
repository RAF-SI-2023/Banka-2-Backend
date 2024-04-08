package rs.edu.raf.StockService.integration.forexservice;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.webjars.NotFoundException;
import rs.edu.raf.StockService.data.entities.Forex;
import rs.edu.raf.StockService.services.ForexService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ForexServiceImplTestsSteps extends ForexServiceImplTestsConfig {
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

    @Then("Fetching forex pair with non-existing id {string} throws NotFound exception")
    public void fetchingForexPairWithNonExistingIdThrowsNotFoundException(String arg0) {
        Long id = Long.parseLong(arg0);
        assertThrows(NotFoundException.class, () -> forexService.findById(id));
    }

    @When("fetching forex pairs with base-currency {string}")
    public void fetchingForexPairsWithBaseCurrency(String baseCurrency) {
        returnedForexes = forexService.findByBaseCurrency(baseCurrency);
    }

    @Then("returned list contains forex pairs with base-currency {string}")
    public void returnedListContainsForexPairsWithBaseCurrency(String baseCurrency) {
        for (Forex forex : returnedForexes) {
            assertEquals(forex.getBaseCurrency(), baseCurrency);
        }
    }

    @When("fetching forex pairs with non-existing base-currency {string}")
    public void fetchingForexPairsWithNonExistingBaseCurrency(String baseCurrency) {
        returnedForexes = forexService.findByBaseCurrency(baseCurrency);

    }

    @When("fetching forex pairs with quote-currency {string}")
    public void fetchingForexPairsWithQuoteCurrency(String quoteCurrency) {
        returnedForexes = forexService.findByQuoteCurrency(quoteCurrency);
    }

    @Then("returned list contains forex pairs with quote-currency {string}")
    public void returnedListContainsForexPairsWithQuoteCurrency(String quoteCurrency) {
        for (Forex forex : returnedForexes) {
            assertEquals(forex.getQuoteCurrency(), quoteCurrency);
        }
    }

    @When("fetching forex pairs with non-existing quote-currency {string}")
    public void fetchingForexPairsWithNonExistingQuoteCurrency(String quoteCurrency) {
        returnedForexes = forexService.findByQuoteCurrency(quoteCurrency);
    }

    @Then("returned list is empty")
    public void returnedListIsEmpty() {
        assertTrue(returnedForexes.isEmpty());
    }
}
