package rs.edu.raf.BankService.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.BankService.data.dto.CardDto;
import rs.edu.raf.BankService.data.dto.CreateCardDto;
import rs.edu.raf.BankService.data.entities.card.Card;

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
                dto.getStatus(),
                dto.getBlock()
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
                card.getStatus(),
                card.getBlock()
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
