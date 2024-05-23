package rs.edu.raf.BankService.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.data.dto.CardDto;
import rs.edu.raf.BankService.data.dto.CreateCardDto;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.data.entities.card.Card;
import rs.edu.raf.BankService.mapper.CardMapper;
import rs.edu.raf.BankService.repository.CardRepository;
import rs.edu.raf.BankService.repository.CashAccountRepository;
import rs.edu.raf.BankService.service.CardService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardMapper cardMapper;
    private final CardRepository cardRepository;
    private final CashAccountRepository cashAccountRepository;

    @Override
    public CardDto createCard(CreateCardDto cardDto) {

        Card createdCard = cardMapper.createCardDtoToCard(cardDto);

        long cardNumber = generateCardNumber(1000000000000000L, 9999999999999999L);

        createdCard.setCvvCode(String.valueOf(generateCVVNumber(100, 999)));

        createdCard.setIdentificationCardNumber(cardNumber);

        if (createdCard.getCvvCode().length() != 3) {
            throw new RuntimeException("CVV code must have 3 digits");
        }
        if (createdCard.getIdentificationCardNumber().toString().length() != 16) {
            throw new RuntimeException("Card number must have 16 digits");
        }

        CashAccount cashAccount = cashAccountRepository.findByAccountNumber(createdCard.getAccountNumber());
        if (cashAccount == null) {
            throw new RuntimeException("Account with account number " + createdCard.getAccountNumber() + " not found");
        }

        List<Card> cardList = cardRepository.findActiveCardsAccountNumber(createdCard.getAccountNumber(), true);

        if (cardList.size() >= 3) {
            throw new RuntimeException("You can't have more than 3 cards");
        }
        cardRepository.save(createdCard);
        return cardMapper.cardToCardDto(createdCard);
        //    return null;
    }

    private int generateCVVNumber(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    private long generateCardNumber(long min, long max) {
        boolean exists = true;
        long randomNum = ThreadLocalRandom.current().nextLong(min, max + 1);
        while (exists) {
            if (cardRepository.findByIdentificationCardNumber(randomNum).isEmpty()) {
                exists = false;
            } else {
                randomNum = ThreadLocalRandom.current().nextLong(min, max + 1);
            }
        }
        return randomNum;
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

    @Override
    public void deleteCard(Long identificationCardNumber) {

        Optional<Card> cardOpt = cardRepository.findByIdentificationCardNumber(identificationCardNumber);

        if (cardOpt.isEmpty()) {
            throw new RuntimeException("Card with identification card number " + identificationCardNumber + " not found");
        }
        Card card = cardOpt.get();
        cardRepository.delete(card);

    }


}
