package rs.edu.raf.StockService.unit;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.webjars.NotFoundException;
import rs.edu.raf.StockService.data.entities.Option;
import rs.edu.raf.StockService.mapper.OptionMapper;
import rs.edu.raf.StockService.repositories.OptionRepository;
import rs.edu.raf.StockService.services.OptionService;
import rs.edu.raf.StockService.services.impl.OptionServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

public class OptionServiceImplTests {

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private OptionMapper optionMapper;
    @InjectMocks
    private OptionServiceImpl optionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testFindAll() {
        // Mock behavior
        List<Option> options = new ArrayList<>();
        when(optionRepository.findAll()).thenReturn(options);

        // Test
        List<Option> result = optionService.findAll();

        // Verify
        assertEquals(options, result);
    }

    @Test
    public void testFindAllByStockListing() {

        // Mock behavior
        /** videti da se ovo nahakuje,//TODO  @PetarRandjelovic*/
 /*

 List<Option> options = new ArrayList<>();
        String stockListing = "XYZ";
        when(optionService.loadOptions(stockListing)).thenReturn(options);

        // Test
        List<Option> result = optionService.findAllByStockListing(stockListing);

        // Verify
        assertEquals(options, result);*/
    }

    @Test
    public void testFindById_ExistingId() {
        // Mock behavior
        Long id = 1L;
        Option option = new Option(/* initialize option here */);
        when(optionRepository.findById(eq(id))).thenReturn(Optional.of(option));

        // Test
        Option result = optionService.findById(id);

        // Verify
        assertEquals(option, result);
    }

    @Test
    public void testFindById_NonExistingId() {
        // Mock behavior
        Long id = 1L;
        when(optionRepository.findById(eq(id))).thenReturn(Optional.empty());

        // Test
        // Expect NotFoundException
        try {
            optionService.findById(id);
        } catch (NotFoundException e) {
            // Verify
            assertEquals("Option with id: 1 not found.", e.getMessage());

        }
    }

    // You can write more tests for other methods in OptionServiceImpl if needed
}
