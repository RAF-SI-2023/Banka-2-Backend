package rs.edu.raf.BankService.unit.profitTransactionsTests;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.BankService.mapper.BankProfitMapper;
import rs.edu.raf.BankService.repository.ActionAgentProfitRepository;
import rs.edu.raf.BankService.repository.BankProfitRepository;
import rs.edu.raf.BankService.repository.BankTransferTransactionDetailsRepository;
import rs.edu.raf.BankService.service.impl.BankProfitServiceImpl;
import org.junit.jupiter.api.Test;
import rs.edu.raf.BankService.data.dto.BankProfitDto;
import rs.edu.raf.BankService.data.entities.exchangeCurrency.BankTransferTransactionDetails;
import rs.edu.raf.BankService.data.entities.profit.ActionAgentProfit;
import rs.edu.raf.BankService.data.entities.profit.BankProfit;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class BankProfitServiceImplUnitTests {
    @Mock
    private ActionAgentProfitRepository actionAgentProfitRepository;
    @Mock
    private BankTransferTransactionDetailsRepository bankTransferTransactionDetailsRepository;
    @Mock
    private BankProfitRepository bankProfitRepository;
    @Mock
    private BankProfitMapper bankProfitMapper;

    @InjectMocks
    private BankProfitServiceImpl bankProfitService;


    @Test
    void getTotalProfit_shouldCalculateTotalProfitCorrectly() {
        // Arrange
        ActionAgentProfit actionAgentProfit = new ActionAgentProfit();
        actionAgentProfit.setProfit(100.0);

        BankTransferTransactionDetails bankTransferTransactionDetails = new BankTransferTransactionDetails();
        bankTransferTransactionDetails.setTotalProfit(200.0);

        BankProfit bankProfit = new BankProfit();
        BankProfitDto bankProfitDto = new BankProfitDto();

        when(actionAgentProfitRepository.findAll()).thenReturn(Collections.singletonList(actionAgentProfit));
        when(bankTransferTransactionDetailsRepository.findAll()).thenReturn(Collections.singletonList(bankTransferTransactionDetails));
        when(bankProfitRepository.findById(1L)).thenReturn(Optional.of(bankProfit));
        when(bankProfitMapper.bankProfitToBankProfitDto(any(BankProfit.class))).thenReturn(bankProfitDto);

        // Act
        BankProfitDto result = bankProfitService.getTotalProfit();

        // Assert
        assertEquals(bankProfitDto, result);
        verify(bankProfitRepository, times(1)).save(bankProfit);
        assertEquals(300.0, bankProfit.getProfit());
    }

    @Test
    void getTotalProfit_shouldHandleEmptyProfit() {
        // Arrange
        when(actionAgentProfitRepository.findAll()).thenReturn(Collections.emptyList());
        when(bankTransferTransactionDetailsRepository.findAll()).thenReturn(Collections.emptyList());
        when(bankProfitRepository.findById(1L)).thenReturn(Optional.empty());
        when(bankProfitMapper.bankProfitToBankProfitDto(any(BankProfit.class))).thenReturn(new BankProfitDto());

        // Act
        BankProfitDto result = bankProfitService.getTotalProfit();

        // Assert
        assertEquals(0.0, result.getProfit());
        verify(bankProfitRepository, times(1)).save(any(BankProfit.class));
    }
}
