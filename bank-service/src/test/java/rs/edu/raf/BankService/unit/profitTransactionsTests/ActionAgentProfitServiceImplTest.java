package rs.edu.raf.BankService.unit.profitTransactionsTests;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rs.edu.raf.BankService.data.dto.ActionAgentProfitDto;
import rs.edu.raf.BankService.data.dto.GenericTransactionDto;
import rs.edu.raf.BankService.data.dto.TotalActionAgentProfitDto;
import rs.edu.raf.BankService.data.entities.SecuritiesOwnership;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.data.entities.profit.ActionAgentProfit;
import rs.edu.raf.BankService.data.entities.transactions.OrderTransaction;
import rs.edu.raf.BankService.data.entities.transactions.SecuritiesTransaction;
import rs.edu.raf.BankService.data.enums.TransactionProfitType;
import rs.edu.raf.BankService.mapper.ActionAgentProfitMapper;
import rs.edu.raf.BankService.mapper.TransactionMapper;
import rs.edu.raf.BankService.repository.ActionAgentProfitRepository;
import rs.edu.raf.BankService.repository.CashAccountRepository;
import rs.edu.raf.BankService.service.impl.ActionAgentProfitServiceImpl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ActionAgentProfitServiceImplTest {

    @Mock
    private ActionAgentProfitRepository actionAgentProfitRepository;

    @Mock
    private ActionAgentProfitMapper actionAgentProfitMapper;

    @Mock
    private CashAccountRepository cashAccountRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private ActionAgentProfitServiceImpl actionAgentProfitService;


    @Test
    void createAgentProfit_shouldCreateProfitForSecuritiesTransaction() {
        // Arrange
        SecuritiesTransaction securitiesTransaction = new SecuritiesTransaction();
        securitiesTransaction.setSenderCashAccount(new CashAccount());
        securitiesTransaction.getSenderCashAccount().setEmail("test@example.com");
        securitiesTransaction.setAmount(1000.0);
        securitiesTransaction.setCreatedAt(LocalDateTime.now());

        SecuritiesOwnership securitiesOwnership = new SecuritiesOwnership();
        securitiesOwnership.setAverageBuyingPrice(800.0);

        ActionAgentProfit actionAgentProfit = new ActionAgentProfit();
        actionAgentProfit.setUserEmail("test@example.com");
        actionAgentProfit.setProfit(200.0);

        GenericTransactionDto genericTransactionDto = new GenericTransactionDto();
        genericTransactionDto.setId("1");

        when(transactionMapper.toGenericTransactionDto(securitiesTransaction)).thenReturn(genericTransactionDto);
        when(actionAgentProfitRepository.save(any(ActionAgentProfit.class))).thenReturn(actionAgentProfit);
        when(actionAgentProfitMapper.actionAgentProfitToActionAgentProfitDto(any(ActionAgentProfit.class)))
                .thenReturn(new ActionAgentProfitDto());

        // Act
        ActionAgentProfitDto result = actionAgentProfitService.createAgentProfit(securitiesTransaction, securitiesOwnership, 1);

        // Assert
        verify(actionAgentProfitRepository, times(1)).save(any(ActionAgentProfit.class));
    }

    @Test
    void createAgentProfit_shouldCreateProfitForOrderTransaction() {
        // Arrange
        OrderTransaction orderTransaction = new OrderTransaction();
        orderTransaction.setAccountNumber("123456");
        orderTransaction.setOrderId(1L);
        orderTransaction.setPayoffAmount(1000.0);

        CashAccount cashAccount = new CashAccount();
        cashAccount.setEmail("test@example.com");

        SecuritiesOwnership securitiesOwnership = new SecuritiesOwnership();
        securitiesOwnership.setAverageBuyingPrice(800.0);

        ActionAgentProfit actionAgentProfit = new ActionAgentProfit();
        actionAgentProfit.setUserEmail("test@example.com");
        actionAgentProfit.setProfit(200.0);

        when(cashAccountRepository.findByAccountNumber("123456")).thenReturn(cashAccount);
        when(actionAgentProfitRepository.save(any(ActionAgentProfit.class))).thenReturn(actionAgentProfit);
        when(actionAgentProfitMapper.actionAgentProfitToActionAgentProfitDto(any(ActionAgentProfit.class)))
                .thenReturn(new ActionAgentProfitDto());

        // Act
        ActionAgentProfitDto result = actionAgentProfitService.createAgentProfit(orderTransaction, securitiesOwnership, 1);

        // Assert
        verify(actionAgentProfitRepository, times(1)).save(any(ActionAgentProfit.class));
    }

    @Test
    void getAllProfits_shouldReturnListOfProfits() {
        // Arrange
        ActionAgentProfit actionAgentProfit = new ActionAgentProfit();
        ActionAgentProfit actionAgentProfit1 = new ActionAgentProfit();

        List<ActionAgentProfit> actionAgentProfits = List.of(actionAgentProfit, actionAgentProfit1);

        ActionAgentProfitDto actionAgentProfitDto = new ActionAgentProfitDto();
        ActionAgentProfitDto actionAgentProfitDto1 = new ActionAgentProfitDto();

        // Mocking repository to return a list with one ActionAgentProfit
        when(actionAgentProfitRepository.findAll()).thenReturn(actionAgentProfits);
        // Mocking mapper to convert ActionAgentProfit to ActionAgentProfitDto
        when(actionAgentProfitMapper.actionAgentProfitToActionAgentProfitDto(actionAgentProfit)).thenReturn(actionAgentProfitDto);

        // Act
        List<ActionAgentProfitDto> result = actionAgentProfitService.getAllProfits();

        // Assert
        assertEquals(2, result.size());
        assertEquals(actionAgentProfitDto, result.get(0));
    }

    @Test
    void getTotalProfitsByUsers_shouldReturnTotalProfitsGroupedByUser() {
        // Arrange
        ActionAgentProfit actionAgentProfit = new ActionAgentProfit();
        actionAgentProfit.setUserEmail("test@example.com");
        actionAgentProfit.setProfit(100.0);

        when(actionAgentProfitRepository.findAll()).thenReturn(Collections.singletonList(actionAgentProfit));

        // Act
        List<TotalActionAgentProfitDto> result = actionAgentProfitService.getTotalProfitsByUsers();

        // Assert
        assertEquals(1, result.size());
        assertEquals("test@example.com", result.get(0).getUserEmail());
        assertEquals(100.0, result.get(0).getTotalProfit());
    }

    @Test
    void getAgentsTotalProfits_shouldReturnSumOfAllProfits() {
        // Arrange
        ActionAgentProfit actionAgentProfit = new ActionAgentProfit();
        actionAgentProfit.setProfit(100.0);

        when(actionAgentProfitRepository.findAll()).thenReturn(Collections.singletonList(actionAgentProfit));

        // Act
        Double result = actionAgentProfitService.getAgentsTotalProfits();

        // Assert
        assertEquals(100.0, result);
    }

}
