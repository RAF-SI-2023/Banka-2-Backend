package rs.edu.raf.OTCService.e2e.bankOtc;

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
public class BankOtcStateTests {
    String jwt;
}
