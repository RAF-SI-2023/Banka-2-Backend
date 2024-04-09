package rs.edu.raf.BankService.creditTests.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rs.edu.raf.BankService.controller.CurrencyExchangeController;
import rs.edu.raf.BankService.data.dto.ExchangeRatesDto;
import rs.edu.raf.BankService.data.dto.ExchangeRequestDto;
import rs.edu.raf.BankService.data.dto.ExchangeTransferDetailsDto;
import rs.edu.raf.BankService.data.entities.exchangeCurrency.ExchangeRates;
import rs.edu.raf.BankService.service.CurrencyExchangeService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExchangeControllerTests {
    @Mock
    private CurrencyExchangeService currencyExchangeService;

    @InjectMocks
    private CurrencyExchangeController currencyExchangeController;

    @Test
    void testGetAllExchangeRates_Success() {
        // Arrange
        ExchangeRatesDto exchangeResultDto = new ExchangeRatesDto(
                1l, 0, 0, "RSD",
                "EUR",
                100.0
        );


        when(currencyExchangeService.getExchangeRatesForCurrency("RSD")).thenReturn(List.of(exchangeResultDto));

        // Act
        ResponseEntity<?> response = currencyExchangeController.getAllExchangeRates("RSD");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(exchangeResultDto), response.getBody());
        verify(currencyExchangeService, times(1)).getExchangeRatesForCurrency("RSD");
    }

    @Test
    void testGetAllExchangeRates_Exception() {
        // Arrange
        String fromCurrency = "XYZ";
        String errorMessage = "Currency not found";
        when(currencyExchangeService.getExchangeRatesForCurrency(fromCurrency)).thenThrow(new RuntimeException(errorMessage));

        // Act
        ResponseEntity<?> response = currencyExchangeController.getAllExchangeRates(fromCurrency);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
        verify(currencyExchangeService, times(1)).getExchangeRatesForCurrency(fromCurrency);
    }

    @Test
    void testExchangeCurrency_Success() {
        // Arrange
        ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto("111111111111111111", "EUR",
                "111111111111111111", "USD", 100.0);
        ExchangeTransferDetailsDto exchangeResultDto = new ExchangeTransferDetailsDto(
                1l, "111111111111111111", "111111111111111111", "USD",
                "EUR",
                100.0,
                1.1,
                0,
                110.0,
                0
        );
        when(currencyExchangeService.exchangeCurrency(exchangeRequestDto)).thenReturn(exchangeResultDto);

        // Act
        ResponseEntity<?> response = currencyExchangeController.exchangeCurrency(exchangeRequestDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(exchangeResultDto, response.getBody());
        verify(currencyExchangeService, times(1)).exchangeCurrency(exchangeRequestDto);
    }

    @Test
    void testExchangeCurrency_Exception() {
        // Arrange
        ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto("111111111111111111", "EUR",
                "111111111111111111", "US", 100.0);
        String errorMessage = "Currency not found";
        when(currencyExchangeService.exchangeCurrency(exchangeRequestDto)).thenThrow(new RuntimeException(errorMessage));

        // Act
        ResponseEntity<?> response = currencyExchangeController.exchangeCurrency(exchangeRequestDto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
        verify(currencyExchangeService, times(1)).exchangeCurrency(exchangeRequestDto);
    }
}
