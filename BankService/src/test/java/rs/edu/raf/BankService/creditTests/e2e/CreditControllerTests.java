package rs.edu.raf.BankService.creditTests.e2e;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.junit.runner.RunWith;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@CucumberOptions(features = "src/test/java/resources/e2e", glue = "rs.edu.raf.BankService.creditTests.e2e", tags = "not @Ignore")
@RunWith(Cucumber.class)
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "rs.edu.raf.BankService.creditTests.e2e")
@SelectClasspathResource("e2e")
public class CreditControllerTests {
}
