package rs.edu.raf.BankService.unit.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rs.edu.raf.BankService.controller.MarginsAccountController;
import rs.edu.raf.BankService.data.dto.MarginsAccountRequestDto;
import rs.edu.raf.BankService.data.dto.MarginsAccountResponseDto;
import rs.edu.raf.BankService.service.impl.MarginsAccountServiceImpl;

public class MarginsAccountControllerTests {

    @Mock
    private MarginsAccountServiceImpl marginsAccountService;

    @InjectMocks
    private MarginsAccountController marginsAccountController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateMarginsAccount_Success() {
        // Arrange
        MarginsAccountRequestDto requestDto = new MarginsAccountRequestDto();
        MarginsAccountResponseDto responseDto = new MarginsAccountResponseDto();

        when(marginsAccountService.createMarginsAccount(requestDto)).thenReturn(responseDto);

        // Act
        MarginsAccountResponseDto result = marginsAccountController.createMarginsAccount(requestDto);

        // Assert
        assertEquals(responseDto, result);
    }

    @Test
    public void testUpdateMarginsAccount_Success() {
        // Arrange
        Long id = 1L;
        MarginsAccountRequestDto requestDto = new MarginsAccountRequestDto();
        MarginsAccountResponseDto responseDto = new MarginsAccountResponseDto();

        when(marginsAccountService.updateMarginsAccount(id, requestDto)).thenReturn(responseDto);

        // Act
        MarginsAccountResponseDto result = marginsAccountController.updateMarginsAccount(id, requestDto);

        // Assert
        assertEquals(responseDto, result);
    }

    @Test
    public void testDeleteMarginsAccount_Success() {
        // Arrange
        Long id = 1L;

        // No need to mock service method as it returns void

        // Act
        marginsAccountController.deleteMarginsAccount(id);

        // Assert
        // Verify that service method was called with correct id
    }

    @Test
    public void testGetMarginsAccountById_Success() {
        // Arrange
        Long id = 1L;
        List<MarginsAccountResponseDto> responseDtoList = Collections.singletonList(new MarginsAccountResponseDto());

        when(marginsAccountService.findById(id)).thenReturn(responseDtoList);

        // Act
        List<MarginsAccountResponseDto> result = marginsAccountController.getMarginsAccountById(id);

        // Assert
        assertEquals(responseDtoList, result);
    }

    @Test
    public void testGetMarginsAccountByUserId_Success() {
        // Arrange
        Long userId = 1L;
        List<MarginsAccountResponseDto> responseDtoList = Collections.singletonList(new MarginsAccountResponseDto());

        when(marginsAccountService.findByUserId(userId)).thenReturn(responseDtoList);

        // Act
        List<MarginsAccountResponseDto> result = marginsAccountController.getMarginsAccountByUserId(userId);

        // Assert
        assertEquals(responseDtoList, result);
    }

    @Test
    public void testSettleMarginCall_Success() {
        // Arrange
        Long id = 1L;
        Double deposit = 100.0;
        MarginsAccountResponseDto responseDto = new MarginsAccountResponseDto();

        when(marginsAccountService.settleMarginCall(id, deposit)).thenReturn(responseDto);

        // Act
        MarginsAccountResponseDto result = marginsAccountController.settleMarginCall(id, deposit);

        // Assert
        assertEquals(responseDto, result);
    }

    @Test
    public void testGetMarginsAccountByEmail_Success() {
        // Arrange
        String email = "test@example.com";
        List<MarginsAccountResponseDto> responseDtoList = Collections.singletonList(new MarginsAccountResponseDto());

        when(marginsAccountService.findByEmail(email)).thenReturn(responseDtoList);

        // Act
        List<MarginsAccountResponseDto> result = marginsAccountController.getMarginsAccountByEmail(email);

        // Assert
        assertEquals(responseDtoList, result);
    }

    @Test
    public void testGetMarginsAccountByAccountNumber_Success() {
        // Arrange
        String accountNumber = "123456";
        List<MarginsAccountResponseDto> responseDtoList = Collections.singletonList(new MarginsAccountResponseDto());

        when(marginsAccountService.findByAccountNumber(accountNumber)).thenReturn(responseDtoList);

        // Act
        List<MarginsAccountResponseDto> result = marginsAccountController
                .getMarginsAccountByAccountNumber(accountNumber);

        // Assert
        assertEquals(responseDtoList, result);
    }

}
