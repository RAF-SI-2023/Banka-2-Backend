package rs.edu.raf.BankService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankProfitDto {

    private Long id;
    private double profit;

}
