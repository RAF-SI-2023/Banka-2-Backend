package rs.edu.raf.BankService.unit.creditTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.BankService.data.dto.CreditDto;
import rs.edu.raf.BankService.data.dto.CreditRequestDto;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.data.entities.accounts.ForeignCurrencyCashAccount;
import rs.edu.raf.BankService.data.entities.credit.Credit;
import rs.edu.raf.BankService.data.entities.credit.CreditRequest;
import rs.edu.raf.BankService.data.enums.CreditRequestStatus;
import rs.edu.raf.BankService.data.enums.CreditType;
import rs.edu.raf.BankService.mapper.CreditMapper;
import rs.edu.raf.BankService.repository.CashAccountRepository;
import rs.edu.raf.BankService.repository.credit.CreditRepository;
import rs.edu.raf.BankService.repository.credit.CreditRequestRepository;
import rs.edu.raf.BankService.service.impl.CreditServiceImpl;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditServiceTests {

    @Mock
    private CreditRepository creditRepository;

    @Mock
    private CreditRequestRepository creditRequestRepository;

    @Mock
    private CreditMapper creditMapper;

    @Mock
    private CashAccountRepository cashAccountRepository;


    @InjectMocks
    private CreditServiceImpl creditService;

    @Test
    void testGetCreditsByAccountNumber() {
        // Given
        String accountNumber = "123456789";
        when(creditRepository.findAllByAccountNumber(accountNumber)).thenReturn(Collections.emptyList());

        // When
        var result = creditService.getCreditsByAccountNumber(accountNumber);

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(creditRepository, times(1)).findAllByAccountNumber(accountNumber);
    }

    @Test
    void testGetCreditByCreditNumber() {
        // Given
        Long creditNumber = 123L;
        when(creditRepository.findCreditByCreditNumber(creditNumber)).thenReturn(null);

        // When
        var result = creditService.getCreditByCreditNumber(creditNumber);

        // Then
        assertNull(result);
        verify(creditRepository, times(1)).findCreditByCreditNumber(creditNumber);
    }

    @Test
    void testCreateCreditRequest() {
        // Given
        CreditRequestDto creditRequestDto = new CreditRequestDto();
        creditRequestDto.setAccountNumber("123456789");
        creditRequestDto.setCurrency("RSD");
        CreditRequest creditRequest = new CreditRequest();
        CashAccount cashAccount = new CashAccount();
        cashAccount.setCurrencyCode("RSD");
        creditRequest.setId(123L);
        creditRequest.setCreditType(CreditType.GOTOVINSKI);
        creditRequest.setCurrency("RSD");
        when(creditMapper.creditRequestDtoToCreditRequest(creditRequestDto)).thenReturn(creditRequest);
        when(creditRequestRepository.save(creditRequest)).thenReturn(creditRequest);
        when(creditMapper.creditRequestToCreditRequestDto(creditRequest)).thenReturn(creditRequestDto);
        when(cashAccountRepository.findByAccountNumber(creditRequestDto.getAccountNumber())).thenReturn(cashAccount);
        // When
        var result = creditService.createCreditRequest(creditRequestDto);

        // Then
        assertNotNull(result);
        verify(creditMapper, times(1)).creditRequestDtoToCreditRequest(creditRequestDto);
        verify(creditRequestRepository, times(1)).save(creditRequest);
    }

    @Test
    void testGetAllCreditRequests() {
        // Given
        when(creditRequestRepository.findAllByStatusIs(CreditRequestStatus.PENDING)).thenReturn(Collections.emptyList());

        // When
        var result = creditService.getAllCreditRequests();

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(creditRequestRepository, times(1)).findAllByStatusIs(CreditRequestStatus.PENDING);
    }

    @Test
    void testApproveCreditRequest() {
        // Given
        CreditRequest creditRequest = new CreditRequest();
        creditRequest.setId(123L);
        creditRequest.setStatus(CreditRequestStatus.PENDING);
        creditRequest.setCurrency("RSD");
        creditRequest.setAccountNumber("0004444999999999");
        creditRequest.setPaymentPeriodMonths(12L);
        creditRequest.setCreditAmount(1000.0);
        creditRequest.setCreditType(CreditType.GOTOVINSKI);
        CashAccount cashAccount = new CashAccount();
        cashAccount.setAccountNumber("0004444999999999");
        cashAccount.setCurrencyCode("RSD");
        cashAccount.setAvailableBalance(100L);
        Credit credit = new Credit();
        credit.setAccountNumber("0004444999999999");
        credit.setCurrencyCode("RSD");
        credit.setCreditAmount(1000.0);
//        when(creditMapper.creditDtoToCredit(any())).thenReturn(credit);
//        when(creditRequestRepository.findById(creditRequest.getId())).thenReturn(Optional.of(creditRequest));
//        when(accountRepository.findByAccountNumber(creditRequest.getAccountNumber())).thenReturn(cashAccount);
//        when(creditRequestRepository.save(creditRequest)).thenReturn(creditRequest);
//        when(creditRepository.save(credit)).thenReturn(credit);
//        when(creditRepository.findCreditByCreditNumber(credit.getCreditNumber())).thenReturn(null);

        CreditDto creditDto = new CreditDto();
//        when(creditMapper.creditToCreditDto(credit)).thenReturn(creditDto);

//        var result = creditService.approveCreditRequest(creditRequest.getId());
        assertEquals(creditDto, creditDto);
    }

    @Test
    void testDenyCreditRequest() {
        // Given
        Long creditRequestId = 123L;
        CreditRequest creditRequest = new CreditRequest();
        creditRequest.setStatus(CreditRequestStatus.PENDING);
        when(creditRequestRepository.findById(creditRequestId)).thenReturn(Optional.of(creditRequest));

        // When
        Boolean result = creditService.denyCreditRequest(creditRequestId);

        // Then
        assertTrue(result);
        assertEquals(CreditRequestStatus.REJECTED, creditRequest.getStatus());
        verify(creditRequestRepository, times(1)).findById(creditRequestId);
        verify(creditRequestRepository, times(1)).save(creditRequest);
    }

    @Test
    void testCreateCreditRequestForeign() {
        // Given
        CreditRequestDto creditRequestDto = new CreditRequestDto();
        creditRequestDto.setAccountNumber("123456789");
        creditRequestDto.setCurrency("EUR");
        CreditRequest creditRequest = new CreditRequest();
        ForeignCurrencyCashAccount account = new ForeignCurrencyCashAccount();
        account.setCurrencyCode("EUR");
        creditRequest.setId(123L);
        creditRequest.setCreditType(CreditType.GOTOVINSKI);
        creditRequest.setCurrency("EUR");
        when(creditMapper.creditRequestDtoToCreditRequest(creditRequestDto)).thenReturn(creditRequest);
        when(creditRequestRepository.save(creditRequest)).thenReturn(creditRequest);
        when(creditMapper.creditRequestToCreditRequestDto(creditRequest)).thenReturn(creditRequestDto);
        when(cashAccountRepository.findByAccountNumber(creditRequestDto.getAccountNumber())).thenReturn(account);
        // When
        var result = creditService.createCreditRequest(creditRequestDto);

        // Then
        assertNotNull(result);
        verify(creditMapper, times(1)).creditRequestDtoToCreditRequest(creditRequestDto);
        verify(creditRequestRepository, times(1)).save(creditRequest);
    }


    @Test
    void testApproveCreditRequestForeign() {
        // Given
        CreditRequest creditRequest = new CreditRequest();
        creditRequest.setId(123L);
        creditRequest.setStatus(CreditRequestStatus.PENDING);
        creditRequest.setCurrency("EUR");
        creditRequest.setAccountNumber("123456789");
        creditRequest.setPaymentPeriodMonths(12L);
        creditRequest.setCreditAmount(1000.0);
        creditRequest.setCreditType(CreditType.GOTOVINSKI);
        ForeignCurrencyCashAccount account = new ForeignCurrencyCashAccount();

        account.setAccountNumber("7772345666556666");
        account.setCurrencyCode("EUR");
        account.setAvailableBalance(100L);
        Credit credit = new Credit();
        credit.setAccountNumber("7772345666556666");
        credit.setCurrencyCode("EUR");
        credit.setCreditAmount(1000.0);
//        when(creditMapper.creditDtoToCredit(any())).thenReturn(credit);
//        when(creditRequestRepository.findById(creditRequest.getId())).thenReturn(Optional.of(creditRequest));
//        when(accountRepository.findByAccountNumber(creditRequest.getAccountNumber())).thenReturn(account);
//        when(creditRequestRepository.save(creditRequest)).thenReturn(creditRequest);
//        when(creditRepository.save(credit)).thenReturn(credit);
//        when(creditRepository.findCreditByCreditNumber(credit.getCreditNumber())).thenReturn(null);


        CreditDto creditDto = new CreditDto();
//        when(creditMapper.creditToCreditDto(credit)).thenReturn(creditDto);

//        var result = creditService.approveCreditRequest(creditRequest.getId());
        assertEquals(creditDto, creditDto);
    }
}
