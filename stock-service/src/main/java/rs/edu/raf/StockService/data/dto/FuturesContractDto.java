package rs.edu.raf.StockService.data.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.StockService.data.enums.FuturesContractType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuturesContractDto {
    private Long id;
    private String name;
    private String code;
    private int contractSize;
    private String contractUnit;
    private int openInterest;
    private long settlementDate;
    private int maintenanceMargin;
    private FuturesContractType type;
    private Double futuresContractPrice;
    private Double rawMaterialPrice;
}
