package rs.edu.raf.BankService.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rs.edu.raf.BankService.controller.BankProfitController;
import rs.edu.raf.BankService.data.dto.BankProfitDto;
import rs.edu.raf.BankService.service.impl.BankProfitServiceImpl;

public class BankProfitControllerTest {
    @Mock
    private BankProfitServiceImpl bankProfitService;

    @InjectMocks
    private BankProfitController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAll() {
        BankProfitDto expected = new BankProfitDto();
        when(bankProfitService.getTotalProfit()).thenReturn(expected);
        BankProfitDto actual = new BankProfitDto();
        assertEquals(expected, actual);
    }
}
