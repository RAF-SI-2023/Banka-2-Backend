package rs.edu.raf.StockService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rs.edu.raf.StockService.controllers.ForexController;
import rs.edu.raf.StockService.data.entities.Forex;
import rs.edu.raf.StockService.services.impl.ForexServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ForexControllerTests {

    @Mock
    private ForexServiceImpl forexService;
    @InjectMocks
    private ForexController forexController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAllForexes() {
        List<Forex> forexes = new ArrayList<>();

        forexes.add(new Forex(
                "Forex1 Symbol",
                "Forex1 Description",
                "Forex1 Exchange",
                1L,
                1.0,
                1.0,
                1.0,
                1.0,
                1,
                "Forex1 baseCurrency",
                "Forex1 quoteCurrency"
        ));
        forexes.add(new Forex(
                "Forex2 Symbol",
                "Forex2 Description",
                "Forex2 Exchange",
                2L,
                2.0,
                2.0,
                2.0,
                2.0,
                2,
                "Forex2 baseCurrency",
                "Forex2 quoteCurrency"
        ));

        when(forexService.findAll()).thenReturn(forexes);

        ResponseEntity<List<Forex>> response = forexController.findAllForexes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testFindForexById() {
        Forex forex = new Forex(
                "Forex1 Symbol",
                "Forex1 Description",
                "Forex1 Exchange",
                1L,
                1.0,
                1.0,
                1.0,
                1.0,
                1,
                "Forex1 baseCurrency",
                "Forex1 quoteCurrency"
        );
        when(forexService.findById(1L)).thenReturn(forex);

        ResponseEntity<Forex> response = forexController.findForexById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(forex, response.getBody());
    }

    @Test
    public void testFindForexByBaseCurrency() {
        Forex forex = new Forex(
                "Forex1 Symbol",
                "Forex1 Description",
                "Forex1 Exchange",
                1L,
                1.0,
                1.0,
                1.0,
                1.0,
                1,
                "Forex1 baseCurrency",
                "Forex1 quoteCurrency"
        );
        when(forexService.findByBaseCurrency("Forex1 baseCurrency")).thenReturn(forex);

        ResponseEntity<Forex> response = forexController.findForexByBaseCurrency("Forex1 baseCurrency");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(forex, response.getBody());
    }

    @Test
    public void testFindForexByQuoteCurrency() {
        Forex forex = new Forex(
                "Forex1 Symbol",
                "Forex1 Description",
                "Forex1 Exchange",
                1L,
                1.0,
                1.0,
                1.0,
                1.0,
                1,
                "Forex1 baseCurrency",
                "Forex1 quoteCurrency"
        );
        when(forexService.findByQuoteCurrency("Forex1 quoteCurrency")).thenReturn(forex);

        ResponseEntity<Forex> response = forexController.findForexByQuoteCurrency("Forex1 quoteCurrency");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(forex, response.getBody());
    }

}
