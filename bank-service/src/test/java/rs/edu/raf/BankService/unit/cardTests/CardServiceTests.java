package rs.edu.raf.BankService.unit.cardTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.BankService.data.dto.CardDto;
import rs.edu.raf.BankService.data.dto.CreateCardDto;
import rs.edu.raf.BankService.data.entities.accounts.BusinessCashAccount;
import rs.edu.raf.BankService.data.entities.card.Card;
import rs.edu.raf.BankService.data.enums.CardType;
import rs.edu.raf.BankService.mapper.CardMapper;
import rs.edu.raf.BankService.repository.CardRepository;
import rs.edu.raf.BankService.repository.CashAccountRepository;
import rs.edu.raf.BankService.service.impl.CardServiceImpl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardServiceTests {

    @Mock
    private CardMapper cardMapper;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private CashAccountRepository cashAccountRepository;

    @InjectMocks
    private CardServiceImpl cardService;

    @Test
    public void testCreateCard_Success() {
        // Mock the behavior of dependencies
        CreateCardDto cardDto = new CreateCardDto(); // Create a sample CardDto
        // Set a valid CVV code
        cardDto.setAccountNumber("3334444999999991"); // Set a valid account number

        BusinessCashAccount account = new BusinessCashAccount();
        account.setAccountNumber("3334444999999991");
        // Set a valid account number
        // Set a valid status
        Card card = new Card(); // Create a sample Card entity
        card.setCvvCode("123"); // Set a valid CVV code
        card.setIdentificationCardNumber(1234567891111111L);
        card.setAccountNumber("3334444999999991");
        card.setStatus(true);

        List<Card> lista = new ArrayList<>();
        lista.add(card);

        when(cashAccountRepository.findByAccountNumber(anyString())).thenReturn(account);
        when(cardMapper.createCardDtoToCard(cardDto)).thenReturn(card);
        when(cardRepository.findActiveCardsAccountNumber(anyString(), anyBoolean())).thenReturn(new ArrayList<>());
        when(cardRepository.findActiveCardsAccountNumber(anyString(), anyBoolean())).thenReturn(lista);

        // Call the method under test
        cardService.createCard(cardDto);

        // Verify that save method is called once
        verify(cardRepository, times(1)).save(card);
    }




    @Test
    public void testCreateCard_MaximumNumberOfCardsReached() {
        // Prepare test data
        CreateCardDto cardDto = new CreateCardDto();
        ; // Set a valid CVV code
        cardDto.setAccountNumber("1234567890123456"); // Set a valid account number
         // Set a valid identification card number
        // Set a valid status

        // Mock the behavior of the card repository to return three active cards
        List<Card> activeCards = Arrays.asList(new Card(), new Card(), new Card(), new Card());
   //     when(cardRepository.findActiveCardsAccountNumber(anyString(), anyBoolean())).thenReturn(activeCards);

        // Call the method under test and assert that it throws a RuntimeException
        assertThrows(RuntimeException.class, () -> cardService.createCard(cardDto));
    }

    @Test
    public void testGetCardByIdentificationCardNumber_Success() {
        // Prepare test data
        Long identificationCardNumber = 1234567890123456L; // Example identification card number
        Card card = new Card(); // Example card entity
        CardDto cardDto = new CardDto(); // Example card DTO

        // Mock the behavior of the card repository to return an optional containing the card entity
        when(cardRepository.findByIdentificationCardNumber(identificationCardNumber)).thenReturn(Optional.of(card));

        // Mock the behavior of the card mapper to return the card DTO
        when(cardMapper.cardToCardDto(card)).thenReturn(cardDto);

        // Call the method under test
        CardDto result = cardService.getCardByIdentificationCardNumber(identificationCardNumber);

        // Verify the result
        assertEquals(cardDto, result);
    }

    @Test
    public void testGetCardByIdentificationCardNumber_CardNotFound() {
        // Prepare test data
        Long identificationCardNumber = 1234567890123456L; // Example identification card number

        // Mock the behavior of the card repository to return an empty optional
        when(cardRepository.findByIdentificationCardNumber(identificationCardNumber)).thenReturn(Optional.empty());

        // Call the method under test and assert that it throws a RuntimeException
        assertThrows(RuntimeException.class, () -> cardService.getCardByIdentificationCardNumber(identificationCardNumber));
    }


    @Test
    public void testChangeCardStatus() {
        // Mock dependencies
        // Create test data
        Long cardNumber = 1234567890123456L; // Example card number
        Card card = new Card();
        card.setIdentificationCardNumber(cardNumber); // 16-digit card number
        card.setCardType(CardType.CREDIT); // or any other card type
        card.setNameOfCard("Test Card");
        card.setCreationDate(Instant.now().toEpochMilli()); // current timestamp in milliseconds
        card.setExpirationDate(10000L); // expiration date 3 years from now
        card.setAccountNumber("1234567890123456"); // 16-digit account number
        card.setCvvCode("123"); // 3-digit CVV code
        card.setLimitCard(1000L); // example limit
        card.setStatus(true);

        CardDto cardDto = new CardDto(); // Example card DTO
        cardDto.setIdentificationCardNumber(cardNumber); // 16-digit card number
        cardDto.setCardType(CardType.CREDIT); // or any other card type
        cardDto.setNameOfCard("Test Card");
       // current timestamp in milliseconds
      ; // expiration date 3 years from now
        cardDto.setAccountNumber("1234567890123456"); // 16-digit account number
        cardDto.setCvvCode("123"); // 3-digit CVV code
        cardDto.setLimitCard(1000L); // example limit
        cardDto.setStatus(true);


        // Mock behavior of cardRepository
        when(cardRepository.findByIdentificationCardNumber(cardNumber)).thenReturn(java.util.Optional.of(card));
        when(cardRepository.save(any(Card.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Echo back the input
        when(cardMapper.cardToCardDto(card)).thenReturn(cardDto);
        // Create instance of YourService (the class containing changeCardStatus method

        // Invoke the method under test
        CardDto result = cardService.changeCardStatus(cardNumber);

        // Verify the result
        assertTrue(result.getStatus()); // Expecting status to be true after toggling

        // Verify interactions with dependencies
        verify(cardRepository, times(1)).findByIdentificationCardNumber(cardNumber);
        verify(cardRepository, times(1)).save(card);
    }


    @Test
    public void testChangeCardStatus_CardNotFound() {
        // Prepare test data
        Long cardNumber = 1234567890123456L; // Example card number

        // Mock the behavior of the card repository to return an empty optional
        when(cardRepository.findByIdentificationCardNumber(cardNumber)).thenReturn(Optional.empty());

        // Call the method under test and assert that it throws a RuntimeException
        assertThrows(RuntimeException.class, () -> cardService.changeCardStatus(cardNumber));
    }

    @Test
    public void testGetCardsByAccountNumber() {
        // Prepare test data
        String accountNumber = "1234567890123456"; // Example account number
        List<Card> cardList = new ArrayList<>();
        cardList.add(createCard("1111111111111111"));
        cardList.add(createCard("2222222222222222"));
        cardList.add(createCard("3333333333333333"));

        // Stubbing the behavior of cardRepository to return the list of cards for the given accountNumber
        when(cardRepository.findByAccountNumber(accountNumber)).thenReturn(cardList);

        // Call the method under test
        List<CardDto> result = cardService.getCardsByAccountNumber(accountNumber);

        // Verify the result
        assertNotNull(result); // Ensure result is not null
        assertEquals(cardList.size(), result.size()); // Ensure the size of the result matches the size of the card list

        // Verify that cardToCardDto method is called for each card in the list
        verify(cardMapper, times(cardList.size())).cardToCardDto(any(Card.class));
    }

    private Card createCard(String identificationCardNumber) {
        Card card = new Card();
        Long s = Long.parseLong(identificationCardNumber);
        card.setIdentificationCardNumber(s); // Set the identification card number
        // Set other properties of the card as needed for testing
        return card;
    }

    @Test
    public void testCheckExpirationDate() {
        // Prepare test data
        List<Card> cardList = new ArrayList<>();
        LocalDate currentDate = LocalDate.ofYearDay(2024, 1);
        LocalDate startOfMonth = currentDate.withDayOfMonth(1);
        LocalDateTime startOfMonthMidnight = startOfMonth.atStartOfDay();
        long millis = startOfMonthMidnight.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();

        cardList.add(createCard(1234567890123456L, 100L)); // Expired card
        cardList.add(createCard(2345678901234567L, 999999999999999999L)); // Not expired card

        // Stubbing the behavior of cardRepository to return the list of cards
        when(cardRepository.findActiveCards(true)).thenReturn(cardList);

        // Call the method under test
        cardService.checkExpirationDate();

        // Verify that save method is called for expired cards
        verify(cardRepository, times(1)).save(any(Card.class)); // Ensure that save method is called at least once
        // You can also add more specific verifications based on your requirements
    }

    private Card createCard(Long identificationCardNumber, Long expirationDate) {
        Card card = new Card();
        card.setIdentificationCardNumber(identificationCardNumber); // Set the identification card number
        card.setExpirationDate(expirationDate); // Set the expiration date
        // Set other properties of the card as needed for testing
        return card;
    }


    @Test
    public void testChangeCardLimit_Success() {
        // Prepare test data
        Long identificationCardNumber = 1234567890123456L;
        Long newLimit = 2000L;
        CardDto cardDto = new CardDto();
        cardDto.setIdentificationCardNumber(identificationCardNumber);
        cardDto.setLimitCard(newLimit);

        Card card = new Card();
        card.setIdentificationCardNumber(identificationCardNumber);
        // Set other properties of the card as needed for testing

        // Stubbing the behavior of cardRepository to return an Optional containing the card entity
        when(cardRepository.findByIdentificationCardNumber(identificationCardNumber)).thenReturn(Optional.of(card));
        when(cardMapper.cardToCardDto(card)).thenReturn(cardDto);
        // Call the method under test
        CardDto result = cardService.changeCardLimit(cardDto);

        // Verify that the card limit is updated
        assertEquals(newLimit, card.getLimitCard());

        // Verify that save method is called once
        verify(cardRepository, times(1)).save(card);

        // Verify that the returned card DTO is not null and has the correct limit
        assertNotNull(result);

    }

    @Test
    public void testChangeCardLimit_CardNotFound() {
        // Prepare test data
        Long identificationCardNumber = 1234567890123456L;
        CardDto cardDto = new CardDto();
        cardDto.setIdentificationCardNumber(identificationCardNumber);

        // Stubbing the behavior of cardRepository to return an empty Optional, indicating that the card is not found
        when(cardRepository.findByIdentificationCardNumber(identificationCardNumber)).thenReturn(Optional.empty());

        // Call the method under test
        CardDto result = cardService.changeCardLimit(cardDto);

        // Verify that the method returns null when the card is not found
        assertNull(result);
    }
}