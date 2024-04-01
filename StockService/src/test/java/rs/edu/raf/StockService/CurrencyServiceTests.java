package rs.edu.raf.StockService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import rs.edu.raf.StockService.data.entities.Currency;
import rs.edu.raf.StockService.repositories.CurrencyRepository;
import rs.edu.raf.StockService.services.impl.CurrencyServiceImpl;
import rs.edu.raf.StockService.services.impl.InMemoryCurrencyServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class CurrencyServiceTests {

    @Mock
    private CurrencyRepository currencyRepository;
    @Mock
    private RedisTemplate<String, Currency> redisTemplate;
    @InjectMocks
    private CurrencyServiceImpl currencyService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAll() {
        // Arrange
        List<Currency> currencies = new ArrayList<>();
        //String currencyName, String currencyCode, String currencySymbol, String currencyPolity, List<CurrencyInflation> inflationList
        currencies.add(new Currency("US dollar", "USD", "$", "USA", new ArrayList<>()));
        currencies.add(new Currency("EURO", "EUR", "e", "EU", new ArrayList<>()));


        when(currencyRepository.findAllWithoutInflation()).thenReturn(currencies);

        // Act
        List<Currency> result = currencyService.findAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals("USD", result.get(0).getCurrencyCode());
        assertEquals("EUR", result.get(1).getCurrencyCode());

    }

    @Test
    public void testFindById() {
        // Arrange
        Currency currency = new Currency(1L, "EURO", "EUR", "e", "EU", new ArrayList<>());

        when(currencyRepository.findById(1L)).thenReturn(java.util.Optional.of(currency));

        // Act
        Currency result = currencyService.findById(1L);

        // Assert
        assertEquals(currency, result);


    }

    @Test
    public void testFindByCurrencyCode() {
        // Arrange
        Currency currency = new Currency(1L, "EURO", "EUR", "e", "EU", new ArrayList<>());

        when(currencyRepository.findByCurrencyCode("USD")).thenReturn(currency);

        // Act
        Currency result = currencyService.findByCurrencyCode("USD");

        // Assert
        assertEquals(currency, result);


    }

    @Test
    public void testFindByCurrencyName() {
        // Arrange
        Currency currency = new Currency(1L, "US Dollar", "USD", "$", "USA", new ArrayList<>());


        when(currencyRepository.findByCurrencyName("US Dollar")).thenReturn(currency);

        // Act
        Currency result = currencyService.findByCurrencyName("US Dollar");

        // Assert
        assertEquals(currency, result);


    }
}
