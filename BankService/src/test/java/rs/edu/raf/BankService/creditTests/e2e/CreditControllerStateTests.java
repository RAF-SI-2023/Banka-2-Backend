package rs.edu.raf.BankService.creditTests.e2e;


import io.cucumber.spring.ScenarioScope;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

// @ScenarioScope anotacija se koriste ukoliko zelite da se ova klasa unisti nakon zavrsetka jednog scenarija.
// Ukoliko zelite da delite state izmedju razlicitih scenarija, mozete da obrisete ovu anotaciju, mada se to
// bas i ne preporucuje.
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@ScenarioScope
public class CreditControllerStateTests {
    String jwt;
}
