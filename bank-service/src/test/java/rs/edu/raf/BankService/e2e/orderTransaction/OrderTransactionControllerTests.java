package rs.edu.raf.BankService.e2e.orderTransaction;

import io.cucumber.core.options.Constants;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/e2e/orderTransactionController")
@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, value = "rs.edu.raf.BankService.e2e.orderTransaction")
public class OrderTransactionControllerTests {
}
