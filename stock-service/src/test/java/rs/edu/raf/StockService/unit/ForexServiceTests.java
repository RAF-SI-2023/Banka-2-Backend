package rs.edu.raf.StockService.unit;

import org.checkerframework.checker.units.qual.t;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.webjars.NotFoundException;

import rs.edu.raf.StockService.data.dto.SecuritiesPriceDto;
import rs.edu.raf.StockService.data.entities.Forex;
import rs.edu.raf.StockService.repositories.ForexRepository;
import rs.edu.raf.StockService.services.impl.ForexServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ForexServiceTests {

        @Mock
        private ForexRepository forexRepository;

        @InjectMocks
        private ForexServiceImpl forexService;

        @BeforeEach
        public void setUp() {
                MockitoAnnotations.initMocks(this);
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
        }

        @Test
        public void testFindAll() {
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

                when(forexRepository.findAll()).thenReturn(forexes);

                List<Forex> result = forexService.findAll();

                assertEquals(2, result.size());
                assertEquals("Forex1 Symbol", result.get(0).getSymbol());
                assertEquals("Forex2 Symbol", result.get(1).getSymbol());
        }

        @Test
        public void testFindById() {
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

                when(forexRepository.findById(1L)).thenReturn(java.util.Optional.of(forex));

                Forex result = forexService.findById(1L);
                assertEquals(forex, result);
        }

        @Test
        public void testFindByBaseCurrency() {
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
                when(forexRepository.findForexesByBaseCurrency("Forex1 baseCurrency")).thenReturn(forexes);

                List<Forex> result = forexService.findByBaseCurrency("Forex1 baseCurrency");
                assertEquals(forex, result.get(0));
        }

        @Test
        public void testFindByQuoteCurrency() {
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
                when(forexRepository.findForexesByQuoteCurrency("Forex1 quoteCurrency")).thenReturn(forexes);

                List<Forex> result = forexService.findByQuoteCurrency("Forex1 quoteCurrency");
                assertEquals(forex, result.get(0));
        }

        @Test
        public void testFindBySymbol() {
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

                when(forexRepository.findForexBySymbol("Forex1 Symbol")).thenReturn(forex);

                Forex resultForex = forexService.findBySymbol("Forex1 Symbol");

                assertEquals(forex, resultForex);
        }

        @Test
        public void testFindCurrentPriceBySymbol() {
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

                SecuritiesPriceDto expected = new SecuritiesPriceDto(forex.getPrice(), forex.getHigh(), forex.getLow());
                when(forexRepository.findForexBySymbol("Forex1 Symbol")).thenReturn(forex);

                SecuritiesPriceDto actual = forexService.findCurrentPriceBySymbol("Forex1 Symbol");

                assertEquals(expected, actual);

        }

}
