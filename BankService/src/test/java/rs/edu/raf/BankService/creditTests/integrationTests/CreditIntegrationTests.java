package rs.edu.raf.BankService.creditTests.integrationTests;

import io.cucumber.core.options.Constants;
import io.cucumber.junit.Cucumber;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.junit.runner.RunWith;
import io.cucumber.junit.CucumberOptions;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/integration/credit-integration")
@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, value = "rs.edu.raf.BankService.creditTests.integrationTests")
public class CreditIntegrationTests {
}
