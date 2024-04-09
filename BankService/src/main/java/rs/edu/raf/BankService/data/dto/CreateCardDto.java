package rs.edu.raf.BankService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.BankService.data.enums.CardType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCardDto {


    private CardType cardType;
    private String nameOfCard;
    private String accountNumber;
    private Long limitCard;

}
