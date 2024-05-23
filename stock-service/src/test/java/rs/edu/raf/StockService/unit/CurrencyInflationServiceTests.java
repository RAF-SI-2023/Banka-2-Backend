package rs.edu.raf.StockService.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rs.edu.raf.StockService.data.entities.CurrencyInflation;
import rs.edu.raf.StockService.repositories.CurrencyInflationRepository;
import rs.edu.raf.StockService.services.impl.CurrencyInflationServiceImpl;
import rs.edu.raf.StockService.services.impl.InMemoryCurrencyInflationServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

public class CurrencyInflationServiceTests {

    @Mock
    private CurrencyInflationRepository currencyInflationRepository;

    private InMemoryCurrencyInflationServiceImpl inMemoryCurrencyInflationService;

    @InjectMocks
    private CurrencyInflationServiceImpl currencyInflationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        inMemoryCurrencyInflationService = new InMemoryCurrencyInflationServiceImpl();
        List<CurrencyInflation> inflationList = new ArrayList<>();
        inflationList.add(new CurrencyInflation(1.5, 2002, 1L, 1));
        inflationList.add(new CurrencyInflation(2.3, 2003, 2L, 1));
        inMemoryCurrencyInflationService.setCurrencyInflationList(inflationList);
    }

    @Test
    public void testFindInflationByCurrencyId() {
        // Arrange
        long currencyId = 1L;
        List<CurrencyInflation> inflationList = new ArrayList<>();
        inflationList.add(new CurrencyInflation(1.5, 2002, 1L, 1));
        inflationList.add(new CurrencyInflation(2.3, 2003, 2L, 1));

        when(currencyInflationRepository.findByCurrencyId(currencyId)).thenReturn(Optional.of(inflationList));

        // Act
        List<CurrencyInflation> result = currencyInflationService.findInflationByCurrencyId(currencyId);

        // Assert
        assertEquals(2, result.size());
        assertEquals(1.5, result.get(0).getInflationRate());
        assertEquals(2.3, result.get(1).getInflationRate());

        List<CurrencyInflation> resultInMemory = inMemoryCurrencyInflationService.findInflationByCurrencyId(currencyId);

        // Assert
        assertEquals(2, resultInMemory.size());
        assertEquals(1.5, resultInMemory.get(0).getInflationRate());
        assertEquals(2.3, resultInMemory.get(1).getInflationRate());
    }

    @Test
    public void testFindInflationByCurrencyIdAndYear() {
        // Arrange
        long currencyId = 1L;
        long year = 2002;
        CurrencyInflation inflation = new CurrencyInflation(1.5, 2002, 1L, 1);

        when(currencyInflationRepository.findByCurrencyIdAndYear(currencyId, year)).thenReturn(Optional.of(inflation));

        // Act
        CurrencyInflation result = currencyInflationService.findInflationByCurrencyIdAndYear(currencyId, year);

        // Assert
        assertEquals(1.5, result.getInflationRate());

        CurrencyInflation resultInMemory = inMemoryCurrencyInflationService.findInflationByCurrencyIdAndYear(1L, 2002);
        CurrencyInflation resultInMemory2 = inMemoryCurrencyInflationService.findInflationByCurrencyIdAndYear(1L, 2025);
        // Assert
        assertEquals(1.5, resultInMemory.getInflationRate());
        assertNull(resultInMemory2);
    }
}
