package rs.edu.raf.BankService.unit.account;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import rs.edu.raf.BankService.data.dto.*;
import rs.edu.raf.BankService.data.entities.SavedAccount;
import rs.edu.raf.BankService.data.entities.UserAccountUserProfileActivationCode;
import rs.edu.raf.BankService.data.entities.accounts.BusinessCashAccount;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.data.entities.accounts.DomesticCurrencyCashAccount;
import rs.edu.raf.BankService.data.entities.accounts.ForeignCurrencyCashAccount;
import rs.edu.raf.BankService.data.enums.UserAccountUserProfileLinkState;
import rs.edu.raf.BankService.exception.*;
import rs.edu.raf.BankService.mapper.AccountMapper;
import rs.edu.raf.BankService.repository.CashAccountRepository;
import rs.edu.raf.BankService.repository.UserAccountUserProfileActivationCodeRepository;
import rs.edu.raf.BankService.service.impl.CashAccountServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CashAccountServiceTests {

    @InjectMocks
    private CashAccountServiceImpl accountService ;

    @Mock
    private UserAccountUserProfileActivationCodeRepository userAccountUserProfileActivationCodeRepository;

    @Mock
    private CashAccountRepository cashAccountRepository;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private RabbitTemplate mockRabbitTemplate;

//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//    }

    @Test
    public void whenAccountNotFound_thenThrowAccountNotFoundException() {
        // Arrange
        AccountNumberDto accountNumberDto = new AccountNumberDto("nonExistingAccountNumber");
        when(cashAccountRepository.findByAccountNumber(accountNumberDto.getAccountNumber())).thenReturn(null);

        // Act & Assert
        assertThrows(AccountNotFoundException.class, () ->
            accountService.userAccountUserProfileConnectionAttempt(accountNumberDto)
        );
    }

    @Test
    public void testUserAccountUserProfileConnectionAttemptWhenAccountExistsAndNotAssociated() {
        // Arrange
        AccountNumberDto accountNumberDto = new AccountNumberDto("123456");
        CashAccount cashAccount = new CashAccount();
        cashAccount.setLinkState(UserAccountUserProfileLinkState.NOT_ASSOCIATED);

        when(cashAccountRepository.findByAccountNumber(anyString())).thenReturn(cashAccount);

        // Arrange
        doNothing().when(mockRabbitTemplate).convertAndSend(anyString(), any(EmailDto.class));

        // Act
        boolean result = accountService.userAccountUserProfileConnectionAttempt(accountNumberDto);

        // Assert
        assertTrue(result);
        verify(cashAccountRepository).saveAndFlush(cashAccount);
    }

    @Test
    public void whenLinkStateIsInProcess_thenThrowUserAccountInProcessOfBindingWithUserProfileException() {
        // Arrange
        String accountNumber = "123456";
        AccountNumberDto accountNumberDto = new AccountNumberDto(accountNumber);
        CashAccount mockCashAccount = mock(CashAccount.class);
        when(cashAccountRepository.findByAccountNumber(accountNumber)).thenReturn(mockCashAccount);
        when(mockCashAccount.getLinkState()).thenReturn(UserAccountUserProfileLinkState.IN_PROCESS);

        // Act & Assert
        assertThrows(UserAccountInProcessOfBindingWithUserProfileException.class, () ->
            accountService.userAccountUserProfileConnectionAttempt(accountNumberDto)
        );

        // Verifikacija da nema interakcija sa repozitorijumom koja bi dovela do promene u stanju
        verify(cashAccountRepository, never()).save(any(CashAccount.class));
    }

    @Test
    public void whenAccountLinkStateIsAlreadyAssociated_thenThrowException() {
        // Arrange
        String fakeAccountNumber = "123456789";
        AccountNumberDto accountNumberDto = new AccountNumberDto();
        accountNumberDto.setAccountNumber(fakeAccountNumber);
        CashAccount fakeCashAccount = new CashAccount();
        fakeCashAccount.setAccountNumber(fakeAccountNumber);
        fakeCashAccount.setLinkState(UserAccountUserProfileLinkState.ASSOCIATED);

        when(cashAccountRepository.findByAccountNumber(fakeAccountNumber)).thenReturn(fakeCashAccount);

        // Act & Assert
        assertThrows(UserAccountAlreadyAssociatedWithUserProfileException.class, () ->
            accountService.userAccountUserProfileConnectionAttempt(accountNumberDto)
        );
    }

    @Test
    public void whenAccountNotFound_thenReturnFalse() {
        // Arrange
        String fakeAccountNumber = "123456789";
        Integer fakeCode = 1234;

        when(cashAccountRepository.findByAccountNumber(fakeAccountNumber)).thenReturn(null);

        // Act
        boolean result = accountService.confirmActivationCode(fakeAccountNumber, fakeCode);

        // Assert
        assertFalse(result);
    }

    @Test
    public void whenActivationCodeDoesNotMatch_thenThrowException() {
        // Arrange
        String accountNumber = "123456789";
        Integer wrongCode = 9999;
        Integer correctCode = 1234;

        CashAccount fakeCashAccount = new CashAccount();
        fakeCashAccount.setAccountNumber(accountNumber);
        UserAccountUserProfileActivationCode fakeToken = new UserAccountUserProfileActivationCode();
        fakeToken.setCode(String.valueOf(correctCode));

        when(cashAccountRepository.findByAccountNumber(accountNumber)).thenReturn(fakeCashAccount);
        when(userAccountUserProfileActivationCodeRepository.findByAccountNumber(accountNumber)).thenReturn(fakeToken);

        // Act & Assert
        assertThrows(ActivationCodeDoesNotMatchException.class, () -> {
            accountService.confirmActivationCode(accountNumber, wrongCode);
        });
    }

    @Test
    void whenActivationCodeIsCorrectButExpired_thenThrowActivationCodeExpiredException() {
        // Arrange
        String accountNumber = "123456789";
        Integer expiredCode = 1234;
        CashAccount mockCashAccount = new CashAccount();

        UserAccountUserProfileActivationCode mockActivationCode = new UserAccountUserProfileActivationCode();
        mockActivationCode.setCode(String.valueOf(expiredCode));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, -1);
        mockActivationCode.setExpirationDateTime(String.valueOf(calendar.getTime().getTime()));

        when(cashAccountRepository.findByAccountNumber(accountNumber)).thenReturn(mockCashAccount);
        when(userAccountUserProfileActivationCodeRepository.findByAccountNumber(accountNumber)).thenReturn(mockActivationCode);

        // Act & Assert
        assertThrows(ActivationCodeExpiredException.class, () -> {
            accountService.confirmActivationCode(accountNumber, expiredCode);
        });

        // Verify that delete was not called since the token is expired
        verify(userAccountUserProfileActivationCodeRepository, never()).delete(mockActivationCode);
    }

    @Test
    void whenActivationCodeIsConfirmed_thenSucceed() {
        // Arrange
        String accountNumber = "12345";
        Integer code = 554411;
        CashAccount cashAccount = new CashAccount();
        UserAccountUserProfileActivationCode token = new UserAccountUserProfileActivationCode();
        token.setCode(String.valueOf(code));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);
        token.setExpirationDateTime(String.valueOf(calendar.getTime().getTime()));

        when(cashAccountRepository.findByAccountNumber(anyString())).thenReturn(cashAccount);
        when(userAccountUserProfileActivationCodeRepository.findByAccountNumber(anyString())).thenReturn(token);

        // Act
        boolean result = accountService.confirmActivationCode(accountNumber, code);

        // Assert
        assertTrue(result);
    }

    @Test
    void whenDomesticAccountNumberExists_thenThrowException() {
        // Arrange
        DomesticCurrencyAccountDto dto = new DomesticCurrencyAccountDto();
        dto.setAccountNumber("existingAccountNumber");
        CashAccount existingAccount = new CashAccount();

        when(cashAccountRepository.findByAccountNumber(dto.getAccountNumber())).thenReturn(existingAccount);

        // Act and Assert
        assertThrows(AccountNumberAlreadyExistException.class, () -> {
            accountService.createDomesticCurrencyAccount(dto);
        });
    }

    @Test
    void whenDomesticAccountNumberNotExists_thenCreateNewAccount() {
        // Arrange
        DomesticCurrencyAccountDto dto = new DomesticCurrencyAccountDto();
        dto.setAccountNumber("NonExistingAccountNumber");
        DomesticCurrencyCashAccount domesticCurrencyCashAccount = new DomesticCurrencyCashAccount();
        when(cashAccountRepository.findByAccountNumber(dto.getAccountNumber())).thenReturn(null);
        when(accountMapper.domesticAccountDtoToDomesticAccount(dto)).thenReturn(domesticCurrencyCashAccount);

        // Act
        DomesticCurrencyAccountDto result = accountService.createDomesticCurrencyAccount(dto);

        // Assert
        verify(cashAccountRepository, times(1)).saveAndFlush(domesticCurrencyCashAccount);
        assertNotNull(result);
    }

    @Test
    void whenForeignAccountNumberExists_thenThrowException() {
        // Arrange
        ForeignCurrencyAccountDto dto = new ForeignCurrencyAccountDto();
        dto.setAccountNumber("existingAccountNumber");
        CashAccount existingAccount = new CashAccount();

        when(cashAccountRepository.findByAccountNumber(dto.getAccountNumber())).thenReturn(existingAccount);

        // Act and Assert
        assertThrows(AccountNumberAlreadyExistException.class, () -> {
            accountService.createForeignCurrencyAccount(dto);
        });
    }

    @Test
    void whenForeignAccountNumberNotExists_thenCreateNewAccount() {
        // Arrange
        ForeignCurrencyAccountDto dto = new ForeignCurrencyAccountDto();
        dto.setAccountNumber("NonExistingAccountNumber");
        ForeignCurrencyCashAccount foreignCurrencyCashAccount = new ForeignCurrencyCashAccount();
        when(cashAccountRepository.findByAccountNumber(dto.getAccountNumber())).thenReturn(null);
        when(accountMapper.foreignAccountDtoToForeignAccount(dto)).thenReturn(foreignCurrencyCashAccount);

        // Act
        ForeignCurrencyAccountDto result = accountService.createForeignCurrencyAccount(dto);

        // Assert
        verify(cashAccountRepository, times(1)).save(foreignCurrencyCashAccount);
        assertNotNull(result);
    }

    @Test
    void whenBusinessAccountNumberExists_thenThrowException() {
        // Arrange
        BusinessAccountDto dto = new BusinessAccountDto();
        dto.setAccountNumber("existingAccountNumber");
        CashAccount existingAccount = new CashAccount();

        when(cashAccountRepository.findByAccountNumber(dto.getAccountNumber())).thenThrow(AccountNumberAlreadyExistException.class);

        // Act and Assert
        assertThrows(AccountNumberAlreadyExistException.class, () -> {
            accountService.createBusinessAccount(dto);
        });
    }

    @Test
    void whenBusinessAccountNumberNotExists_thenCreateNewAccount() {
        // Arrange
        BusinessAccountDto dto = new BusinessAccountDto();
        dto.setAccountNumber("NonExistingAccountNumber");
        BusinessCashAccount businessCurrencyCashAccount = new BusinessCashAccount();
        when(cashAccountRepository.findByAccountNumber(dto.getAccountNumber())).thenReturn(null);
        when(accountMapper.businessAccountDtoToBusinessAccount(dto)).thenReturn(businessCurrencyCashAccount);

        // Act
        BusinessAccountDto result = accountService.createBusinessAccount(dto);

        // Assert
        verify(cashAccountRepository, times(1)).save(businessCurrencyCashAccount);
        assertNotNull(result);
    }

    @Test
    public void whenNoAccountsExistForEmail_thenEmptyListIsReturned() {
        // Arrange
        String testEmail = "test@example.com";
        when(cashAccountRepository.findAllByEmail(testEmail)).thenReturn(List.of());

        // Act
        List<AccountDto> result = accountService.findAccountsByEmail(testEmail);

        // Assert
        assertTrue(result.isEmpty(), "Expected empty list of accounts for non-existing email");
    }

    @Test
    void whenAccountsExistForEmail_thenMappedAccountListIsReturned() {
        // Arrange
        String testEmail = "test@example.com";
        DomesticCurrencyCashAccount domesticAccount = new DomesticCurrencyCashAccount();
        ForeignCurrencyCashAccount foreignAccount = new ForeignCurrencyCashAccount();
        BusinessCashAccount businessAccount = new BusinessCashAccount();
        List<CashAccount> accounts = Arrays.asList(domesticAccount, foreignAccount, businessAccount);
        when(cashAccountRepository.findAllByEmail(testEmail)).thenReturn(accounts);

        DomesticCurrencyAccountDto domesticDto = new DomesticCurrencyAccountDto();
        ForeignCurrencyAccountDto foreignDto = new ForeignCurrencyAccountDto();
        BusinessAccountDto businessDto = new BusinessAccountDto();


        when(accountMapper.domesticCurrencyAccountToDomesticCurrencyAccountDtoDto(domesticAccount)).thenReturn(domesticDto);
        when(accountMapper.foreignCurrencyAccountToForeignCurrencyAccountDtoDto(foreignAccount)).thenReturn(foreignDto);
        when(accountMapper.businessAccountToBusinessAccountDto(businessAccount)).thenReturn(businessDto);

        // Act
        List<AccountDto> result = accountService.findAccountsByEmail(testEmail);

        // Assert
        assertEquals(3, result.size(), "Expected three accounts for existing email");
        assertTrue(result.contains(domesticDto), "Expected domestic account DTO");
        assertTrue(result.contains(foreignDto), "Expected foreign account DTO");
        assertTrue(result.contains(businessDto), "Expected business account DTO");
    }

    @Test
    public void testCreateSavedAccountWhenAccountNotFound() {
        // Arrange
        Long fakeAccountId = 1L;
        SavedAccountDto fakeSavedAccountDto = new SavedAccountDto(); // Pretpostavljamo da postoji konstruktor
        // Configure the mock to throw an exception when findById is called
        when(cashAccountRepository.findById(fakeAccountId)).thenThrow(new AccountNotFoundException("Account not found"));

        // Act and Assert
        assertThrows(AccountNotFoundException.class, () -> {
            accountService.createSavedAccount(fakeAccountId, fakeSavedAccountDto);
        });
    }

    @Test
    public void testCreateSavedAccountWhenAccountFound() {
        // Arrange
        Long existingAccountId = 1L;
        SavedAccountDto newSavedAccountDto = new SavedAccountDto();
        newSavedAccountDto.setName("Test Account");
        newSavedAccountDto.setAccountNumber("1234567890");

        CashAccount existingAccount = new CashAccount();
        existingAccount.setSavedAccounts(new ArrayList<>());

        when(cashAccountRepository.findById(existingAccountId)).thenReturn(Optional.of(existingAccount));

        // Act
        SavedAccountDto result = accountService.createSavedAccount(existingAccountId, newSavedAccountDto);

        // Assert
        assertNotNull(result);
        assertEquals(newSavedAccountDto.getName(), result.getName());
        assertEquals(newSavedAccountDto.getAccountNumber(), result.getAccountNumber());
        verify(cashAccountRepository).save(any(CashAccount.class)); // Proverite da li je save metoda pozvana
    }

    @Test
    public void testUpdateSavedAccountWhenAccountNotFound() {
        // Arrange
        Long fakeAccountId = 1L;
        String fakeSavedAccountNumber = "1123";
        SavedAccountDto fakeSavedAccountDto = new SavedAccountDto(); // Pretpostavljamo da postoji konstruktor
        // Configure the mock to throw an exception when findById is called
        when(cashAccountRepository.findById(fakeAccountId)).thenThrow(new AccountNotFoundException("Account not found"));

        // Act and Assert
        assertThrows(AccountNotFoundException.class, () -> {
            accountService.updateSavedAccount(fakeAccountId, fakeSavedAccountNumber, fakeSavedAccountDto);
        });
    }

    @Test
    public void testUpdateSavedAccountWhenSavedAccountNotFound() {
        // Arrange
        Long fakeAccountId = 1L;
        String fakeSavedAccountNumber = "1123";
        SavedAccountDto fakeSavedAccountDto = new SavedAccountDto();
        fakeSavedAccountDto.setAccountNumber(fakeSavedAccountNumber);

        SavedAccount wrongFakeSavedAccount = new SavedAccount();
        wrongFakeSavedAccount.setAccountNumber("9999");

        CashAccount existingAccount = new CashAccount();
        ArrayList<SavedAccount> savedAccounts = new ArrayList<>();
        savedAccounts.add(wrongFakeSavedAccount);
        existingAccount.setSavedAccounts(savedAccounts);

        // Configure the mock to throw an exception when findById is called
        when(cashAccountRepository.findById(fakeAccountId)).thenReturn(Optional.of(existingAccount));

        // Act and Assert
        assertThrows(AccountNotFoundException.class, () -> {
            accountService.updateSavedAccount(fakeAccountId, fakeSavedAccountNumber, fakeSavedAccountDto);
        });
    }

    @Test
    public void testUpdateSavedAccount_Success() {
        // Arrange
        Long fakeAccountId = 1L;
        String fakeSavedAccountNumber = "1123";
        SavedAccountDto fakeSavedAccountDto = new SavedAccountDto();
        fakeSavedAccountDto.setAccountNumber(fakeSavedAccountNumber);

        SavedAccount wrongFakeSavedAccount = new SavedAccount();
        wrongFakeSavedAccount.setAccountNumber("1123");

        CashAccount existingAccount = new CashAccount();
        ArrayList<SavedAccount> savedAccounts = new ArrayList<>();
        savedAccounts.add(wrongFakeSavedAccount);
        existingAccount.setSavedAccounts(savedAccounts);

        // Configure the mock to throw an exception when findById is called
        when(cashAccountRepository.findById(fakeAccountId)).thenReturn(Optional.of(existingAccount));

        SavedAccountDto resultDto = accountService.updateSavedAccount(fakeAccountId, fakeSavedAccountNumber, fakeSavedAccountDto);

        // Assert
        assertEquals(fakeSavedAccountDto.getName(), resultDto.getName());
        assertEquals(fakeSavedAccountDto.getAccountNumber(), resultDto.getAccountNumber());
        verify(cashAccountRepository).save(existingAccount);
    }

    @Test
    public void testDeleteSavedAccountWhenSavedAccountNotFound() {
        Long fakeAccountId = 1L;
        String fakeSavedAccountNumber = "1123";

        when(cashAccountRepository.findById(fakeAccountId)).thenThrow(new AccountNotFoundException("Account not found"));

        assertThrows(AccountNotFoundException.class, () -> {
            accountService.deleteSavedAccount(fakeAccountId, fakeSavedAccountNumber);
        });
    }

    @Test
    public void testDeleteSavedAccount_Success() {
        Long fakeAccountId = 1L;
        String fakeSavedAccountNumber = "1123";
        SavedAccount fakeSavedAccount = new SavedAccount();
        fakeSavedAccount.setAccountNumber(fakeSavedAccountNumber);

        CashAccount existingAccount = new CashAccount();
        ArrayList<SavedAccount> savedAccounts = new ArrayList<>();
        savedAccounts.add(fakeSavedAccount);
        existingAccount.setSavedAccounts(savedAccounts);

        when(cashAccountRepository.findById(fakeAccountId)).thenReturn(Optional.of(existingAccount));

        accountService.deleteSavedAccount(fakeAccountId, fakeSavedAccountNumber);

        // Assert
        verify(cashAccountRepository, times(1)).save(existingAccount);
        assertTrue(existingAccount.getSavedAccounts().isEmpty());
    }



}
