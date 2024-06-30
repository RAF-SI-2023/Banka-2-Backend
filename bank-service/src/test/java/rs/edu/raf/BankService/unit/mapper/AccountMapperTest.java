package rs.edu.raf.BankService.unit.mapper;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import rs.edu.raf.BankService.data.dto.AccountDto;
import rs.edu.raf.BankService.data.dto.BusinessAccountDto;
import rs.edu.raf.BankService.data.dto.DomesticCurrencyAccountDto;
import rs.edu.raf.BankService.data.dto.ForeignCurrencyAccountDto;
import rs.edu.raf.BankService.data.entities.accounts.BusinessCashAccount;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.data.entities.accounts.DomesticCurrencyCashAccount;
import rs.edu.raf.BankService.data.entities.accounts.ForeignCurrencyCashAccount;
import rs.edu.raf.BankService.data.enums.AccountType;
import rs.edu.raf.BankService.data.enums.DomesticCurrencyAccountType;
import rs.edu.raf.BankService.data.enums.UserAccountUserProfileLinkState;
import rs.edu.raf.BankService.mapper.AccountMapper;

public class AccountMapperTest {

    private AccountMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new AccountMapper();
    }

    @Test
    public void accountDtoToAccount_Success() {
        // Given
        AccountDto dto = new AccountDto();
        dto.setAccountNumber("12345");
        dto.setEmail("owner@gmail.com");
        dto.setAccountType(AccountType.BANK_ACCOUNT.name());
        dto.setCurrencyCode("USD");

        // When
        CashAccount account = mapper.accountDtoToAccount(dto);

        // Then
        assertEquals("12345", account.getAccountNumber());
        assertEquals("owner@gmail.com", account.getEmail());
        assertEquals(AccountType.BANK_ACCOUNT.name(), dto.getAccountType());
        assertEquals("USD", account.getCurrencyCode());
        assertEquals(220.0, dto.getMaintenanceFee(), 0);
    }

    @Test
    public void domesticAccountDtoToDomesticAccount_Success() {
        // Given
        DomesticCurrencyAccountDto dto = new DomesticCurrencyAccountDto();
        dto.setAccountNumber("12345");
        dto.setEmail("owner@gmail.com");
        dto.setCurrencyCode("RSD");

        // When
        DomesticCurrencyCashAccount account = mapper.domesticAccountDtoToDomesticAccount(dto);

        // Then
        assertEquals("12345", account.getAccountNumber());
        assertEquals("owner@gmail.com", account.getEmail());
        assertEquals(AccountType.DOMESTIC_CURRENCY_ACCOUNT, dto.getAccountType());
        assertEquals("RSD", account.getCurrencyCode());
        assertEquals(220.0, dto.getMaintenanceFee(), 0);
    }

    @Test
    public void foreignAccountDtoToForeignAccount_Success() {
        // Given
        ForeignCurrencyAccountDto dto = new ForeignCurrencyAccountDto();
        dto.setAccountNumber("12345");
        dto.setEmail("owner@gmail.com");
        dto.setCurrencyCode("EUR");

        // When
        ForeignCurrencyCashAccount account = mapper.foreignAccountDtoToForeignAccount(dto);

        // Then
        assertEquals("12345", account.getAccountNumber());
        assertEquals("owner@gmail.com", account.getEmail());
        assertEquals(AccountType.FOREIGN_CURRENCY_ACCOUNT, dto.getAccountType());
        assertEquals("EUR", account.getCurrencyCode());
        assertEquals(220.0, dto.getMaintenanceFee(), 0);
    }

    @Test
    public void businessAccountDtoToBusinessAccount_Success() {
        // Given
        BusinessAccountDto dto = new BusinessAccountDto();
        dto.setAccountNumber("12345");
        dto.setEmail("owner@gmail.com");
        dto.setCurrencyCode("EUR");
        dto.setPIB("123456789");
        dto.setIdentificationNumber("65");

        // When
        BusinessCashAccount account = mapper.businessAccountDtoToBusinessAccount(dto);

        // Then
        assertEquals("12345", account.getAccountNumber());
        assertEquals("owner@gmail.com", account.getEmail());
        assertEquals(AccountType.BUSINESS_ACCOUNT, dto.getAccountType());
        assertEquals("EUR", account.getCurrencyCode());
        assertEquals(220.0, dto.getMaintenanceFee(), 0);
        assertEquals("123456789", account.getPIB());
        assertEquals("65", account.getIdentificationNumber());
    }

    @Test
    public void accountToAccountDto_Success() {
        // Given
        CashAccount cashAccount = new CashAccount("120884329483248", "owner@gmail.com",
                AccountType.BANK_ACCOUNT, "JPY", 220.0);

        // When
        AccountDto dto = mapper.accountToAccountDto(cashAccount);

        // Then
        assertEquals("120884329483248", dto.getAccountNumber());
        assertEquals(UserAccountUserProfileLinkState.NOT_ASSOCIATED, dto.getLinkState());
        assertEquals("owner@gmail.com", dto.getEmail());
        assertEquals(AccountType.BANK_ACCOUNT, dto.getAccountType());
        assertEquals("JPY", dto.getCurrencyCode());
        assertEquals(220.0, dto.getMaintenanceFee(), 0);
    }

    @Test
    public void domesticCurrencyAccountToDomesticCurrencyAccountDto_Success() {
        // Given
        DomesticCurrencyCashAccount account = new DomesticCurrencyCashAccount(
                "08940948908903180",
                "owner@gmail.com",
                AccountType.BANK_ACCOUNT,
                "CHF", 220.0, DomesticCurrencyAccountType.RETIREMENT);

        // When
        DomesticCurrencyAccountDto dto = mapper.domesticCurrencyAccountToDomesticCurrencyAccountDtoDto(account);

        // Then
        assertEquals("08940948908903180", dto.getAccountNumber());
        assertEquals("owner@gmail.com", dto.getEmail());
        assertEquals(AccountType.BANK_ACCOUNT, dto.getAccountType());
        assertEquals("CHF", dto.getCurrencyCode());
        assertEquals(DomesticCurrencyAccountType.RETIREMENT, dto.getDomesticCurrencyAccountType());
        assertEquals(2.5, dto.getInterestRate(), 0);
    }

    @Test
    public void businessAccountToBusinessAccountDto_Success() {
        // Given
        BusinessCashAccount account = new BusinessCashAccount(
                "33355566143415",
                "owner@gmail.com",
                AccountType.BUSINESS_ACCOUNT,
                "USD",
                220.0,
                "123456789",
                "66");

        // When
        BusinessAccountDto dto = mapper.businessAccountToBusinessAccountDto(account);

        // Then
        assertEquals("33355566143415", dto.getAccountNumber());
        assertEquals("owner@gmail.com", dto.getEmail());
        assertEquals(AccountType.BUSINESS_ACCOUNT, dto.getAccountType());
        assertEquals("USD", dto.getCurrencyCode());
        assertEquals(BigDecimal.valueOf(220.0), dto.getMaintenanceFee());
        assertEquals("123456789", dto.getPIB());
        assertEquals("66", dto.getIdentificationNumber());
    }

}
