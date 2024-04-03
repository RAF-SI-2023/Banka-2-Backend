package rs.edu.raf.StockService.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rs.edu.raf.StockService.data.dto.FuturesContractDto;
import rs.edu.raf.StockService.data.entities.FuturesContract;
import rs.edu.raf.StockService.data.enums.FuturesContractType;

import static org.junit.jupiter.api.Assertions.*;

class FuturesContractMapperTest {

    private FuturesContractMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new FuturesContractMapper();
    }

    @Test
    public void futuresContractToFuturesContractDto_Success() {
        // Given
        FuturesContract futuresContract = new FuturesContract(
                1L,
                "Name",
                "Code",
                100,
                "Unit",
                200,
                860379441,
                FuturesContractType.AGRICULTURE
        );

        // When
        FuturesContractDto futuresContractDto = mapper.futuresContractToFuturesContractDto(futuresContract);

        // Then
        assertEquals(1L, futuresContractDto.getId());
        assertEquals("Name", futuresContractDto.getName());
        assertEquals("Code", futuresContractDto.getCode());
        assertEquals(100, futuresContractDto.getContractSize());
        assertEquals("Unit", futuresContractDto.getContractUnit());
        assertEquals(200, futuresContractDto.getOpenInterest());
        assertEquals(860379441, futuresContractDto.getSettlementDate());
        assertEquals(FuturesContractType.AGRICULTURE, futuresContractDto.getType());
    }

    @Test
    public void futuresContractDtoToFuturesContract_Success() {
        // Given
        FuturesContractDto futuresContractDto = new FuturesContractDto(
                1L,
                "Name",
                "Code",
                100,
                "Unit",
                200,
                860379441,
                FuturesContractType.AGRICULTURE
        );

        // When
        FuturesContract futuresContract = mapper.futuresContractDtoToFuturesContract(futuresContractDto);

        // Then
        assertEquals(1L, futuresContract.getId());
        assertEquals("Name", futuresContract.getName());
        assertEquals("Code", futuresContract.getCode());
        assertEquals(100, futuresContract.getContractSize());
        assertEquals("Unit", futuresContract.getContractUnit());
        assertEquals(200, futuresContract.getOpenInterest());
        assertEquals(860379441, futuresContract.getSettlementDate());
        assertEquals(FuturesContractType.AGRICULTURE, futuresContract.getType());
    }
}