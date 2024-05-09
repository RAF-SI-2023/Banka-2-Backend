package rs.edu.raf.BankService.data.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListingDto implements Serializable {

    private Long id;
    @NotNull
    private String symbol;
    private String description;
    @NotNull
    private String exchange;
    private Long lastRefresh;
    @NotNull
    private Double price;
    @NotNull
    private Double high;
    @NotNull
    private Double low;
    private Double change;
    @NotNull
    private Integer volume;

}
