package rs.edu.raf.StockService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import rs.edu.raf.StockService.bootstrap.readers.ExchangeCsvReader;
import rs.edu.raf.StockService.data.entities.Exchange;

import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ExchangeCSVReaderTests {

    private ExchangeCsvReader exchangeCsvReader;

    @BeforeEach
    void setUp() {
        exchangeCsvReader = new ExchangeCsvReader();
    }

    @Test
    void testLoadExchangeCsv() throws IOException {
        Mockito.clearAllCaches();
        List<Exchange> exchanges = null;

        try {
            BufferedReader br = mock(BufferedReader.class);
            exchangeCsvReader.setReader(br);
            when(br.readLine()).thenReturn(
                    "Exchange Name,Exchange Acronym,Exchange Mic Code,Country,Currency,Time Zone,Open Time,Close Time",
                    "Jakarta Futures Exchange (bursa Berjangka Jakarta),BBJ,XBBJ,Indonesia,Indonesian Rupiah,Asia/Jakarta, 09:00, 17:30",
                    null);

            exchanges = exchangeCsvReader.loadExchangeCsv();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        assertEquals(1, exchanges.size());
        assertEquals("Jakarta Futures Exchange (bursa Berjangka Jakarta)", exchanges.get(0).getExchangeName());
        assertEquals("BBJ", exchanges.get(0).getExchangeAcronym());
        assertEquals("XBBJ", exchanges.get(0).getExchangeMICode());
        assertEquals("Indonesia", exchanges.get(0).getPolity());
    }

}
