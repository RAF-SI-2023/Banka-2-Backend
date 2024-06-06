package rs.edu.raf.BankService.e2e.orders;

import io.cucumber.spring.ScenarioScope;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@ScenarioScope
public class OrderControllerStateTests {
    String jwt;
}
