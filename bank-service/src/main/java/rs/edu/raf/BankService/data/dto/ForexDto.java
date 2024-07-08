package rs.edu.raf.BankService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForexDto extends ListingDto {
    private String baseCurrency;
    private String quoteCurrency;
    private Long contractSize;
    private Long maintenanceMargin;

    public Integer getContractSize() {
        return 1000;
    }

    public Double getMaintenanceMargin() {
        return getContractSize() * getPrice() * 0.1;
    }
}
