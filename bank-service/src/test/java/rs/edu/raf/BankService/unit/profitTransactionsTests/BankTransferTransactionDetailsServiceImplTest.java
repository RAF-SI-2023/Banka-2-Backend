package rs.edu.raf.BankService.unit.profitTransactionsTests;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rs.edu.raf.BankService.data.dto.BankTransferTransactionDetailsDto;
import rs.edu.raf.BankService.data.entities.exchangeCurrency.BankTransferTransactionDetails;
import rs.edu.raf.BankService.data.entities.exchangeCurrency.ExchangeTransferTransactionDetails;
import rs.edu.raf.BankService.mapper.BankTransferTransactionDetailsMapper;
import rs.edu.raf.BankService.repository.BankTransferTransactionDetailsRepository;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.BankService.service.impl.BankTransferTransactionDetailsServiceImpl;

@ExtendWith(MockitoExtension.class)
public class BankTransferTransactionDetailsServiceImplTest {

    @Mock
    private BankTransferTransactionDetailsRepository bankTransferTransactionDetailsRepository;

    @Mock
    private BankTransferTransactionDetailsMapper bankTransferTransactionDetailsMapper;

    @InjectMocks
    private BankTransferTransactionDetailsServiceImpl bankTransferTransactionDetailsService;



    @Test
    void getAllBankExchangeRates_shouldReturnListOfDtos() {
        // Arrange
        BankTransferTransactionDetails bankTransferTransactionDetails = new BankTransferTransactionDetails();
        BankTransferTransactionDetailsDto dto = new BankTransferTransactionDetailsDto();

        when(bankTransferTransactionDetailsRepository.findAll()).thenReturn(Collections.singletonList(bankTransferTransactionDetails));
        when(bankTransferTransactionDetailsMapper.convertToDto(any(BankTransferTransactionDetails.class))).thenReturn(dto);

        // Act
        List<BankTransferTransactionDetailsDto> result = bankTransferTransactionDetailsService.getAllBankExchangeRates();

        // Assert
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void getTotalProfit_shouldCalculateTotalProfitCorrectly() {
        // Arrange
        BankTransferTransactionDetails bankTransferTransactionDetails = new BankTransferTransactionDetails();
        bankTransferTransactionDetails.setTotalProfit(100.0);

        when(bankTransferTransactionDetailsRepository.findAll()).thenReturn(Collections.singletonList(bankTransferTransactionDetails));

        // Act
        Double totalProfit = bankTransferTransactionDetailsService.getTotalProfit();

        // Assert
        assertEquals(100.0, totalProfit);
    }

    @Test
    void getTotalProfit_shouldReturnZeroWhenNoTransactions() {
        // Arrange
        when(bankTransferTransactionDetailsRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        Double totalProfit = bankTransferTransactionDetailsService.getTotalProfit();

        // Assert
        assertEquals(0.0, totalProfit);
    }

    @Test
    void createBankTransferTransactionDetails_shouldCreateAndReturnDto() {
        // Arrange
        ExchangeTransferTransactionDetails exchangeTransferTransactionDetails = new ExchangeTransferTransactionDetails();
        exchangeTransferTransactionDetails.setAmount(1000.0);
        exchangeTransferTransactionDetails.setExchangeRate(1.5);
        exchangeTransferTransactionDetails.setFromCurrency("USD");
        exchangeTransferTransactionDetails.setToCurrency("EUR");

        BankTransferTransactionDetails bankTransferTransactionDetails = new BankTransferTransactionDetails();
        BankTransferTransactionDetailsDto dto = new BankTransferTransactionDetailsDto();

        when(bankTransferTransactionDetailsRepository.save(any(BankTransferTransactionDetails.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(bankTransferTransactionDetailsMapper.convertToDto(any(BankTransferTransactionDetails.class)))
                .thenReturn(dto);

        // Act
        BankTransferTransactionDetailsDto result = bankTransferTransactionDetailsService.createBankTransferTransactionDetails(exchangeTransferTransactionDetails);

        // Assert
        assertEquals(dto, result);
        verify(bankTransferTransactionDetailsRepository, times(1)).save(any(BankTransferTransactionDetails.class));
    }

}
