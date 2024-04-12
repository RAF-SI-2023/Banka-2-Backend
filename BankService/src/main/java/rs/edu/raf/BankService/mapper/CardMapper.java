package rs.edu.raf.BankService.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.BankService.data.dto.CardDto;
import rs.edu.raf.BankService.data.dto.CreateCardDto;
import rs.edu.raf.BankService.data.entities.card.Card;
import rs.edu.raf.BankService.data.enums.CardType;

@Component
public class CardMapper {

 public Card cardDtoToCard(CardDto dto){
        return new Card(
                dto.getIdentificationCardNumber(),
                dto.getCardType(),
                dto.getNameOfCard(),
                dto.getAccountNumber(),
                dto.getCvvCode(),
                dto.getLimitCard(),
                dto.getStatus()
        );
    }

    public CardDto cardToCardDto(Card card){
        return new CardDto(
                card.getIdentificationCardNumber(),
                card.getCardType(),
                card.getNameOfCard(),
                card.getCreationDate(),
                card.getExpirationDate(),
                card.getAccountNumber(),
                card.getCvvCode(),
                card.getLimitCard(),
                card.getStatus()
        );
    }



public Card createCardDtoToCard(CreateCardDto dto){
        return new Card(
                dto.getCardType(),
                dto.getNameOfCard(),
                dto.getAccountNumber(),
                dto.getLimitCard()
        );
    }


}
