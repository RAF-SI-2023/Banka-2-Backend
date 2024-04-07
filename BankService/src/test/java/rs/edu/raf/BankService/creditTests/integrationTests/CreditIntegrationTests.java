package rs.edu.raf.BankService.creditTests.integrationTests;

import io.cucumber.junit.Cucumber;
import org.junit.runner.RunWith;
import io.cucumber.junit.CucumberOptions;

@CucumberOptions(features = "src/test/java/resources", glue = "rs.edu.raf.BankService.creditTests.integrationTests", tags = "not @Ignore")
@RunWith(Cucumber.class)
public class CreditIntegrationTests {
}
