package rs.edu.raf.StockService.unit;

import org.junit.jupiter.api.Assertions;
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
import java.util.*;

import static org.mockito.Mockito.when;

public class SecuritiesServiceImplTests {
    @InjectMocks
    private SecuritiesController securitiesController;

    @Mock
    private SecuritiesService securitiesService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should return empty list when no securities are found")
    public void shouldReturnEmptyListWhenNoSecuritiesFound() {
        LocalDate futureDate = LocalDate.of(2023, 12, 31);
        when(securitiesService.getSecuritiesBySettlementDate(Optional.of(futureDate)))
                .thenReturn(Collections.emptyList());

        ResponseEntity<List<SecuritiesDto>> response = securitiesController.getSecuritiesBySettlementDate(futureDate);

        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertEquals(0, Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    @DisplayName("Should return multiple securities when multiple securities are found")
    public void shouldReturnMultipleSecuritiesWhenMultipleSecuritiesFound() {
        LocalDate futureDate = LocalDate.of(2023, 12, 31);
        SecuritiesDto securitiesDto1 = new SecuritiesDto();
        SecuritiesDto securitiesDto2 = new SecuritiesDto();
        when(securitiesService.getSecuritiesBySettlementDate(Optional.of(futureDate)))
                .thenReturn(Arrays.asList(securitiesDto1, securitiesDto2));

        ResponseEntity<List<SecuritiesDto>> response = securitiesController.getSecuritiesBySettlementDate(futureDate);

        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertEquals(2, Objects.requireNonNull(response.getBody()).size());
    }
}
