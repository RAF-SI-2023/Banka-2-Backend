package rs.edu.raf.StockService.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.webjars.NotFoundException;
import rs.edu.raf.StockService.controllers.ExchangeController;
import rs.edu.raf.StockService.data.entities.Exchange;
import rs.edu.raf.StockService.services.impl.InMemoryExchangeServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ExchangeControllerTests {

    @Mock
    private InMemoryExchangeServiceImpl exchangeService;
    @InjectMocks
    private ExchangeController exchangeController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAllExchanges() {
        List<Exchange> exchanges = new ArrayList<>();
        exchanges.add(new Exchange(
                1L,
                "Jakarta Futures Exchange (bursa Berjangka Jakarta)",
                "BBJ",
                "XBBJ",
                "Indonesia",
                "Indonesian Rupiah",
                7
        ));
        exchanges.add(new Exchange(
                2L,
                "Asx - Trade24",
                "SFE",
                "XSFE",
                "Australia",
                "Australian Dollar",
                12
        ));

        exchanges.add(new Exchange(
                3L,
                "Cboe Edga U.s. Equities Exchange Dark",
                "EDGADARK",
                "EDGD",
                "United States",
                "United States Dollar",
                -7
        ));
        when(exchangeService.findAll()).thenReturn(exchanges);

        ResponseEntity<List<Exchange>> response = exchangeController.findAllExchanges();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3, response.getBody().size());
    }

    @Test
    public void testFindExchangeById() {
        Exchange exchange = new Exchange(
                1L,
                "Jakarta Futures Exchange (bursa Berjangka Jakarta)",
                "BBJ",
                "XBBJ",
                "Indonesia",
                "Indonesian Rupiah",
                7
        );
        when(exchangeService.findById(1L)).thenReturn(exchange);

        ResponseEntity<Exchange> response = exchangeController.findExchangeById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(exchange, response.getBody());
    }

    @Test
    public void testFindExchangeByIdFail() {
        when(exchangeService.findById(1231L)).thenThrow(new NotFoundException("Exchange with id: 1231 not found"));
        ResponseEntity<Exchange> response = exchangeController.findExchangeById(1231L);
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testFindExchangeByExchangeName() {
        Exchange exchange = new Exchange(
                1L,
                "Jakarta Futures Exchange (bursa Berjangka Jakarta)",
                "BBJ",
                "XBBJ",
                "Indonesia",
                "Indonesian Rupiah",
                7
        );
        when(exchangeService.findByExchangeName("Jakarta Futures Exchange (bursa Berjangka Jakarta)")).thenReturn(exchange);
        ResponseEntity<Exchange> response = exchangeController.findExchangeByName("Jakarta Futures Exchange (bursa Berjangka Jakarta)");
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(exchange, response.getBody());
    }

    @Test
    public void testFindExchangeByExchangeNameException() {

        when(exchangeService.findByExchangeName("some name that doesnt exist")).thenThrow(new NotFoundException("Exchange with name: some name that doesnt exist not found"));
        ResponseEntity<Exchange> response = exchangeController.findExchangeByName("some name that doesnt exist");
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    public void testFindExchangeByMiCode() {
        Exchange exchange = new Exchange(
                1L,
                "Jakarta Futures Exchange (bursa Berjangka Jakarta)",
                "BBJ",
                "XBBJ",
                "Indonesia",
                "Indonesian Rupiah",
                7
        );
        when(exchangeService.findByMICode("XBBJ")).thenReturn(exchange);

        ResponseEntity<Exchange> response = exchangeController.findExchangeByMiCode("XBBJ");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(exchange, response.getBody());
    }

    @Test
    public void testFindExchangeByMiCodeException() {

        when(exchangeService.findByMICode("AAAAAA")).thenThrow(new NotFoundException("Exchange with miCode: AAAAAA not found"));

        ResponseEntity<Exchange> response = exchangeController.findExchangeByMiCode("AAAAAA");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
       
    }
}
