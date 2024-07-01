package rs.edu.raf.BankService.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import rs.edu.raf.BankService.controller.BankTransferTransactionDetailsController;
import rs.edu.raf.BankService.data.dto.BankTransferTransactionDetailsDto;
import rs.edu.raf.BankService.data.entities.exchangeCurrency.BankTransferTransactionDetails;
import rs.edu.raf.BankService.service.impl.BankTransferTransactionDetailsServiceImpl;
import rs.edu.raf.BankService.unit.profitTransactionsTests.BankTransferTransactionDetailsServiceImplTest;

public class BankTransferTransactionDetailsControllerTests {

    @Mock
    private BankTransferTransactionDetailsServiceImpl bankTransferTransactionDetailsService;

    @InjectMocks
    private BankTransferTransactionDetailsController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAll() {
        List<BankTransferTransactionDetailsDto> expected = new ArrayList<>();
        expected.add(new BankTransferTransactionDetailsDto());
        expected.add(new BankTransferTransactionDetailsDto());

        when(bankTransferTransactionDetailsService.getAllBankExchangeRates()).thenReturn(expected);

        ResponseEntity<?> result = controller.getAll();
        List<BankTransferTransactionDetailsDto> actual = (List<BankTransferTransactionDetailsDto>) result.getBody();

        assertEquals(expected.size(), actual.size());
    }

    @Test
    public void getTotalProfit() {

        BankTransferTransactionDetailsDto dto1 = new BankTransferTransactionDetailsDto();
        dto1.setTotalProfit(2.0);
        BankTransferTransactionDetailsDto dto2 = new BankTransferTransactionDetailsDto();
        dto2.setTotalProfit(3.0);

        BankTransferTransactionDetailsDto[] expected = { dto1, dto2 };

        when(bankTransferTransactionDetailsService.getTotalProfit())
                .thenReturn(
                        Arrays.stream(expected).mapToDouble(BankTransferTransactionDetailsDto::getTotalProfit).sum());

        ResponseEntity<?> result = controller.getTotalProfit();
        Double actual = (Double) result.getBody();

        assertEquals(5.0, actual);
    }
}
