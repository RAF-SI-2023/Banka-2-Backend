package rs.edu.raf.StockService.data.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import rs.edu.raf.StockService.data.enums.FuturesContractType;

@Data
@AllArgsConstructor
public class FuturesContractDto {
    private Long id;
    private String name;
    private String code;
    private int contractSize;
    private String contractUnit;
    private int openInterest;
    private long settlementDate;
    private FuturesContractType type;
}
