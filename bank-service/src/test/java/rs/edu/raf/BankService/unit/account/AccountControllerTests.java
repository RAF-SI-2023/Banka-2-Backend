package rs.edu.raf.BankService.unit.account;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rs.edu.raf.BankService.controller.AccountController;
import rs.edu.raf.BankService.data.dto.*;
import rs.edu.raf.BankService.exception.AccountNotFoundException;
import rs.edu.raf.BankService.exception.AccountNumberAlreadyExistException;
import rs.edu.raf.BankService.service.impl.CashAccountServiceImpl;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTests {

    @InjectMocks
    private AccountController accountController;

    @Mock
    private CashAccountServiceImpl accountService;

    @Test
    public void testAssociateProfileWithAccount_ThrowsException() {
        AccountNumberDto accountNumberDto = new AccountNumberDto(); // Pretpostavljam da imate DTO klasu kao ovo

        when(accountService.userAccountUserProfileConnectionAttempt(accountNumberDto)).thenThrow(new RuntimeException("Neki izuzetak"));

        ResponseEntity<?> response = accountController.associateProfileWithAccount(accountNumberDto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Neki izuzetak", response.getBody());
    }

    @Test
    public void testAssociateProfileWithAccount_Success() {
        AccountNumberDto accountNumberDto = new AccountNumberDto(); // Pretpostavljam da imate DTO klasu kao ovo

        when(accountService.userAccountUserProfileConnectionAttempt(accountNumberDto)).thenReturn(true); // Pretpostavljam da ova metoda vraća boolean

        ResponseEntity<?> response = accountController.associateProfileWithAccount(accountNumberDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testConfirmActivationCode_ThrowsException() {
        String fakeAccountNumber = "123456";
        Integer fakeCode = 1234;

        when(accountService.confirmActivationCode(fakeAccountNumber, fakeCode)).thenThrow(new RuntimeException("Neki izuzetak"));

        ResponseEntity<?> response = accountController.confirmActivationCode(fakeAccountNumber, fakeCode);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Neki izuzetak", response.getBody());
    }

    @Test
    public void testAssociateProfileWithAccount_Successful() {
        String fakeAccountNumber = "123456";
        Integer fakeCode = 1234;

        when(accountService.confirmActivationCode(fakeAccountNumber, fakeCode)).thenReturn(true);

        ResponseEntity<?> response = accountController.confirmActivationCode(fakeAccountNumber, fakeCode);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
    }

    @Test
    public void whenCreateDomesticAccountThrowsAccountNumberAlreadyExists_thenResponseStatusIsConflict() {
        // Arrange
        DomesticCurrencyAccountDto dto = new DomesticCurrencyAccountDto();
        when(accountService.createDomesticCurrencyAccount(dto)).thenThrow(new AccountNumberAlreadyExistException("123456"));

        // Act
        ResponseEntity<?> response = accountController.createDomesticAccount(dto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void whenCreateDomesticAccountThrowsException_thenResponseStatusIsInternalServerError() {
        // Arrange
        DomesticCurrencyAccountDto dto = new DomesticCurrencyAccountDto();
        when(accountService.createDomesticCurrencyAccount(dto)).thenThrow(new RuntimeException("Internal server error"));

        // Act
        ResponseEntity<?> response = accountController.createDomesticAccount(dto);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal server error", response.getBody());
    }

    @Test
    public void whenCreateDomesticAccountIsCalled_thenSuccessResponseIsReturned() {
        // Arrange
        DomesticCurrencyAccountDto dto = new DomesticCurrencyAccountDto();
        when(accountService.createDomesticCurrencyAccount(dto)).thenReturn(dto);

        // Act
        ResponseEntity<?> response = accountController.createDomesticAccount(dto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    public void whenCreateForeignAccountThrowsAccountNumberAlreadyExists_thenResponseIsConflict() {
        // Arrange
        ForeignCurrencyAccountDto dto = new ForeignCurrencyAccountDto();
        when(accountService.createForeignCurrencyAccount(dto)).thenThrow(new AccountNumberAlreadyExistException("123456"));

        // Act
        ResponseEntity<?> response = accountController.createForeignAccount(dto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void whenCreateForeignAccountThrowsException_thenResponseIsInternalServerError() {
        // Arrange
        ForeignCurrencyAccountDto dto = new ForeignCurrencyAccountDto();
        when(accountService.createForeignCurrencyAccount(dto)).thenThrow(new RuntimeException());

        // Act
        ResponseEntity<?> response = accountController.createForeignAccount(dto);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void whenCreateForeignAccountIsSuccessful_thenResponseIsOkWithDto() {
        // Arrange
        ForeignCurrencyAccountDto dto = new ForeignCurrencyAccountDto();
        when(accountService.createForeignCurrencyAccount(dto)).thenReturn(dto);

        // Act
        ResponseEntity<?> response = accountController.createForeignAccount(dto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    public void whenCreateBusinessAccountThrowsAccountNumberAlreadyExists_thenResponseIsConflict() {
        // Arrange
        BusinessAccountDto dto = new BusinessAccountDto();
        when(accountService.createBusinessAccount(dto)).thenThrow(new AccountNumberAlreadyExistException("12345"));

        // Act
        ResponseEntity<?> response = accountController.createBusinessAccount(dto);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void whenCreateBusinessAccountThrowsException_thenResponseIsInternalServerError() {
        // Arrange
        BusinessAccountDto dto = new BusinessAccountDto();
        when(accountService.createBusinessAccount(dto)).thenThrow(new RuntimeException("General error"));

        // Act
        ResponseEntity<?> response = accountController.createBusinessAccount(dto);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("General error", response.getBody());
    }

    @Test
    public void whenCreateBusinessAccountIsSuccessful_thenResponseIsOkWithDto() {
        // Arrange
        BusinessAccountDto dto = new BusinessAccountDto();
        when(accountService.createBusinessAccount(dto)).thenReturn(dto);

        // Act
        ResponseEntity<?> response = accountController.createBusinessAccount(dto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    public void whenFindAccountsByEmailThrowsAccountNumberAlreadyExists_thenResponseIsConflict() {
        // Arrange
        String email = "test@example.com";
        String fakeAccountNumber = "123";
        AccountNumberAlreadyExistException exception = new AccountNumberAlreadyExistException(fakeAccountNumber);
        when(accountService.findAccountsByEmail(email)).thenThrow(exception);

        // Act
        ResponseEntity<?> response = accountController.findAccountsByEmail(email);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(exception.getMessage(), response.getBody());
    }

    @Test
    public void whenFindAccountsByEmailIsSuccessful_thenResponseIsOkWithListOfAccountDtos() {
        // Arrange
        String email = "test@example.com";
        List<AccountDto> accountDtos = Arrays.asList(new AccountDto(), new AccountDto());
        when(accountService.findAccountsByEmail(email)).thenReturn(accountDtos);

        // Act
        ResponseEntity<?> response = accountController.findAccountsByEmail(email);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accountDtos, response.getBody());
    }

    @Test
    public void createSavedAccount_Success() {
        // Arrange
        Long accountId = 1L;
        SavedAccountDto savedAccountDto = new SavedAccountDto(); // Assume this object is correctly instantiated
        SavedAccountDto returnedSavedAccountDto = new SavedAccountDto(); // Assume this object is correctly instantiated

        // Mock the service call
        when(accountService.createSavedAccount(anyLong(), any(SavedAccountDto.class)))
                .thenReturn(returnedSavedAccountDto);

        // Act
        SavedAccountDto result = accountController.createSavedAccount(accountId, savedAccountDto);

        // Assert
        assertEquals(returnedSavedAccountDto, result);
    }

    @Test
    public void updateSavedAccount_Success() {
        // Arrange
        Long accountId = 1L;
        String savedAccountNumber = "123456789";
        SavedAccountDto savedAccountDto = new SavedAccountDto(); // Initialize as necessary
        SavedAccountDto expectedDto = new SavedAccountDto(); // Initialize as necessary

        // Stub the service method to return the expected DTO
        when(accountService.updateSavedAccount(anyLong(), anyString(), any(SavedAccountDto.class)))
                .thenReturn(expectedDto);

        // Act
        SavedAccountDto actualDto = accountController.updateSavedAccount(accountId, savedAccountNumber, savedAccountDto);

        // Assert
        assertEquals(expectedDto, actualDto);
    }

    @Test
    public void deleteSavedAccount_Success() {
        // Arrange
        Long accountId = 1L;
        String savedAccountNumber = "123-456-789";

        // Act
        accountController.deleteSavedAccount(accountId, savedAccountNumber);

        // Assert
        verify(accountService, times(1)).deleteSavedAccount(accountId, savedAccountNumber);
    }

    @Test
    public void findAccountByAccountNumber_Success(){
        String accounNumber = "0004444999999999";
        AccountNumberDto accountNumberDto = new AccountNumberDto(accounNumber);
        AccountDto accountDto = new AccountDto();

        when(accountService.findAccountByNumber(accountNumberDto)).thenReturn(accountDto);

        ResponseEntity<?> response = accountController.findAccountByAccountNumber(accountNumberDto);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), accountDto);
    }


    @Test
    public void findAccountByAccountNumber_NotFound(){
        String accountNumber = "0004444999999999";
        AccountNumberDto accountNumberDto = new AccountNumberDto(accountNumber);

        when(accountService.findAccountByNumber(accountNumberDto)).thenThrow(new AccountNotFoundException(accountNumber));

        ResponseEntity<?> response = accountController.findAccountByAccountNumber(accountNumberDto);

        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        assertEquals(response.getBody(), "Account with account number " + accountNumber + " not found");

    }

}
