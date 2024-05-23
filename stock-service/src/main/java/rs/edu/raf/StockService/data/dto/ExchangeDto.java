package rs.edu.raf.StockService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeDto {
    private String exchangeName;
    private String exchangeAcronym;
    private String exchangeMICode;
    private String polity;
    private String currency;
    private Integer timeZone;
}
