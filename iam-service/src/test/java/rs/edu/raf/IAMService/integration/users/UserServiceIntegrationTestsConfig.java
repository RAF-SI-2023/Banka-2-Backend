package rs.edu.raf.IAMService.integration.users;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest()
@AutoConfigureMockMvc
public class UserServiceIntegrationTestsConfig {
}
