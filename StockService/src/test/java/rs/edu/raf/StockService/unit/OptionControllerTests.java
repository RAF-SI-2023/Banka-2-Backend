package rs.edu.raf.StockService.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rs.edu.raf.StockService.controllers.OptionController;
import rs.edu.raf.StockService.data.entities.Option;
import rs.edu.raf.StockService.services.impl.OptionServiceImpl;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class OptionControllerTests {

    @Mock
    private OptionServiceImpl optionService;

    @InjectMocks
    private OptionController optionController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testFindAllOptionsByStockListing() {
        // Arrange
        String stockListing = "STOCK";
        List<Option> options = Collections.singletonList(new Option());
        when(optionService.findAllByStockListing(stockListing)).thenReturn(options);

        // Act
        ResponseEntity<List<Option>> responseEntity = optionController.findAllOptionsByStockListing(stockListing);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(options, responseEntity.getBody());
        verify(optionService, times(1)).findAllByStockListing(stockListing);
    }


}
