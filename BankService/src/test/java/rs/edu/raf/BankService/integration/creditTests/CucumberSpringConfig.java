package rs.edu.raf.BankService.integration.creditTests;

import io.cucumber.core.options.Constants;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.springframework.boot.test.context.SpringBootTest;


@CucumberContextConfiguration
@SpringBootTest
@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, value = "rs/edu/raf/BankService/integration/creditTests")
public class CucumberSpringConfig {
}
