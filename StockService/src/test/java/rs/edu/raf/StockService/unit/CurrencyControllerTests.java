package rs.edu.raf.StockService.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rs.edu.raf.StockService.controllers.CurrencyController;
import rs.edu.raf.StockService.data.entities.Currency;
import rs.edu.raf.StockService.data.entities.CurrencyInflation;
import rs.edu.raf.StockService.services.impl.CurrencyInflationServiceImpl;
import rs.edu.raf.StockService.services.impl.CurrencyServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

public class CurrencyControllerTests {

    @Mock
    private CurrencyServiceImpl currencyService;

    @Mock
    private CurrencyInflationServiceImpl currencyInflationService;

    @InjectMocks
    private CurrencyController currencyController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAllCurrency() {
        // Arrange
        List<Currency> currencies = new ArrayList<>();
        currencies.add(new Currency(1L, "US dollar", "USD", "$", "USA", null));
        currencies.add(new Currency(1L, "EURO", "EUR", "e", "EU", null));
        when(currencyService.findAll()).thenReturn(currencies);

        // Act
        ResponseEntity<List<Currency>> response = currencyController.findAllCurrency();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testFindCurrencyById() {
        // Arrange
        Currency currency = new Currency(1L, "US dollar", "USD", "$", "USA", null);
        when(currencyService.findById(1L)).thenReturn(currency);

        // Act
        ResponseEntity<Currency> response = currencyController.findCurrencyById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(currency, response.getBody());
    }

    @Test
    public void testFindCurrencyByCurrencyCode() {
        // Arrange
        Currency currency = new Currency(1L, "US dollar", "USD", "$", "USA", null);
        when(currencyService.findByCurrencyCode("USD")).thenReturn(currency);

        // Act
        ResponseEntity<Currency> response = currencyController.findCurrencyByCurrencyCode("USD");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(currency, response.getBody());
    }

    @Test
    public void testFindInflationByCurrencyId() {
        // Arrange
        long currencyId = 1L;
        List<CurrencyInflation> inflationList = new ArrayList<>();
        inflationList.add(new CurrencyInflation(5.2, 2022, 1L, 1));
        inflationList.add(new CurrencyInflation(3.6, 2023, 2L, 1));
        when(currencyInflationService.findInflationByCurrencyId(currencyId)).thenReturn(inflationList);

        // Act
        ResponseEntity<List<CurrencyInflation>> response = currencyController.findInflationByCurrencyId(currencyId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals(5.2, response.getBody().get(0).getInflationRate());
    }

    @Test
    public void testFindInflationByCurrencyIdAndYear() {
        // Arrange
        long currencyId = 1L;
        long year = 2022;
        CurrencyInflation inflation = new CurrencyInflation(5.2, 2022, 1L, 1);
        when(currencyInflationService.findInflationByCurrencyIdAndYear(currencyId, year)).thenReturn(inflation);

        // Act
        ResponseEntity<CurrencyInflation> response = currencyController.findInflationByCurrencyIdAndYear(currencyId, year);
        ResponseEntity<CurrencyInflation> responseFailed = currencyController.findInflationByCurrencyIdAndYear(currencyId, 2052);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(inflation, response.getBody());
        assertNull(responseFailed.getBody());
    }

}
