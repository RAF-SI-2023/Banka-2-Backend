package rs.edu.raf.OTCService.data.dto.testing;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OfferDto implements Serializable {
    private String ticker;
    private Integer amount;
    private Double price;
    private Long idBank; // id koji je u bazi vase banke
}
