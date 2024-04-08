package rs.edu.raf.BankService.creditTests;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.webjars.NotFoundException;
import rs.edu.raf.BankService.controller.CardController;
import rs.edu.raf.BankService.controller.CreditController;
import rs.edu.raf.BankService.data.dto.CardDto;
import rs.edu.raf.BankService.data.dto.CreditRequestDto;
import rs.edu.raf.BankService.data.entities.card.Card;
import rs.edu.raf.BankService.service.CardService;
import rs.edu.raf.BankService.service.CreditService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardControllerTests {

    @Mock
    private CardService cardService;

    @InjectMocks
    private CardController cardController;


    @Test
    void testCreateCard() {
        CardDto cardDto = new CardDto();
        when(cardService.createCard(cardDto)).thenReturn(cardDto);

        // When
        ResponseEntity<?> responseEntity = cardController.createCard(cardDto);
        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(cardDto, responseEntity.getBody());
    }

    @Test
    public void testCreateCard_Forbidden() {
        // Mock the response from the cardService when an exception occurs
        CardDto cardDto = new CardDto(); // Assuming CardDto is your DTO class
        when(cardService.createCard(cardDto)).thenThrow(new RuntimeException("Creation failed"));

        // Call the controller method
        ResponseEntity<?> responseEntity = cardController.createCard(cardDto);

        // Verify the response
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertEquals("Creation failed", responseEntity.getBody());
    }

    @Test
    public void testGetCardByIdentificationCardNumber() {
        // Mock the response from the cardService
        CardDto expectedCard = new CardDto(); // Assuming Card is your entity class
        when(cardService.getCardByIdentificationCardNumber(123L)).thenReturn(expectedCard);

        // Call the controller method
        ResponseEntity<?> responseEntity = cardController.getCardByIdentificationCardNumber(123L);

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedCard, responseEntity.getBody());
    }

    @Test
    public void testGetCardByIdentificationCardNumberNotFound() {
        // Mock the response from the cardService when card is not found
        when(cardService.getCardByIdentificationCardNumber(123L)).thenThrow(new NotFoundException("Card not found"));

        // Call the controller method
        ResponseEntity<?> responseEntity = cardController.getCardByIdentificationCardNumber(123L);

        // Verify the response
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Card not found", responseEntity.getBody());
    }


    @Test
    public void testChangeCardStatus() {
        // Mock the response from the cardService
        boolean statusChanged = true; // Assuming the status was successfully changed
        CardDto expectedCard = new CardDto();
        expectedCard.setStatus(!statusChanged);
        when(cardService.changeCardStatus(123L)).thenReturn(expectedCard);

        // Call the controller method
        ResponseEntity<?> responseEntity = cardController.changeCardStatus(123L);

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedCard, responseEntity.getBody());
    }

    @Test
    public void testChangeCardStatus_NotFound() {
        // Mock the response from the cardService when card is not found
        when(cardService.changeCardStatus(123L)).thenThrow(new NotFoundException("Card not found"));

        // Call the controller method
        ResponseEntity<?> responseEntity = cardController.changeCardStatus(123L);

        // Verify the response
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Card not found", responseEntity.getBody());
    }

    @Test
    public void testGetCardsByAccountNumber() {
        // Mock the response from the cardService
        List<CardDto> cards = Arrays.asList(new CardDto(), new CardDto()); // Assuming you have a list of Card objects
        String accountNumber = "123456789"; // Example account number
        when(cardService.getCardsByAccountNumber(accountNumber)).thenReturn(cards);

        // Call the controller method
        ResponseEntity<?> responseEntity = cardController.getCardsByAccountNumber(accountNumber);

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(cards, responseEntity.getBody());
    }

    @Test
    public void testGetCardsByAccountNumber_NotFound() {
        // Mock the response from the cardService when cards are not found
        String accountNumber = "123456789"; // Example account number
        when(cardService.getCardsByAccountNumber(accountNumber)).thenThrow(new NotFoundException("Cards not found"));

        // Call the controller method
        ResponseEntity<?> responseEntity = cardController.getCardsByAccountNumber(accountNumber);

        // Verify the response
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Cards not found", responseEntity.getBody());
    }

    @Test
    public void testChangeCardLimit_Success() {
        // Mock the response from the cardService
        CardDto cardDto = new CardDto(); // Assuming CardDto is your DTO class
        cardDto.setIdentificationCardNumber(123L); // Set identification card number
        when(cardService.changeCardLimit(cardDto)).thenReturn(cardDto);

        // Call the controller method
        ResponseEntity<?> responseEntity = cardController.changeCardLimit(cardDto);

        // Verify the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(cardDto, responseEntity.getBody());
    }

    @Test
    public void testChangeCardLimit_NotFound() {
        // Mock the response from the cardService when card is not found
        CardDto cardDto = new CardDto(); // Assuming CardDto is your DTO class
        cardDto.setIdentificationCardNumber(123L); // Set identification card number
        when(cardService.changeCardLimit(cardDto)).thenReturn(null);

        // Call the controller method
        ResponseEntity<?> responseEntity = cardController.changeCardLimit(cardDto);

        // Verify the response
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Card with identification card number 123 not found", responseEntity.getBody());
    }

}
