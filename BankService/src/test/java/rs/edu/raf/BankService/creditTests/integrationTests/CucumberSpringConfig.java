package rs.edu.raf.BankService.creditTests.integrationTests;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import rs.edu.raf.BankService.BankServiceApplication;


@CucumberContextConfiguration
@SpringBootTest(classes = BankServiceApplication.class)
public class CucumberSpringConfig {
}
