package rs.edu.raf.BankService.integration.credit;

import io.cucumber.core.options.Constants;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/integration/credit-integration")
@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, value = "rs.edu.raf.BankService.integration.credit")
public class CreditIntegrationTests {
}
