package rs.edu.raf.StockService.unit.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import rs.edu.raf.StockService.data.dto.FuturesContractDto;
import rs.edu.raf.StockService.data.dto.SecuritiesDto;
import rs.edu.raf.StockService.data.entities.Option;
import rs.edu.raf.StockService.data.enums.FuturesContractType;
import rs.edu.raf.StockService.data.enums.OptionType;
import rs.edu.raf.StockService.mapper.SecuritiesMapper;

public class SecuritiesMapperTest {

    private SecuritiesMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new SecuritiesMapper();
    }

    @Test
    public void convertFuturesContractToSecuritiesDto_Success() {
        // Given
        FuturesContractDto futuresContractDto = new FuturesContractDto(1L, "Oil Futures", "OIL2024", 1000, "Barrels",
                1500, 1682552399000L, 100, FuturesContractType.AGRICULTURE, 95.5, 60.0);

        // When
        SecuritiesDto dto = mapper.convertFuturesContractToSecuritiesDto(futuresContractDto);

        // Then
        assertEquals(1L, dto.getId());
        assertEquals("Oil Futures", dto.getName());
        assertEquals("Futures Contract", dto.getType());
        assertEquals(1682552399000L, dto.getSettlementDate());
        assertEquals(95.5, dto.getPrice());
    }

    @Test
    public void convertOptionToSecuritiesDto_Success() {
        // Given
        Option option = new Option(1L, "AAPL", OptionType.CALL, 150.0, 0.25, 1000.0, 1682552399000L);

        // When
        SecuritiesDto dto = mapper.convertOptionToSecuritiesDto(option);

        // Then
        assertEquals(1L, dto.getId());
        assertEquals("AAPL", dto.getName());
        assertEquals("Option", dto.getType());
        assertEquals(1682552399000L, dto.getSettlementDate());
        assertEquals(150.0, dto.getPrice());
    }
}
