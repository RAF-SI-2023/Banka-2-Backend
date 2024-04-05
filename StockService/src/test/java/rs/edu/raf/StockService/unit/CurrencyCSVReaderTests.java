package rs.edu.raf.StockService.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ResourceLoader;
import rs.edu.raf.StockService.bootstrap.readers.CurrencyCsvReader;
import rs.edu.raf.StockService.data.entities.Currency;
import rs.edu.raf.StockService.data.entities.CurrencyInflation;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CurrencyCSVReaderTests {

    @Mock
    CurrencyCsvReader currencyCsvReader;
    @Mock
    private HttpClient httpClient;

    @Mock
    private HttpResponse<String> httpResponse;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void testReadCurrencyFromCsv() {
        // Mock the BufferedReader
        Mockito.clearAllCaches();

        List<Currency> currencyList = null;

        try {
            BufferedReader br = mock(BufferedReader.class);
            currencyCsvReader.setBufferedReader(br);
            when(br.readLine()).thenReturn("CurrencyCode,CurrencyName", "USD,US Dollar", "EUR,Euro", null);

            currencyList = currencyCsvReader.readCurrencyFromCsv();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        // Assertions
        assertEquals(2, currencyList.size());
        assertEquals("USD", currencyList.get(0).getCurrencyCode());
        assertEquals("US Dollar", currencyList.get(0).getCurrencyName());
        assertEquals("EUR", currencyList.get(1).getCurrencyCode());
        assertEquals("Euro", currencyList.get(1).getCurrencyName());


    }

    @Test
    public void testLoadCurrencyData() {
        // Mock the currency list from CSV
        List<Currency> currencyList = List.of(
                new Currency(1L, "US dollar", "USD", "$", "USA", null),
                new Currency(2L, "Euro", "EUR", "€", "EU", null)
        );


        // Set up the expected behavior
        when(currencyCsvReader.loadCurrencyData()).thenReturn(currencyList);

        // Call the method under test
        List<Currency> result = currencyCsvReader.loadCurrencyData();
        // Assertions
        assertEquals(2, result.size());
        assertEquals("USD", result.get(0).getCurrencyCode());
        assertEquals("US dollar", result.get(0).getCurrencyName());
        assertEquals("EUR", result.get(1).getCurrencyCode());
        assertEquals("Euro", result.get(1).getCurrencyName());
        assertEquals("$", result.get(0).getCurrencySymbol());
        assertEquals("€", result.get(1).getCurrencySymbol());


    }

    @Test
    public void testPullCurrencyInflationData() throws IOException, InterruptedException {
        // Initialize mocks


        // Mock response from API
        String jsonResponse = "{ \"values\": { \"PCPIPCH\": { \"USA\": { \"2022\": 5.2, \"2023\": 3.6 } } } }";
        when(httpResponse.body()).thenReturn(jsonResponse);
        mockStatic(HttpClient.class);
        when(HttpClient.newHttpClient()).thenReturn(httpClient); // Assuming httpClient is a mock of HttpClient
        when(httpClient.send(Mockito.any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any())).thenReturn(httpResponse);
        // Prepare test data

        List<Currency> currencyList = List.of(
                new Currency(1L, "US dollar", "USD", "$", "USA", null),
                new Currency(2L, "Euro", "EUR", "€", "EU", null)
        );


        // Call the method under test
        CurrencyCsvReader currencyCsvReader = new CurrencyCsvReader(Mockito.mock(ResourceLoader.class));
        List<CurrencyInflation> result = currencyCsvReader.pullCurrencyInflationData(currencyList);

        // Assertions
        assertEquals(2, result.size());
        // Assuming the inflation values are properly mapped to CurrencyInflation objects
        assertEquals(5.2, result.get(0).getInflationRate());
        assertEquals(3.6, result.get(1).getInflationRate());
        // Assuming other assertions for currency ID, etc.
    }
}





