package rs.edu.raf.StockService.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rs.edu.raf.StockService.controllers.FuturesContractController;
import rs.edu.raf.StockService.data.dto.FuturesContractDto;
import rs.edu.raf.StockService.services.FuturesContractService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class FuturesContractControllerTests {
    @Mock
    private FuturesContractService futuresContractService;
    @InjectMocks
    private FuturesContractController futuresContractController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAllFutureContracts() {
        // Mock behavior
        List<FuturesContractDto> contracts = new ArrayList<>();
        contracts.add(new FuturesContractDto(/* initialize contract here */));
        when(futuresContractService.findAll()).thenReturn(contracts);

        // Test
        ResponseEntity<?> response = futuresContractController.allFutureContracts();

        // Verify
        assertSame(HttpStatus.OK, response.getStatusCode());
        assertSame(contracts, response.getBody());
    }

    @Test
    public void testFindFutureContractById_ExistingId() {
        // Mock behavior
        Long id = 1L;
        FuturesContractDto contract = new FuturesContractDto(/* initialize contract here */);
        when(futuresContractService.findById(eq(id))).thenReturn(contract);

        // Test
        ResponseEntity<?> response = futuresContractController.findFutureContractById(id);

        // Verify
        assertSame(HttpStatus.OK, response.getStatusCode());
        assertSame(contract, response.getBody());
    }

    @Test
    public void testFindFutureContractById_NonExistingId() {
        // Mock behavior
        Long id = 1L;
        when(futuresContractService.findById(eq(id))).thenThrow(new RuntimeException("Futures contract with " + id + " not found"));

        // Test
        ResponseEntity<?> response = futuresContractController.findFutureContractById(id);

        // Verify
        assertSame(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
