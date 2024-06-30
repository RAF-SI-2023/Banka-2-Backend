package rs.edu.raf.BankService.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rs.edu.raf.BankService.controller.ActionAgentProfitController;
import rs.edu.raf.BankService.data.dto.ActionAgentProfitDto;
import rs.edu.raf.BankService.data.dto.TotalActionAgentProfitDto;
import rs.edu.raf.BankService.service.ActionAgentProfitService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ActionAgentProfitControllerTests {

    @Mock
    private ActionAgentProfitService actionAgentProfitService;

    @InjectMocks
    private ActionAgentProfitController actionAgentProfitController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAll_Success() {
        // Arrange
        List<ActionAgentProfitDto> profits = Collections.singletonList(new ActionAgentProfitDto());

        when(actionAgentProfitService.getAllProfits()).thenReturn(profits);

        // Act
        ResponseEntity<?> response = actionAgentProfitController.getAll();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(profits, response.getBody());
    }

    @Test
    public void testGetAll_ExceptionThrown() {
        // Arrange
        when(actionAgentProfitService.getAllProfits()).thenThrow(new RuntimeException("Error"));

        // Act
        ResponseEntity<?> response = actionAgentProfitController.getAll();

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    public void testGetAgentsTotalProfit_Success() {
        // Arrange
        Double totalProfit = 1000.0;

        when(actionAgentProfitService.getAgentsTotalProfits()).thenReturn(totalProfit);

        // Act
        ResponseEntity<?> response = actionAgentProfitController.getAgentsTotalProfit();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(totalProfit, response.getBody());
    }

    @Test
    public void testGetAgentsTotalProfit_ExceptionThrown() {
        // Arrange
        when(actionAgentProfitService.getAgentsTotalProfits()).thenThrow(new RuntimeException("Error"));

        // Act
        ResponseEntity<?> response = actionAgentProfitController.getAgentsTotalProfit();

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    public void testGetAllTotalProfitsByEmail_Success() {
        // Arrange
        List<TotalActionAgentProfitDto> totalProfitsByUsers = Collections
                .singletonList(new TotalActionAgentProfitDto());

        when(actionAgentProfitService.getTotalProfitsByUsers()).thenReturn(totalProfitsByUsers);

        // Act
        ResponseEntity<?> response = actionAgentProfitController.getAllTotalProfitsByEmail();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(totalProfitsByUsers, response.getBody());
    }

    @Test
    public void testGetAllTotalProfitsByEmail_ExceptionThrown() {
        // Arrange
        when(actionAgentProfitService.getTotalProfitsByUsers()).thenThrow(new RuntimeException("Error"));

        // Act
        ResponseEntity<?> response = actionAgentProfitController.getAllTotalProfitsByEmail();

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }
}
