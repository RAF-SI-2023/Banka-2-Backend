package rs.edu.raf.BankService.service;

import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.data.dto.CardDto;
import rs.edu.raf.BankService.data.dto.CreateCardDto;
import rs.edu.raf.BankService.data.dto.CreditDto;
import rs.edu.raf.BankService.data.entities.card.Card;
import rs.edu.raf.BankService.data.entities.credit.Credit;

import java.util.List;

@Service
public interface CardService {

    CardDto createCard(CreateCardDto cardDto);

    CardDto getCardByIdentificationCardNumber(Long identificationCardNumber);

    CardDto changeCardStatus(Long cardNumber);

//    CardDto save(CardDto cardDto);

    List<CardDto> getCardsByAccountNumber(String accountNumber);

    void checkExpirationDate();

    CardDto changeCardLimit(CardDto cardDto);

    void deleteCard(Long identificationCardNumber);
}
