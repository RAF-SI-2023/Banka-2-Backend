package rs.edu.raf.StockService.integration.exchangeservice;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.webjars.NotFoundException;
import rs.edu.raf.StockService.data.entities.Exchange;
import rs.edu.raf.StockService.services.ExchangeService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExchangeServiceImplTestsSteps extends ExchangeServiceImplTestsConfig {
    @Autowired
    private ExchangeService exchangeService;
    private List<Exchange> returnedExchanges;
    private Exchange returnedExchange;

    @When("fetching all exchanges")
    public void fetchingAllExchanges() {
        returnedExchanges = exchangeService.findAll();
    }

    @When("return list of exchanges")
    public void returnAllExchanges() {
        assertNotNull(returnedExchanges);
    }

    @When("fetching exchange with id {string}")
    public void fetchingExchangeWithId(String arg0) {
        Long id = Long.parseLong(arg0);
        returnedExchange = exchangeService.findById(id);
    }

    @Then("return exchange with id {string}")
    public void returnExchangeWithId(String arg0) {
        Long id = Long.parseLong(arg0);
        assertEquals(id, returnedExchange.getId());
    }

    @Then("fetching exchange with  non-existing id {string} throws NotFoundException")
    public void fetchingExchangeWithNonExistingIdThrowsNotFoundException(String arg0) {
        Long id = Long.parseLong(arg0);
        returnedExchange = exchangeService.findById(id);
        assertNull(returnedExchange);
    }

    @When("fetching exchange with existing name {string}")
    public void fetchingExchangeWithExistingName(String name) {
        returnedExchange = exchangeService.findByExchangeName(name);
    }

    @Then("returned exchange with name {string}")
    public void returnedExchangeWithName(String name) {
        assertEquals(name, returnedExchange.getExchangeName());
    }

    @When("fetching exchange with non-existing name {string}")
    public void fetchingExchangeWithNonExistingName(String name) {
        returnedExchange = exchangeService.findByExchangeName(name);
    }

    @Then("returned list does not contain any exchange with name")
    public void returnedDoesNotContainAnyExchangeWithName() {
        assertTrue(returnedExchange == null);
    }

    @When("fetching exchange with existing MICode {string}")
    public void fetchingExchangeWithExistingMICode(String MICode) {
        returnedExchange = exchangeService.findByMICode(MICode);
    }

    @Then("returned exchange with MICode {string}")
    public void returnedExchangeWithMICode(String MICode) {
        assertEquals(MICode, returnedExchange.getExchangeMICode());
    }

    @Then("fetching exchange with non-existing MICode {string}")
    public void fetchingExchangeWithNonExistingMICode(String MICode) {
        returnedExchange = exchangeService.findByMICode(MICode);
        assertNull(returnedExchange);
    }

}
