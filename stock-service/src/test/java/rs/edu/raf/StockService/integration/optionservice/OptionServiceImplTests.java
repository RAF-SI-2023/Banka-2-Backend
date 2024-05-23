package rs.edu.raf.StockService.integration.optionservice;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.core.options.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/integration/optionservice")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "rs.edu.raf.StockService.integration.optionservice")
public class OptionServiceImplTests {
}
