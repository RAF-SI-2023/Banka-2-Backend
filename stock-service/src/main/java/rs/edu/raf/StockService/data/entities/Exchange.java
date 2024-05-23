package rs.edu.raf.StockService.data.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Exchange {

    @Id
    @GeneratedValue
    private Long id;
    private String exchangeName;
    private String exchangeAcronym;
    private String exchangeMICode;
    private String polity;
    private String currency;
    private Integer timeZone;

    public Exchange(
            String exchangeName,
            String exchangeAcronym,
            String exchangeMICode,
            String polity,
            String currency,
            Integer timeZone
    ) {
        this.exchangeName = exchangeName;
        this.exchangeAcronym = exchangeAcronym;
        this.exchangeMICode = exchangeMICode;
        this.polity = polity;
        this.currency = currency;
        this.timeZone = timeZone;
    }

}
