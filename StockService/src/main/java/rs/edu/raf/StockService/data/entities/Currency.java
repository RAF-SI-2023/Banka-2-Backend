package rs.edu.raf.StockService.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class Currency {
    @Id
    @GeneratedValue
    private int id;

    private String currencyName;
    private String currencyCode;
    private String currencySymbol;
    private String currencyPolity;
    @OneToMany(mappedBy = "currency")
    private List<CurrencyInflation> inflationList;

    public Currency(String currencyName, String currencyCode) {
        this.currencyName = currencyName;
        this.currencyCode = currencyCode;
    }

    public Currency(String currencyName, String currencyCode, String currencySymbol, String currencyPolity, List<CurrencyInflation> inflationList) {
        this.currencyName = currencyName;
        this.currencyCode = currencyCode;
        this.currencySymbol = currencySymbol;
        this.currencyPolity = currencyPolity;
        this.inflationList = inflationList;
    }
}
