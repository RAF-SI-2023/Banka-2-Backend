package rs.edu.raf.StockService.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rs.edu.raf.StockService.data.entities.Exchange;
import rs.edu.raf.StockService.repositories.ExchangeRepository;
import rs.edu.raf.StockService.services.impl.ExchangeServiceImpl;
import rs.edu.raf.StockService.services.impl.InMemoryExchangeServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ExchangeServiceTests {

    @Mock
    private ExchangeRepository exchangeRepository;

    @InjectMocks
    private ExchangeServiceImpl exchangeService;

    @Mock
    private InMemoryExchangeServiceImpl inMemoryExchangeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        inMemoryExchangeService = new InMemoryExchangeServiceImpl();
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

        inMemoryExchangeService.setExchangeList(exchanges);
    }

    @Test
    public void testFindAll() {
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


        when(exchangeRepository.findAll()).thenReturn(exchanges);


        List<Exchange> result = exchangeService.findAll();
        List<Exchange> resultInMemory = inMemoryExchangeService.findAll();

        assertEquals(3, result.size());
        assertEquals("XBBJ", result.get(0).getExchangeMICode());
        assertEquals("XSFE", result.get(1).getExchangeMICode());
        assertEquals("EDGD", result.get(2).getExchangeMICode());

        assertEquals(3, resultInMemory.size());
        assertEquals("XBBJ", resultInMemory.get(0).getExchangeMICode());
        assertEquals("XSFE", resultInMemory.get(1).getExchangeMICode());
        assertEquals("EDGD", resultInMemory.get(2).getExchangeMICode());
    }

    @Test
    public void testFindById() {
        Exchange exchange = new Exchange(
                1L,
                "Jakarta Futures Exchange (bursa Berjangka Jakarta)",
                "BBJ",
                "XBBJ",
                "Indonesia",
                "Indonesian Rupiah",
                7
        );

        when(exchangeRepository.findById(1L)).thenReturn(java.util.Optional.of(exchange));

        Exchange result = exchangeService.findById(1L);
        assertEquals(exchange, result);


        Exchange resultInMemory = inMemoryExchangeService.findById(1L);
        assertEquals("XBBJ", resultInMemory.getExchangeMICode());
    }

    @Test
    public void testFindByMICode() {
        Exchange exchange = new Exchange(
                1L,
                "Jakarta Futures Exchange (bursa Berjangka Jakarta)",
                "BBJ",
                "XBBJ",
                "Indonesia",
                "Indonesian Rupiah",
                7
        );

        when(exchangeRepository.findByExchangeMICode("XBBJ")).thenReturn(exchange);


        Exchange result = exchangeService.findByMICode("XBBJ");
        assertEquals(exchange, result);

        Exchange resultInMemory = inMemoryExchangeService.findByMICode("XBBJ");
        assertEquals("XBBJ", resultInMemory.getExchangeMICode());
    }

    @Test
    public void testFindByExchangeName() {
        Exchange exchange = new Exchange(
                1L,
                "Jakarta Futures Exchange (bursa Berjangka Jakarta)",
                "BBJ",
                "XBBJ",
                "Indonesia",
                "Indonesian Rupiah",
                7
        );

        when(exchangeRepository.findByExchangeName("Jakarta Futures Exchange (bursa Berjangka Jakarta)")).thenReturn(exchange);


        Exchange result = exchangeService.findByExchangeName("Jakarta Futures Exchange (bursa Berjangka Jakarta)");
        assertEquals(exchange, result);

        Exchange resultInMemory = inMemoryExchangeService.findByExchangeName("Jakarta Futures Exchange (bursa Berjangka Jakarta)");
        // Assert
        assertEquals("Jakarta Futures Exchange (bursa Berjangka Jakarta)", resultInMemory.getExchangeName());
    }

}
