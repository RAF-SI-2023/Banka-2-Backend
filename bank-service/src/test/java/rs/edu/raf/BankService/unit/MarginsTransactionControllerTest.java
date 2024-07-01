package rs.edu.raf.BankService.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rs.edu.raf.BankService.controller.MarginsTransactionController;
import rs.edu.raf.BankService.data.dto.MarginsTransactionRequestDto;
import rs.edu.raf.BankService.data.dto.MarginsTransactionResponseDto;
import rs.edu.raf.BankService.service.impl.MarginsTransactionServiceImpl;

public class MarginsTransactionControllerTest {

    @Mock
    private MarginsTransactionServiceImpl marginsTransactionService;

    @InjectMocks
    private MarginsTransactionController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTransaction() {
        MarginsTransactionRequestDto dto = new MarginsTransactionRequestDto();
        MarginsTransactionResponseDto expected = new MarginsTransactionResponseDto();
        expected.setId(1l);
        expected.setAccountNumber("2312431421412");

        when(marginsTransactionService.createTransaction(dto)).thenReturn(expected);

        MarginsTransactionResponseDto actual = controller.createTransaction(dto);

        assertEquals(expected.getId(), actual.getId());
    }

    @Test
    public void testGetFilteredTransactions() {
        MarginsTransactionResponseDto dto1 = new MarginsTransactionResponseDto();
        dto1.setCurrencyCode("RSD");
        MarginsTransactionResponseDto dto2 = new MarginsTransactionResponseDto();
        dto2.setCurrencyCode("USD");

        when(marginsTransactionService.getFilteredTransactions("RSD", null, null)).thenReturn(List.of(dto1));
        when(marginsTransactionService.getFilteredTransactions("USD", null, null)).thenReturn(List.of(dto2));

        List<MarginsTransactionResponseDto> res1 = controller.getFilteredTransactions("RSD", null, null);
        List<MarginsTransactionResponseDto> res2 = controller.getFilteredTransactions("USD", null, null);

        assertEquals(1, res1.size());
        assertEquals(1, res2.size());
    }

    @Test
    public void testGetTransactionsByAccountId() {
        MarginsTransactionResponseDto dto1 = new MarginsTransactionResponseDto();
        dto1.setAccountNumber("123");
        MarginsTransactionResponseDto dto2 = new MarginsTransactionResponseDto();
        dto2.setAccountNumber("123");
        MarginsTransactionResponseDto dto3 = new MarginsTransactionResponseDto();
        dto3.setAccountNumber("12312312");
        List<MarginsTransactionResponseDto> list = List.of(dto1, dto2, dto3);

        when(marginsTransactionService.getTransactionsByAccountId(123l)).thenReturn(list.stream()
                .filter(dto -> dto.getAccountNumber().equals("123"))
                .collect(Collectors.toList()));

        List<MarginsTransactionResponseDto> actual = controller.getTransactionsByAccountId(123l);

        assertEquals(2, actual.size());
    }

    @Test
    public void testGetAllTransactionsByEmail() {
        MarginsTransactionResponseDto dto1 = new MarginsTransactionResponseDto();
        dto1.setAccountNumber("123");
        MarginsTransactionResponseDto dto2 = new MarginsTransactionResponseDto();
        dto2.setAccountNumber("123");
        MarginsTransactionResponseDto dto3 = new MarginsTransactionResponseDto();
        dto3.setAccountNumber("12312312");
        List<MarginsTransactionResponseDto> list = List.of(dto1, dto2, dto3);

        when(marginsTransactionService.findAllByEmail("o@gmail.com")).thenReturn(list);

        List<MarginsTransactionResponseDto> actual = controller.getAllTransactionsByEmail("o@gmail.com");

        assertEquals(list.size(), actual.size());
    }
}
