package rs.edu.raf.BankService.integration.orders;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.core.options.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/integration/orders-integration")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "rs.edu.raf.BankService.integration.orders")
public class OrderServiceIntegrationTest {
}
