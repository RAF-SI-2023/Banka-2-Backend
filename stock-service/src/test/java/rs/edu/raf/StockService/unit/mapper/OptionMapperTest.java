package rs.edu.raf.StockService.unit.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import rs.edu.raf.StockService.data.dto.OptionDto;
import rs.edu.raf.StockService.data.entities.Option;
import rs.edu.raf.StockService.data.enums.OptionType;
import rs.edu.raf.StockService.mapper.OptionMapper;

public class OptionMapperTest {

    private OptionMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new OptionMapper();
    }

    @Test
    public void optionToOptionDto_Success() {
        // Given
        Option option = new Option("AAPL", OptionType.CALL, 150.0, 0.25, 1000.0, System.currentTimeMillis());

        // When
        OptionDto dto = mapper.optionToOptionDto(option);

        // Then
        assertEquals("AAPL", dto.getStockListing());
        assertEquals(OptionType.CALL, dto.getOptionType());
        assertEquals(150.0, dto.getStrikePrice());
        assertEquals(0.25, dto.getImpliedVolatility());
        assertEquals(1000.0, dto.getOpenInterest());
        assertEquals(option.getSettlementDate(), dto.getSettlementDate());
    }

    @Test
    public void optionDtoToOption_Success() {
        // Given
        OptionDto dto = new OptionDto("AAPL", OptionType.CALL, 150.0, 0.25, 1000.0, System.currentTimeMillis());

        // When
        Option option = mapper.optionDtoToOption(dto);

        // Then
        assertEquals("AAPL", option.getStockListing());
        assertEquals(OptionType.CALL, option.getOptionType());
        assertEquals(150.0, option.getStrikePrice());
        assertEquals(0.25, option.getImpliedVolatility());
        assertEquals(1000.0, option.getOpenInterest());
        assertEquals(dto.getSettlementDate(), option.getSettlementDate());
    }
}
