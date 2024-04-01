package rs.edu.raf.StockService;

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
    public void testFindAllOptions() {
        // Arrange
        List<Option> options = Collections.singletonList(new Option());
        when(optionService.findAll()).thenReturn(options);

        // Act
        ResponseEntity<List<Option>> responseEntity = optionController.findAllOptions();

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(options, responseEntity.getBody());
        verify(optionService, times(1)).findAll();

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

    @Test
    public void testFindOptionById() {
        // Arrange
        Long id = 1L;
        Option option = new Option();
        option.setId(id);
        when(optionService.findById(id)).thenReturn(option);

        // Act
        ResponseEntity<Option> responseEntity = optionController.findOptionById(id);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(option, responseEntity.getBody());
        verify(optionService, times(1)).findById(id);
    }

    @Test
    public void testFindOptionByStockListing() {
        // Arrange
        String stockListing = "STOCK";
        Option option = new Option();
        when(optionService.findByStockListing(stockListing)).thenReturn(option);

        // Act
       /* ResponseEntity<Option> responseEntity = optionController.find(stockListing);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(option, responseEntity.getBody());
        verify(optionService, times(1)).findByStockListing(stockListing);*/
    }
}
