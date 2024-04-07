package rs.edu.raf.StockService.integration;


import io.cucumber.junit.Cucumber;
import org.junit.runner.RunWith;
import io.cucumber.junit.CucumberOptions;
//C:\Users\petar\Desktop\Banka-2-Backend\StockService\src\test\resources
@CucumberOptions(features = "src/test/resources", glue = "rs.edu.raf.StockService.integration", tags = "not @Ignore")
@RunWith(Cucumber.class)
public class CucumberIntegrationTest {
}
