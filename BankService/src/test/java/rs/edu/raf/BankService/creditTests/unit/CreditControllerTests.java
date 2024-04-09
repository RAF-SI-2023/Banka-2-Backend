package rs.edu.raf.BankService.creditTests.unit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rs.edu.raf.BankService.controller.CreditController;
import rs.edu.raf.BankService.data.dto.CreditDto;
import rs.edu.raf.BankService.data.dto.CreditRequestDto;
import rs.edu.raf.BankService.service.CreditService;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditControllerTests {

    @Mock
    private CreditService creditService;

    @InjectMocks
    private CreditController creditController;

    @Test
    void testFindCreditsByAccountNumber() {
        // Given
        String accountNumber = "123456789";
        CreditDto creditDto = new CreditDto();
        when(creditService.getCreditsByAccountNumber(accountNumber)).thenReturn(Collections.singletonList(creditDto));
        when(creditService.getCreditsByAccountNumber("someString")).thenThrow(new RuntimeException("Account not found"));

        // When
        ResponseEntity<?> responseEntity = creditController.findCreditsByAccountNumber(accountNumber);
        ResponseEntity<?> responseEntityError = creditController.findCreditsByAccountNumber("someString");

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Collections.singletonList(creditDto), responseEntity.getBody());
        verify(creditService, times(1)).getCreditsByAccountNumber(accountNumber);
        assertEquals(HttpStatus.NOT_FOUND, responseEntityError.getStatusCode());

    }

    @Test
    void testGetCreditByCreditNumber() {
        // Given
        Long creditNumber = 123L;
        CreditDto creditDto = new CreditDto();
        when(creditService.getCreditByCreditNumber(creditNumber)).thenReturn(creditDto);
        when(creditService.getCreditByCreditNumber(0L)).thenThrow(new RuntimeException("Credit not found"));
        // When
        ResponseEntity<?> responseEntity = creditController.getCreditByCreditNumber(creditNumber);
        ResponseEntity<?> responseEntity1 = creditController.getCreditByCreditNumber(0L);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity1.getStatusCode());
        assertEquals(creditDto, responseEntity.getBody());
        verify(creditService, times(1)).getCreditByCreditNumber(creditNumber);
    }

    @Test
    void testCreateCreditRequest() {
        // Given
        CreditRequestDto creditRequestDto = new CreditRequestDto();
        CreditRequestDto creditRequestDtoError = new CreditRequestDto();
        when(creditService.createCreditRequest(creditRequestDto)).thenReturn(creditRequestDto);

        // When
        ResponseEntity<?> responseEntity = creditController.createCreditRequest(creditRequestDto);


        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        when(creditService.createCreditRequest(creditRequestDtoError)).thenThrow(new RuntimeException("Error"));
        ResponseEntity<?> responseEntity1 = creditController.createCreditRequest(creditRequestDtoError);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity1.getStatusCode());
        assertEquals(creditRequestDto, responseEntity.getBody());
        verify(creditService, times(2)).createCreditRequest(creditRequestDto);
    }

    @Test
    void testGetAllCreditRequests() {
        // Given
        CreditRequestDto creditRequestDto = new CreditRequestDto();

        when(creditService.getAllCreditRequests()).thenReturn(Collections.singletonList(creditRequestDto));

        // When
        ResponseEntity<?> responseEntity = creditController.getAllPendingCreditRequests();

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Collections.singletonList(creditRequestDto), responseEntity.getBody());
        verify(creditService, times(1)).getAllCreditRequests();
        when(creditService.getAllCreditRequests()).thenThrow(new RuntimeException("Error"));
        ResponseEntity<?> responseEntityError = creditController.getAllPendingCreditRequests();
        assertEquals(HttpStatus.FORBIDDEN, responseEntityError.getStatusCode());
    }

    @Test
    void testApproveCreditRequest() {
        // Given
        Long creditRequestId = 123L;
        CreditDto creditDto = new CreditDto();
        creditDto.setCreditNumber(creditRequestId);

        when(creditService.approveCreditRequest(creditRequestId)).thenReturn(creditDto);
        when(creditService.approveCreditRequest(0L)).thenThrow(new RuntimeException("Credit request is not pending"));

        // When
        ResponseEntity<?> responseEntity = creditController.approveCreditRequest(creditRequestId);
        ResponseEntity<?> responseEntityError = creditController.approveCreditRequest(0L);
        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.FORBIDDEN, responseEntityError.getStatusCode());
        assertEquals(creditDto, responseEntity.getBody());
        verify(creditService, times(1)).approveCreditRequest(creditRequestId);
    }

    @Test
    void testDenyCreditRequest() {
        // Given
        Long creditRequestId = 123L;
        when(creditService.denyCreditRequest(creditRequestId)).thenReturn(true);
        when(creditService.denyCreditRequest(0L)).thenThrow(new RuntimeException("Credit request not found"));
        // When
        ResponseEntity<?> responseEntity = creditController.denyCreditRequest(creditRequestId);
        ResponseEntity<?> responseEntityError = creditController.denyCreditRequest(0L);
        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, responseEntityError.getStatusCode());
        assertEquals(true, responseEntity.getBody());
        verify(creditService, times(1)).denyCreditRequest(creditRequestId);
    }
    // Similarly, write tests for other methods in the CreditController class
}