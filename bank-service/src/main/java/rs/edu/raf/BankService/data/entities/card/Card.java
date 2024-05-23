package rs.edu.raf.BankService.data.entities.card;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.BankService.data.enums.CardType;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

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




    public Card(Long identificationCardNumber, CardType cardType, String nameOfCard, String accountNumber, String cvvCode, Long limitCard, Boolean status) {
        this.identificationCardNumber = identificationCardNumber;
        this.cardType = cardType;
        this.nameOfCard = nameOfCard;
        this.creationDate = Instant.now().getEpochSecond();
        LocalDateTime now = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
        LocalDateTime expirationLocalDateTime = now.plus(3, ChronoUnit.YEARS);
        Instant expirationInstant = expirationLocalDateTime.toInstant(ZoneOffset.UTC);
        this.expirationDate = expirationInstant.getEpochSecond();
        this.accountNumber = accountNumber;
        this.cvvCode = cvvCode;
        this.limitCard = limitCard;
        this.status = status;
    }


    public Card(CardType cardType, String nameOfCard, String accountNumber, Long limitCard) {
        this.cardType = cardType;
        this.nameOfCard = nameOfCard;
        this.creationDate = Instant.now().getEpochSecond();
        LocalDateTime now = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
        LocalDateTime expirationLocalDateTime = now.plus(3, ChronoUnit.YEARS);
        Instant expirationInstant = expirationLocalDateTime.toInstant(ZoneOffset.UTC);
        this.expirationDate = expirationInstant.getEpochSecond();
        this.accountNumber = accountNumber;
        this.limitCard = limitCard;
        this.status = true;
    }
}
