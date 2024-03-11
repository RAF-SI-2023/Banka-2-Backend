package rs.edu.raf.StockService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rs.edu.raf.StockService.data.entities.Currency;
import rs.edu.raf.StockService.repositories.CurrencyRepository;
import rs.edu.raf.StockService.services.impl.CurrencyServiceImpl;
import rs.edu.raf.StockService.services.impl.InMemoryCurrencyServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CurrencyServiceTests {

    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private CurrencyServiceImpl currencyService;

    @Mock
    private InMemoryCurrencyServiceImpl inMemoryCurrencyService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        inMemoryCurrencyService = new InMemoryCurrencyServiceImpl();
        List<Currency> currencies = new ArrayList<>();
        currencies.add(new Currency(1L, "US Dollar", "USD", "$", "USA", null));
        currencies.add(new Currency(2L, "EURO", "EUR", "e", "EU", null));
        inMemoryCurrencyService.setCurrencyList(currencies);

    }

    @Test
    public void testFindAll() {
        // Arrange
        List<Currency> currencies = new ArrayList<>();
        //String currencyName, String currencyCode, String currencySymbol, String currencyPolity, List<CurrencyInflation> inflationList
        currencies.add(new Currency("US dollar", "USD", "$", "USA", null));
        currencies.add(new Currency("EURO", "EUR", "e", "EU", null));


        when(currencyRepository.findAll()).thenReturn(currencies);

        // Act
        List<Currency> result = currencyService.findAll();
        List<Currency> resultInMemory = inMemoryCurrencyService.findAll();
        // Assert
        assertEquals(2, result.size());
        assertEquals("USD", result.get(0).getCurrencyCode());
        assertEquals("EUR", result.get(1).getCurrencyCode());

        assertEquals(2, resultInMemory.size());
        assertEquals("USD", resultInMemory.get(0).getCurrencyCode());
        assertEquals("EUR", resultInMemory.get(1).getCurrencyCode());
    }

    @Test
    public void testFindById() {
        // Arrange
        Currency currency = new Currency(1L, "US Dollar", "USD", "$", "USA", null);

        when(currencyRepository.findById(1L)).thenReturn(java.util.Optional.of(currency));

        // Act
        Currency result = currencyService.findById(1L);

        // Assert
        assertEquals(currency, result);


        Currency resultInMemory = inMemoryCurrencyService.findById(1L);

        // Assert
        assertEquals("USD", resultInMemory.getCurrencyCode());

    }

    @Test
    public void testFindByCurrencyCode() {
        // Arrange
        Currency currency = new Currency(1L, "US Dollar", "USD", "$", "USA", null);

        when(currencyRepository.findByCurrencyCode("USD")).thenReturn(currency);

        // Act
        Currency result = currencyService.findByCurrencyCode("USD");

        // Assert
        assertEquals(currency, result);

        Currency resultInMemory = inMemoryCurrencyService.findByCurrencyCode("USD");

        // Assert
        assertEquals("US Dollar", resultInMemory.getCurrencyName());
    }

    @Test
    public void testFindByCurrencyName() {
        // Arrange
        Currency currency = new Currency(1L, "US Dollar", "USD", "$", "USA", null);


        when(currencyRepository.findByCurrencyName("US Dollar")).thenReturn(currency);

        // Act
        Currency result = currencyService.findByCurrencyName("US Dollar");

        // Assert
        assertEquals(currency, result);

        Currency resultInMemory = inMemoryCurrencyService.findByCurrencyName("EURO");
        Currency resultInMemory2 = inMemoryCurrencyService.findByCurrencyName("euro");
        // Assert
        assertEquals("EUR", resultInMemory.getCurrencyCode());
        assertEquals("EUR", resultInMemory2.getCurrencyCode());
    }
}
