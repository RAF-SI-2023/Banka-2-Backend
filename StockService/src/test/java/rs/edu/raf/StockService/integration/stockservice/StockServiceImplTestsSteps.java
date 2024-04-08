package rs.edu.raf.StockService.integration.stockservice;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.webjars.NotFoundException;
import rs.edu.raf.StockService.data.entities.Stock;
import rs.edu.raf.StockService.services.StockService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StockServiceImplTestsSteps extends StockServiceImplTestsConfig {
    @Autowired
    private StockService stockService;
    private List<Stock> returnedStocks;
    private Stock returnedStock;

    @When("fetching all stocks")
    public void fetchingAllStocks() {
        returnedStocks = stockService.findAll();
    }

    @Then("return list of stocks")
    public void returnListOfStocks() {
        assertNotNull(returnedStocks);
    }

    @When("fetching stock with id {string}")
    public void fetchingStockWithId(String arg0) {
        Long id = Long.parseLong(arg0);
        returnedStock = stockService.findById(id);
    }

    @Then("return stock with id {string}")
    public void returnStockWithId(String arg0) {
        Long id = Long.parseLong(arg0);
        assertEquals(id, returnedStock.getId());
    }

    @Then("fetching stock with  non-existing id {string} throws NotFoundException")
    public void fetchingStockWithNonExistingIdThrowsNotFoundException(String arg0) {
        Long id = Long.parseLong(arg0);
        assertThrows(NotFoundException.class, () -> stockService.findById(id));
    }

    @When("fetching stocks with existing symbol {string}")
    public void fetchingStocksWithExistingSymbol(String symbol) {
        returnedStocks = stockService.findBySymbol(symbol);
    }

    @Then("returned list contains only stocks with symbol {string}:")
    public void returnedListContainsOnlyStocksWithSymbol(String symbol) {
        for (Stock stock : returnedStocks) {
            assertEquals(symbol, stock.getSymbol());
        }
    }

    @When("fetching stocks with non-existing symbol {string}")
    public void fetchingStocksWithNonExistingSymbol(String symbol) {
        returnedStocks = stockService.findBySymbol(symbol);
    }

    @Then("returned list does not contain any stock")
    public void returnedListDoesNotContainAnyStock() {
        assertTrue(returnedStocks.isEmpty());
    }
}
