package rs.edu.raf.BankService.integration.transactionservice;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@CucumberContextConfiguration
@SpringBootTest
@MockBean(RabbitTemplate.class)
public class TransactionServiceIntegrationTestConfig {
}
