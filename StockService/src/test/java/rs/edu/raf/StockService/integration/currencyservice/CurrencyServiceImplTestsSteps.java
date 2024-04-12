package rs.edu.raf.StockService.integration.currencyservice;

import org.springframework.beans.factory.annotation.Autowired;
import rs.edu.raf.StockService.data.entities.Currency;
import rs.edu.raf.StockService.services.impl.CurrencyServiceImpl;

import java.util.List;

public class CurrencyServiceImplTestsSteps extends CurrencyServiceImplTestsConfig {

    @Autowired
    private CurrencyServiceImpl currencyService;
    //private CurrencyService currencyService;

    private List<Currency> returnedCurrencyList;
    private Currency returnedCurrency;
    /*
    @When("fetching all currencies")
    public void fetchingAllCurrencies() {
        returnedCurrencyList = currencyService.findAll();
    }

    @Then("return list of currencies")
    public void returnListOfCurrencies() {
        assertNotNull(returnedCurrencyList);
    }

    @When("fetching currency with id {string}")
    public void fetchingCurrencyWithId(String arg0) {
        Long id = Long.parseLong(arg0);
        returnedCurrency = currencyService.findById(id);
    }

    @Then("return currency with id {string}")
    public void returnCurrencyWithId(String arg0) {
        Long id = Long.parseLong(arg0);
        assertEquals(id, returnedCurrency.getId());
    }

    @Then("fetching currency with  non-existing id {string} throws NotFoundException")
    public void fetchingCurrencyWithNonExistingIdThrowsNotFoundException(String arg0) {
        Long id = Long.parseLong(arg0);
        assertThrows(NotFoundException.class, () -> currencyService.findById(id));
    }

    @When("fetching currency with existing name {string}")
    public void fetchingCurrencyWithExistingName(String arg0) {
        returnedCurrency = currencyService.findByCurrencyName(arg0);

    }

    @Then("returned currency with name {string}")
    public void returnedCurrencyWithName(String arg0) {
        assertEquals(arg0, returnedCurrency.getCurrencyName());
    }

    @Then("fetching currency with non-existing name {string}")
    public void fetchingCurrencyWithNonExistingName(String arg0) {
        assertThrows(NotFoundException.class, () -> currencyService.findByCurrencyName(arg0));

    }

     */
}
