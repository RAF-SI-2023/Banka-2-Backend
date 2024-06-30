package rs.edu.raf.BankService.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import rs.edu.raf.BankService.data.dto.MarginsAccountRequestDto;
import rs.edu.raf.BankService.data.dto.MarginsAccountResponseDto;
import rs.edu.raf.BankService.data.entities.MarginsAccount;
import rs.edu.raf.BankService.data.entities.MarginsTransaction;
import rs.edu.raf.BankService.data.enums.TransactionDirection;
import rs.edu.raf.BankService.filters.principal.CustomUserPrincipal;
import rs.edu.raf.BankService.mapper.MarginsAccountMapper;
import rs.edu.raf.BankService.repository.MarginsAccountRepository;
import rs.edu.raf.BankService.repository.MarginsTransactionRepository;
import rs.edu.raf.BankService.service.impl.MarginsAccountServiceImpl;
import rs.edu.raf.BankService.springSecurityUtil.SpringSecurityUtil;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MarginsAccountServiceImplTests {

    @Mock
    private SpringSecurityUtil springSecurityUtilMock;

    @Mock
    private MarginsAccountRepository marginsAccountRepository;

    @Mock
    private MarginsAccountMapper marginsAccountMapper;

    @Mock
    private MarginsTransactionRepository marginsTransactionRepository;

    @InjectMocks
    private MarginsAccountServiceImpl marginsAccountService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContext securityContext = new SecurityContextImpl();
        securityContext
                .setAuthentication(new TestingAuthenticationToken(new CustomUserPrincipal(1l, "email"), null, "test"));
        SecurityContextHolder.setContext(securityContext);

    }

    @Test
    public void testCreateMarginsAccount() {
        MarginsAccountRequestDto requestDto = new MarginsAccountRequestDto();
        MarginsAccount marginsAccount = new MarginsAccount();
        MarginsAccountResponseDto responseDto = new MarginsAccountResponseDto();

        when(marginsAccountMapper.toEntity(requestDto)).thenReturn(marginsAccount);
        when(marginsAccountRepository.save(marginsAccount)).thenReturn(marginsAccount);
        when(marginsAccountMapper.toDto(marginsAccount)).thenReturn(responseDto);

        MarginsAccountResponseDto result = marginsAccountService.createMarginsAccount(requestDto);

        assertNotNull(result);
        verify(marginsAccountRepository, times(1)).save(marginsAccount);
    }

    @Test
    public void testUpdateMarginsAccount() {
        Long id = 1L;
        MarginsAccountRequestDto requestDto = new MarginsAccountRequestDto();
        MarginsAccount marginsAccount = new MarginsAccount();
        MarginsAccountResponseDto responseDto = new MarginsAccountResponseDto();

        when(marginsAccountRepository.existsById(id)).thenReturn(true);
        when(marginsAccountMapper.toEntity(requestDto)).thenReturn(marginsAccount);
        when(marginsAccountRepository.save(marginsAccount)).thenReturn(marginsAccount);
        when(marginsAccountMapper.toDto(marginsAccount)).thenReturn(responseDto);

        MarginsAccountResponseDto result = marginsAccountService.updateMarginsAccount(id, requestDto);

        assertNotNull(result);
        verify(marginsAccountRepository, times(1)).save(marginsAccount);
    }

    @Test
    public void testUpdateMarginsAccount_NotFound() {
        Long id = 1L;
        MarginsAccountRequestDto requestDto = new MarginsAccountRequestDto();

        when(marginsAccountRepository.existsById(id)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            marginsAccountService.updateMarginsAccount(id, requestDto);
        });

        assertEquals("Margins account with id " + id + " doesn't exist", exception.getMessage());
    }

    @Test
    public void testDeleteById() {
        Long id = 1L;

        when(marginsAccountRepository.existsById(id)).thenReturn(true);

        marginsAccountService.deleteById(id);

        verify(marginsAccountRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteById_NotFound() {
        Long id = 1L;

        when(marginsAccountRepository.existsById(id)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            marginsAccountService.deleteById(id);
        });

        assertEquals("Margins account with id " + id + " doesn't exist", exception.getMessage());
    }

    @Test
    public void testFindById() {
        Long id = 1L;
        MarginsAccount marginsAccount = new MarginsAccount();
        List<MarginsAccount> marginsAccounts = new ArrayList<>();
        marginsAccounts.add(marginsAccount);
        MarginsAccountResponseDto responseDto = new MarginsAccountResponseDto();

        when(marginsAccountRepository.findAllById(id)).thenReturn(marginsAccounts);
        when(marginsAccountMapper.toDto(marginsAccount)).thenReturn(responseDto);

        List<MarginsAccountResponseDto> result = marginsAccountService.findById(id);

        assertEquals(1, result.size());
        verify(marginsAccountRepository, times(1)).findAllById(id);
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        MarginsAccount marginsAccount = new MarginsAccount();
        List<MarginsAccount> marginsAccounts = new ArrayList<>();
        marginsAccounts.add(marginsAccount);
        MarginsAccountResponseDto responseDto = new MarginsAccountResponseDto();

        when(marginsAccountRepository.findAllByUserId(userId)).thenReturn(marginsAccounts);
        when(marginsAccountMapper.toDto(marginsAccount)).thenReturn(responseDto);

        List<MarginsAccountResponseDto> result = marginsAccountService.findByUserId(userId);

        assertEquals(1, result.size());
        verify(marginsAccountRepository, times(1)).findAllByUserId(userId);
    }

    @Test
    public void testSettleMarginCall_NoMarginCall() {
        Long id = 1L;
        Double deposit = 100.0;
        MarginsAccount marginsAccount = new MarginsAccount();
        marginsAccount.setMarginCall(false);

        when(marginsAccountRepository.findById(id)).thenReturn(Optional.of(marginsAccount));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            marginsAccountService.settleMarginCall(id, deposit);
        });

        assertEquals("Margin call for margins account with id " + id + " already settled", exception.getMessage());
    }

    @Test
    public void testSettleMarginCall_AccountNotFound() {
        Long id = 1L;
        Double deposit = 100.0;

        when(marginsAccountRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            marginsAccountService.settleMarginCall(id, deposit);
        });

        assertEquals("Margins account with id " + id + " doesn't exist", exception.getMessage());
    }

    @Test
    public void testFindByEmail() {
        String email = "test@example.com";
        MarginsAccount marginsAccount = new MarginsAccount();
        List<MarginsAccount> marginsAccounts = new ArrayList<>();
        marginsAccounts.add(marginsAccount);
        MarginsAccountResponseDto responseDto = new MarginsAccountResponseDto();

        when(marginsAccountRepository.findAllByEmail(email)).thenReturn(marginsAccounts);
        when(marginsAccountMapper.toDto(marginsAccount)).thenReturn(responseDto);

        List<MarginsAccountResponseDto> result = marginsAccountService.findByEmail(email);

        assertEquals(1, result.size());
        verify(marginsAccountRepository, times(1)).findAllByEmail(email);
    }

    @Test
    public void testFindByAccountNumber() {
        String accountNumber = "123456";
        MarginsAccount marginsAccount = new MarginsAccount();
        List<MarginsAccount> marginsAccounts = new ArrayList<>();
        marginsAccounts.add(marginsAccount);
        MarginsAccountResponseDto responseDto = new MarginsAccountResponseDto();

        when(marginsAccountRepository.findAllByAccountNumber(accountNumber)).thenReturn(marginsAccounts);
        when(marginsAccountMapper.toDto(marginsAccount)).thenReturn(responseDto);

        List<MarginsAccountResponseDto> result = marginsAccountService.findByAccountNumber(accountNumber);

        assertEquals(1, result.size());
        verify(marginsAccountRepository, times(1)).findAllByAccountNumber(accountNumber);
    }

    @Test
    public void testCreateTransactionForMarginCallSettlement() {
        Long currentTimestamp = System.currentTimeMillis();
        MarginsTransaction expected = new MarginsTransaction();
        expected.setInvestmentAmount(2.0);
        expected.setCreatedAt(currentTimestamp);
        expected.setUserId(1l);
        expected.setType(TransactionDirection.DEPOSIT);
        expected.setDescription("MARGIN CALL DEPOSIT " + 2.0);

        MarginsTransaction actual = marginsAccountService.createTransactionForMarginCallSettlement(2.0);

        assertEquals(expected.getId(), actual.getId());
    }

}
