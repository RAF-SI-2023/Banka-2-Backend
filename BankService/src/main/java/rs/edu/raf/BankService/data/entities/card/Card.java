package rs.edu.raf.BankService.data.entities.card;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.BankService.data.enums.CardType;

@Entity
@Table(name = "card")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Card {

    @Id
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
