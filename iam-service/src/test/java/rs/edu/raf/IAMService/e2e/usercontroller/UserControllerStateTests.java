package rs.edu.raf.IAMService.e2e.usercontroller;


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
public class UserControllerStateTests {
    String jwt;
}
