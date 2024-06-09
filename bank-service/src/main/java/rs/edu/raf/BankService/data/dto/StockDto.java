package rs.edu.raf.BankService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockDto extends ListingDto {
    private Integer shares;
    private Double yield;

    public Double getMarketCap() {
        return shares * getPrice();
    }

    public Integer getContractSize() {
        return 1;
    }

    public Double getMaintenanceMargin() {
        return getPrice() * 0.5;
    }
    @Override
    public String toString(){
        return super.toString()+"shares="+shares+", yield=yield";
    }
}
