package rs.edu.raf.OTCService.e2e.buyingAndSellingOrders;


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
public class BuyingAndSellingOrdersJwtConst {
    String jwt;
    String jwt2;
}
