package rs.edu.raf.StockService.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rs.edu.raf.StockService.data.entities.Stock;
import rs.edu.raf.StockService.repositories.StockRepository;
import rs.edu.raf.StockService.services.impl.StockServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class StockServiceTests {
        @Mock
        private StockRepository stockRepository;

        @InjectMocks
        private StockServiceImpl stockService;

        @BeforeEach
        public void setUp() {
                MockitoAnnotations.openMocks(this);
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
                                1.0));
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
                                2.0));
        }

        @Test
        public void testFindAll() {
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
                                1.0));
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
                                2.0));

                when(stockRepository.findAll()).thenReturn(stocks);

                List<Stock> result = stockService.findAll();

                assertEquals(2, result.size());
                assertEquals("Stock1 Symbol", result.get(0).getSymbol());
                assertEquals("Stock2 Symbol", result.get(1).getSymbol());
        }

        @Test
        public void testFindById() {
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
                                1.0);

                when(stockRepository.findById(1L)).thenReturn(java.util.Optional.of(stock));

                Stock result = stockService.findById(1L);
                assertEquals(stock, result);
        }

        @Test
        public void testFindByStockSymbol() {
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
                                1.0);
                List<Stock> stocks = new ArrayList<>();
                stocks.add(stock);
                when(stockRepository.findStocksBySymbol("Stock1 Symbol")).thenReturn(stocks);

                List<Stock> result = stockService.findBySymbolDEPRICATED("Stock1 Symbol");
                assertEquals(stock, result.get(0));
        }
}
