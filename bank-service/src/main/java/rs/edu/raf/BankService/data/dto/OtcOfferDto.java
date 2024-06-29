package rs.edu.raf.BankService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OtcOfferDto {
    private String ticker;
    private Integer amount;
    private Double price;
}
