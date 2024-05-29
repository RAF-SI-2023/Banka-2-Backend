package rs.edu.raf.BankService.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.BankService.data.enums.FuturesContractType;



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
