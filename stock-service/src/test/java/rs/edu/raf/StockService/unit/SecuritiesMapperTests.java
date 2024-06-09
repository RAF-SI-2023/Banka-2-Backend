package rs.edu.raf.StockService.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rs.edu.raf.StockService.data.dto.FuturesContractDto;
import rs.edu.raf.StockService.data.dto.SecuritiesDto;
import rs.edu.raf.StockService.data.entities.Option;
import rs.edu.raf.StockService.mapper.SecuritiesMapper;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class SecuritiesMapperTests {

    @InjectMocks
    private SecuritiesMapper securitiesMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should convert FuturesContractDto to SecuritiesDto correctly when all fields are set")
    public void shouldConvertFuturesContractDtoToSecuritiesDtoCorrectlyWhenAllFieldsAreSet() {
        long futureDate = Instant.now().plus(Duration.ofDays(365)).toEpochMilli();

        FuturesContractDto futuresContractDto = new FuturesContractDto();
        futuresContractDto.setId(1L);
        futuresContractDto.setName("Test Futures Contract");
        futuresContractDto.setSettlementDate(futureDate);
        futuresContractDto.setFuturesContractPrice(100.0);

        SecuritiesDto securitiesDto = securitiesMapper.convertFuturesContractToSecuritiesDto(futuresContractDto);

        assertEquals(1L, securitiesDto.getId());
        assertEquals("Test Futures Contract", securitiesDto.getName());
        assertEquals("Futures Contract", securitiesDto.getType());
        assertEquals(futureDate, securitiesDto.getSettlementDate());
        assertEquals(100.0, securitiesDto.getPrice());
    }

    @Test
    @DisplayName("Should convert Option to SecuritiesDto correctly when all fields are set")
    public void shouldConvertOptionToSecuritiesDtoCorrectlyWhenAllFieldsAreSet() {
        long futureDate = Instant.now().plus(Duration.ofDays(365)).toEpochMilli();

        Option option = new Option();
        option.setId(1L);
        option.setStockListing("Test Option");
        option.setSettlementDate(futureDate);
        option.setStrikePrice(100.0);

        SecuritiesDto securitiesDto = securitiesMapper.convertOptionToSecuritiesDto(option);

        assertEquals(1L, securitiesDto.getId());
        assertEquals("Test Option", securitiesDto.getName());
        assertEquals("Option", securitiesDto.getType());
        assertEquals(futureDate, securitiesDto.getSettlementDate());
        assertEquals(100.0, securitiesDto.getPrice());
    }
}