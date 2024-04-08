package rs.edu.raf.BankService.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.BankService.data.enums.CardType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDto {

    private Long identificationCardNumber;
    private CardType cardType;
    private String nameOfCard;
    private Long creationDate;
    private Long expirationDate;
    private String accountNumber;
    private String cvvCode;
    private Long limitCard;
    private Boolean status;

}
