package rs.edu.raf.StockService.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import rs.edu.raf.StockService.controllers.SecuritiesController;
import rs.edu.raf.StockService.data.dto.SecuritiesDto;
import rs.edu.raf.StockService.services.SecuritiesService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class SecuritiesControllerTest {

    @InjectMocks
    private SecuritiesController securitiesController;

    @Mock
    private SecuritiesService securitiesService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should return securities when future date is provided")
    public void shouldReturnSecuritiesWhenFutureDateIsProvided() {
        LocalDate futureDate = LocalDate.of(2023, 12, 31);
        SecuritiesDto securitiesDto = new SecuritiesDto();
        when(securitiesService.getSecuritiesBySettlementDate(Optional.of(futureDate)))
                .thenReturn(Collections.singletonList(securitiesDto));

        ResponseEntity<List<SecuritiesDto>> response = securitiesController.getSecuritiesBySettlementDate(futureDate);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @DisplayName("Should return securities when future date is not provided")
    public void shouldReturnSecuritiesWhenFutureDateIsNotProvided() {
        SecuritiesDto securitiesDto = new SecuritiesDto();
        when(securitiesService.getSecuritiesBySettlementDate(Optional.empty()))
                .thenReturn(Collections.singletonList(securitiesDto));

        ResponseEntity<List<SecuritiesDto>> response = securitiesController.getSecuritiesBySettlementDate(null);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }
}