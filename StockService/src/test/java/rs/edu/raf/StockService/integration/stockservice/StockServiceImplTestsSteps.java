package rs.edu.raf.StockService.integration.stockservice;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import rs.edu.raf.StockService.data.entities.Stock;
import rs.edu.raf.StockService.services.StockService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StockServiceImplTestsSteps extends StockServiceImplTestsConfig{
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

    @When("fetching stock with  non-existing id {string}")
    public void fetchingStockWithNonExistingId(String arg0) {
        Long id = Long.parseLong(arg0);
        returnedStock = stockService.findById(id);
    }

    @Then("returns null")
    public void returnsNull() {
        assertNull(returnedStock);
    }

    @When("fetching stock with non-existing symbol {string}")
    public void fetchingStockWithNonExistingSymbol(String arg0) {
        returnedStock = stockService.findBySymbol(arg0);
    }
}
