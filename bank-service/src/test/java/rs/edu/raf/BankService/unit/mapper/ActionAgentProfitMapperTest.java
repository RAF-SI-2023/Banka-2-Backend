package rs.edu.raf.BankService.unit.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rs.edu.raf.BankService.data.dto.ActionAgentProfitDto;
import rs.edu.raf.BankService.data.entities.profit.ActionAgentProfit;
import rs.edu.raf.BankService.data.enums.TransactionProfitType;
import rs.edu.raf.BankService.mapper.ActionAgentProfitMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActionAgentProfitMapperTest {

    private ActionAgentProfitMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new ActionAgentProfitMapper();
    }

    @Test
    public void actionAgentProfitToActionAgentProfitDto_Success() {
        // Given
        ActionAgentProfit profit = new ActionAgentProfit();
        profit.setId(1L);
        profit.setUserEmail("test@example.com");
        profit.setProfit(100.0);
        profit.setTransactionType(TransactionProfitType.ORDER);
        profit.setTransactionId(123L);
        profit.setCreatedAt(System.currentTimeMillis());

        // When
        ActionAgentProfitDto dto = mapper.actionAgentProfitToActionAgentProfitDto(profit);

        // Then
        assertEquals(profit.getId(), dto.getId());
        assertEquals(profit.getUserEmail(), dto.getUserEmail());
        assertEquals(profit.getProfit(), dto.getProfit());
        assertEquals(profit.getTransactionType(), dto.getTransactionType());
        assertEquals(profit.getTransactionId(), dto.getTransactionId());
        assertEquals(profit.getCreatedAt(), dto.getCreatedAt());
    }

    @Test
    public void actionAgentProfitDtoToActionAgentProfit_Success() {
        // Given
        ActionAgentProfitDto dto = new ActionAgentProfitDto();
        dto.setId(1L);
        dto.setUserEmail("test@example.com");
        dto.setProfit(100.0);
        dto.setTransactionType(TransactionProfitType.SECURITIES);
        dto.setTransactionId(123L);
        dto.setCreatedAt(System.currentTimeMillis());

        // When
        ActionAgentProfit profit = mapper.actionAgentProfitDtoToActionAgentProfit(dto);

        // Then
        assertEquals(dto.getId(), profit.getId());
        assertEquals(dto.getUserEmail(), profit.getUserEmail());
        assertEquals(dto.getProfit(), profit.getProfit());
        assertEquals(dto.getTransactionType(), profit.getTransactionType());
        assertEquals(dto.getTransactionId(), profit.getTransactionId());
        assertEquals(dto.getCreatedAt(), profit.getCreatedAt());
    }
}
