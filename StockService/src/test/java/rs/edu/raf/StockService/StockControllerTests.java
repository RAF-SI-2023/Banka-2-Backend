package rs.edu.raf.StockService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rs.edu.raf.StockService.controllers.StockController;
import rs.edu.raf.StockService.data.entities.Stock;
import rs.edu.raf.StockService.services.impl.StockServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class StockControllerTests {

    @Mock
    private StockServiceImpl stockService;
    @InjectMocks
    private StockController stockController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAllStocks() {
        List<Stock> stocks = new ArrayList<>();

        stocks.add(new Stock(
                "Stock1 Symbol",
                "Stock1 Description",
                "Stock1 Exchange",
                1L,
                1.0,
                1.0,
                1.0,
                1.0,
                1,
                1,
                1.0
        ));
        stocks.add(new Stock(
                "Stock2 Symbol",
                "Stock2 Description",
                "Stock2 Exchange",
                2L,
                2.0,
                2.0,
                2.0,
                2.0,
                2,
                2,
                2.0
        ));

        when(stockService.findAll()).thenReturn(stocks);

        ResponseEntity<List<Stock>> response = stockController.findAllStocks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testFindStockById() {
        Stock stock = new Stock(
                "Stock1 Symbol",
                "Stock1 Description",
                "Stock1 Exchange",
                1L,
                1.0,
                1.0,
                1.0,
                1.0,
                1,
                1,
                1.0
        );
        when(stockService.findById(1L)).thenReturn(stock);

        ResponseEntity<Stock> response = stockController.findStockById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(stock, response.getBody());
    }

    @Test
    public void testFindStockBySymbol() {
        Stock stock = new Stock(
                "Stock1 Symbol",
                "Stock1 Description",
                "Stock1 Exchange",
                1L,
                1.0,
                1.0,
                1.0,
                1.0,
                1,
                1,
                1.0
        );
        when(stockService.findBySymbol("Stock1 Symbol")).thenReturn(stock);

        ResponseEntity<Stock> response = stockController.findStockBySymbol("Stock1 Symbol");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(stock, response.getBody());
    }
}
