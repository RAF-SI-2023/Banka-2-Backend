package rs.edu.raf.StockService.data.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SecuritiesDto {
    private Long id;
    private String name;
    private String type;
    private long settlementDate;
    private Double price;
}
