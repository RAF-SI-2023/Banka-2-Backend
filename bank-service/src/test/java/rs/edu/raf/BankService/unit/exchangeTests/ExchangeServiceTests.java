package rs.edu.raf.BankService.unit.exchangeTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rs.edu.raf.BankService.data.dto.ExchangeRatesDto;
import rs.edu.raf.BankService.data.dto.ExchangeRequestDto;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.data.entities.accounts.ForeignCurrencyCashAccount;
import rs.edu.raf.BankService.data.entities.exchangeCurrency.ExchangeRates;
import rs.edu.raf.BankService.exception.AccountNotFoundException;
import rs.edu.raf.BankService.mapper.ExchangeRatesMapper;
import rs.edu.raf.BankService.mapper.ExchangeTransferDetailsMapper;
import rs.edu.raf.BankService.repository.CashAccountRepository;
import rs.edu.raf.BankService.repository.CashTransactionRepository;
import rs.edu.raf.BankService.repository.ExchangeRateRepository;
import rs.edu.raf.BankService.repository.ForeignCurrencyHolderRepository;
import rs.edu.raf.BankService.service.impl.CurrencyExchangeServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExchangeServiceTests {

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @Mock
    private CashAccountRepository cashAccountRepository;

    @Mock
    private CashTransactionRepository cashTransactionRepository;

    @Mock
    private ForeignCurrencyHolderRepository foreignCurrencyHolderRepository;
    @Mock
    private ExchangeRatesMapper exchangeRatesMapper;
    @Mock
    private ExchangeTransferDetailsMapper exchangeTransferDetailsMapper;

    @InjectMocks
    private CurrencyExchangeServiceImpl currencyExchangeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllExchangeRates() {
        // Mock repository response
        List<ExchangeRates> exchangeRatesList = List.of(new ExchangeRates(), new ExchangeRates());
        when(exchangeRateRepository.findAll()).thenReturn(exchangeRatesList);

        // Invoke service method
        List<ExchangeRatesDto> result = currencyExchangeService.getAllExchangeRates();

        // Verify
        assertEquals(exchangeRatesList.size(), result.size());
    }

    @Test
    void testGetExchangeRatesForCurrency() {
        // Mock repository response
        ExchangeRates exchangeRates = new ExchangeRates();
        exchangeRates.setFromCurrency("USD");
        exchangeRates.setToCurrency("EUR");
        List<ExchangeRates> exchangeRatesList = List.of(exchangeRates);
        when(exchangeRateRepository.findByFromCurrencyAndToCurrency(anyString(), anyString())).thenReturn(exchangeRates);
        when(exchangeRateRepository.findAll()).thenReturn(exchangeRatesList);

        // Invoke service method
        List<ExchangeRatesDto> result = currencyExchangeService.getExchangeRatesForCurrency("USD");

        // Verify
        assertEquals(1, result.size());
    }

    @Test
    void testExchangeCurrency_Success() {
        // Prepare test data
        ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto();
        exchangeRequestDto.setFromAccount("111111111111111111");
        exchangeRequestDto.setToAccount("222222222222222222");
        CashAccount from = new CashAccount();
        from.setAccountNumber("111111111111111111");
        from.setAvailableBalance(100l);
        from.setEmail("a");
        from.setCurrencyCode("USD");


        ForeignCurrencyCashAccount to = new ForeignCurrencyCashAccount();

        to.setAccountNumber("222222222222222222");
        to.setAvailableBalance(0l);
        to.setEmail("a");
        to.setCurrencyCode("EUR");


        CashAccount bank1 = new CashAccount();
        bank1.setAccountNumber("1");
        bank1.setAvailableBalance(10000l);
        bank1.setEmail("a");
        bank1.setCurrencyCode("USD");
        CashAccount bank2 = new CashAccount();
        bank2.setAccountNumber("1");
        bank2.setAvailableBalance(10000l);
        bank2.setEmail("a");
        bank2.setCurrencyCode("EUR");
        when(cashAccountRepository.findByAccountNumber("111111111111111111")).thenReturn(from);
        when(cashAccountRepository.findByAccountNumber("222222222222222222")).thenReturn(to);
        when(exchangeRateRepository.findByFromCurrencyAndToCurrency(anyString(), anyString())).thenReturn(new ExchangeRates());
        when(cashAccountRepository.findAllByEmail(anyString())).thenReturn(List.of(new CashAccount(), new CashAccount()));
        when(cashAccountRepository.findAllByEmail(anyString())).thenReturn(List.of(bank1, bank2));
        // Invoke service method
        assertDoesNotThrow(() -> currencyExchangeService.exchangeCurrency(exchangeRequestDto));

        // Verify
        verify(cashTransactionRepository, times(1)).save(any());
    }

    @Test
    void testExchangeCurrency_AccountNotFound() {
        // Prepare test data
        ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto();
        when(cashAccountRepository.findByAccountNumber(anyString())).thenReturn(null);

        // Invoke and verify
        assertThrows(AccountNotFoundException.class, () -> currencyExchangeService.exchangeCurrency(exchangeRequestDto));
    }

    @Test
    void testExchangeCurrency_notSamePerson() {
        ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto();
        exchangeRequestDto.setFromAccount("111111111111111111");
        exchangeRequestDto.setToAccount("222222222222222222");
        exchangeRequestDto.setAmount(10000000);
        CashAccount from = new CashAccount();
        from.setAccountNumber("111111111111111111");
        from.setAvailableBalance(100l);
        from.setEmail("a");
        from.setCurrencyCode("USD");


        ForeignCurrencyCashAccount to = new ForeignCurrencyCashAccount();

        to.setAccountNumber("222222222222222222");
        to.setAvailableBalance(0l);
        to.setEmail("b");
        to.setCurrencyCode("EUR");


        CashAccount bank1 = new CashAccount();
        bank1.setAccountNumber("1");
        bank1.setAvailableBalance(10000l);
        bank1.setEmail("a");
        bank1.setCurrencyCode("USD");
        CashAccount bank2 = new CashAccount();
        bank2.setAccountNumber("1");
        bank2.setAvailableBalance(10000l);
        bank2.setEmail("a");
        bank2.setCurrencyCode("EUR");
        when(cashAccountRepository.findByAccountNumber("111111111111111111")).thenReturn(from);
        when(cashAccountRepository.findByAccountNumber("222222222222222222")).thenReturn(to);
        when(exchangeRateRepository.findByFromCurrencyAndToCurrency(anyString(), anyString())).thenReturn(new ExchangeRates());
        when(cashAccountRepository.findAllByEmail(anyString())).thenReturn(List.of(new CashAccount(), new CashAccount()));
        when(cashAccountRepository.findAllByEmail(anyString())).thenReturn(List.of(bank1, bank2));


        // Invoke and verify
        assertThrows(RuntimeException.class, () -> currencyExchangeService.exchangeCurrency(exchangeRequestDto));
    }

    // Add more test cases as needed
}
