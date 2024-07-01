package rs.edu.raf.OTCService.e2e.bankOtc;

import io.cucumber.core.options.Constants;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import rs.edu.raf.OTCService.OtcServiceApplication;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/e2e/bankOtc")
@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, value = "rs.edu.raf.OTCService.e2e.bankOtc")
@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {OtcServiceApplication.class})
@ContextConfiguration
public class BankOtcTests {
}
