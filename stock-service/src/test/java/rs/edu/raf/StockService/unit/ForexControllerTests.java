package rs.edu.raf.StockService.unit;

import org.aspectj.weaver.ast.Not;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.webjars.NotFoundException;
import rs.edu.raf.StockService.controllers.ForexController;
import rs.edu.raf.StockService.data.dto.SecuritiesPriceDto;
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
        MockitoAnnotations.openMocks(this);
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
                "Forex1 quoteCurrency"));
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
                "Forex2 quoteCurrency"));

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
                "Forex1 quoteCurrency");
        when(forexService.findById(1L)).thenReturn(forex);

        ResponseEntity<Forex> response = forexController.findForexById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(forex, response.getBody());
    }

    @Test
    public void testFindForexBySymbol_Success() {
        Forex forex = new Forex("Forex1 Symbol",
                "Forex1 Description",
                "Forex1 Exchange",
                1L,
                1.0,
                1.0,
                1.0,
                1.0,
                1,
                "Forex1 baseCurrency",
                "Forex1 quoteCurrency");

        when(forexService.findBySymbol("Forex1 Symbol")).thenReturn(forex);

        ResponseEntity<Forex> responseEntity = forexController.findForexBySymbol("Forex1 Symbol");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(forex, responseEntity.getBody());
    }

    @Test
    public void testFindForexByIdException() {
        when(forexService.findById(0L)).thenThrow(new NotFoundException("Forex with id 0 not found"));
        ResponseEntity<Forex> response = forexController.findForexById(0L);
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
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
                "Forex1 quoteCurrency");
        List<Forex> forexes = new ArrayList<>();
        forexes.add(forex);
        when(forexService.findByBaseCurrency("Forex1 baseCurrency")).thenReturn(forexes);

        ResponseEntity<List<Forex>> response = forexController.findForexByBaseCurrency("Forex1 baseCurrency");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(forex, response.getBody().get(0));
    }

    @Test
    public void testFindBySymbol_Error() {
        when(forexService.findById(0l)).thenThrow(new NotFoundException("Forex not found"));
        ResponseEntity<Forex> response = forexController.findForexById(0L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
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
                "Forex1 quoteCurrency");
        List<Forex> forexes = new ArrayList<>();
        forexes.add(forex);
        when(forexService.findByQuoteCurrency("Forex1 quoteCurrency")).thenReturn(forexes);

        ResponseEntity<List<Forex>> response = forexController.findForexByQuoteCurrency("Forex1 quoteCurrency");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(forex, response.getBody().get(0));
    }

    @Test
    public void findCurrentStockPriceBySymbol_Success() {
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
                "Forex1 quoteCurrency");

        when(forexService.findCurrentPriceBySymbol("Forex1 Symbol"))
                .thenReturn(new SecuritiesPriceDto(forex.getPrice(), forex.getHigh(), forex.getLow()));

        ResponseEntity<SecuritiesPriceDto> respponse = forexController.findCurrentStockPriceBySymbol("Forex1 Symbol");
        SecuritiesPriceDto price = respponse.getBody();

        assertEquals(HttpStatus.OK, respponse.getStatusCode());
        assertEquals(forex.getPrice(), price.getPrice());
        assertEquals(forex.getHigh(), price.getHigh());
        assertEquals(forex.getLow(), price.getLow());
    }

    public void findCurrentStockPriceBySymbol_Error() {
        when(forexService.findCurrentPriceBySymbol("Invalid")).thenThrow(new NotFoundException(""));

        ResponseEntity<SecuritiesPriceDto> response = forexController.findCurrentStockPriceBySymbol("Invalid");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
