package rs.edu.raf.BankService.creditTests.integrationTests.credit;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import rs.edu.raf.BankService.BankServiceApplication;


@CucumberContextConfiguration
@SpringBootTest(classes = BankServiceApplication.class)
public class CucumberSpringConfig {
}
