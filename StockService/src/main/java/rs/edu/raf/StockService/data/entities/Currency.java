package rs.edu.raf.StockService.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class Currency implements Serializable {
    @Id
    @GeneratedValue
    private long id;

    private String currencyName;
    private String currencyCode;
    private String currencySymbol;
    private String currencyPolity;
    @OneToMany(mappedBy = "currencyId")
    private List<CurrencyInflation> inflationList;

    public Currency(String currencyName, String currencyCode) {
        this.currencyName = currencyName;
        this.currencyCode = currencyCode;
    }

    public Currency(String currencyName, String currencyCode, String currencySymbol, String currencyPolity) {
        this.currencyName = currencyName;
        this.currencyCode = currencyCode;
        this.currencySymbol = currencySymbol;
        this.currencyPolity = currencyPolity;
        this.inflationList = new ArrayList<>();
    }

    public Currency(String currencyName, String currencyCode, String currencySymbol, String currencyPolity, List<CurrencyInflation> inflationList) {
        this.currencyName = currencyName;
        this.currencyCode = currencyCode;
        this.currencySymbol = currencySymbol;
        this.currencyPolity = currencyPolity;
        this.inflationList = inflationList;
    }
}
