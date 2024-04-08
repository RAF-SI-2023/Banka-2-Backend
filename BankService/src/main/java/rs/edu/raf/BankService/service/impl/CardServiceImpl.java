package rs.edu.raf.BankService.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.data.dto.CardDto;
import rs.edu.raf.BankService.data.entities.card.Card;
import rs.edu.raf.BankService.mapper.CardMapper;
import rs.edu.raf.BankService.repository.CardRepository;
import rs.edu.raf.BankService.service.CardService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardMapper cardMapper;
    private final CardRepository cardRepository;

    @Override
    public CardDto createCard(CardDto cardDto) {
        if (cardDto.getCvvCode().length() != 3) {
            throw new RuntimeException("CVV code must have 3 digits");
        }
        if (cardDto.getIdentificationCardNumber().toString().length() != 16) {
            throw new RuntimeException("Card number must have 16 digits");
        }
        List<Card> cardList = cardRepository.findActiveCardsAccountNumber(cardDto.getAccountNumber(), true);

        if (cardList.size() >= 3) {
            throw new RuntimeException("You can't have more than 3 cards");
        }
        Card card = cardMapper.cardDtoToCard(cardDto);
        cardRepository.save(card);
        return cardMapper.cardToCardDto(card);
        //    return null;
    }

    @Override
    public CardDto getCardByIdentificationCardNumber(Long identificationCardNumber) {

        CardDto cardDto = cardMapper.cardToCardDto(cardRepository.findByIdentificationCardNumber(identificationCardNumber).get());

        if (cardDto == null) {
            throw new RuntimeException("Card with identification card number " + identificationCardNumber + " not found");
        }

        return cardDto;
        //  return null;
    }

    @Override
    public CardDto changeCardStatus(Long cardNumber) {
        Card card = cardRepository.findByIdentificationCardNumber(cardNumber).get();
        if (card == null) {
            throw new RuntimeException("Card with identification card number " + cardNumber + " not found");
        }
        card.setStatus(!card.getStatus());
        cardRepository.save(card);
        CardDto cardDto = cardMapper.cardToCardDto(card);

        return cardDto;
    }

    @Override
    public List<CardDto> getCardsByAccountNumber(String accountNumber) {
        List<Card> cardList = cardRepository.findByAccountNumber(accountNumber);
        List<CardDto> cardDtoList = new ArrayList<>();
        for (Card card : cardList) {
            cardDtoList.add(cardMapper.cardToCardDto(card));
        }
        return cardDtoList;
    }

    @Override
    public void checkExpirationDate() {
        List<Card> cardList = cardRepository.findActiveCards(true);

        LocalDate currentDate = LocalDate.now();

        LocalDate startOfMonth = currentDate.withDayOfMonth(1);
        LocalDateTime startOfMonthMidnight = startOfMonth.atStartOfDay();
        long millis = startOfMonthMidnight.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();

        for (Card card : cardList) {
            System.out.println(card.getExpirationDate() + " " + millis);
            if (card.getExpirationDate() < millis) {

                card.setStatus(false);
                cardRepository.save(card);
            }
        }

    }

    @Override
    public CardDto changeCardLimit(CardDto cardDto) {
        Optional<Card> cardOpt = cardRepository.findByIdentificationCardNumber(cardDto.getIdentificationCardNumber());
        if (cardOpt.isEmpty()) {
        return null;
        }
        Card card = cardOpt.get();
        card.setLimitCard(cardDto.getLimitCard());
        cardRepository.save(card);

        return cardMapper.cardToCardDto(card);
    }


}
