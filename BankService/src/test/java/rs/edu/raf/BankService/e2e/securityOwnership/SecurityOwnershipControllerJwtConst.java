package rs.edu.raf.BankService.e2e.securityOwnership;


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
public class SecurityOwnershipControllerJwtConst {
    String jwt;
}
