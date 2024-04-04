package rs.edu.raf.StockService.cucumber;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import rs.edu.raf.StockService.StockServiceApplication;
import rs.edu.raf.StockService.services.impl.OptionServiceImpl;

@SpringBootTest(classes = StockServiceApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
public class IntegrationTest {




}
