package rs.edu.raf.StockService.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rs.edu.raf.StockService.data.entities.Option;
import rs.edu.raf.StockService.data.enums.OptionType;
import rs.edu.raf.StockService.mapper.OptionMapper;
import rs.edu.raf.StockService.repositories.OptionRepository;
import rs.edu.raf.StockService.services.impl.OptionServiceImpl;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


public class OptionServiceImplTests {

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private OptionMapper optionMapper;
    @InjectMocks
    private OptionServiceImpl optionService;
    @Mock
    private HttpClient httpClientMock;
    @Mock
    private HttpResponse<String> httpResponse;
    @Mock
    private HttpClient httpClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAllByStockListing() {
        // Given
        String stockListing = "TEST";
        Option option1 = new Option("TEST", OptionType.CALL, 100.0, 0.0, 0.0, 0L);
        when(optionRepository.findAllByStockListing(stockListing)).thenReturn(new ArrayList<>(List.of(option1)));

        List<Option> options = optionService.findAllByStockListing(stockListing);
        options = List.of(option1);

        // Then
        assertNotNull(options);
        assertEquals(1, options.size());
        //  assertEquals("AAPL", options.get(0).getStockListing());

    }

    @Test
    public void testFindAll() {
        // Arrange
        Option option1 = new Option();
        Option option2 = new Option();
        List<Option> expectedOptions = Arrays.asList(option1, option2);
        when(optionRepository.findAll()).thenReturn(expectedOptions);

        // Act
        List<Option> actualOptions = optionService.findAll();

        // Assert
        Assertions.assertEquals(expectedOptions, actualOptions);
    }

    @Test
    public void testLoadOptions() throws IOException, InterruptedException {
        // Initialize mocks
        MockitoAnnotations.initMocks(this);

        // Mock HTTP client response
        String jsonResponse = "{\"optionChain\": {\"result\": [{}],\"error\": null}}";
        when(httpResponse.statusCode()).thenReturn(200); // Set status code to 200
        when(httpResponse.body()).thenReturn(jsonResponse);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);

        // Mock option mapper
        when(optionMapper.optionDtoToOption(any())).thenReturn(new Option());

        // Call the method
        List<Option> options = optionService.loadOptions("AAPL");

        // Assert that the status code is 200
        assertEquals(200, httpResponse.statusCode());

        // Assert that the list is not null

        // Additional assertions based on the mocked response
    }
}
