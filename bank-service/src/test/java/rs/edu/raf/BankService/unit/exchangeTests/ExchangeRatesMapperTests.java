package rs.edu.raf.BankService.unit.exchangeTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import rs.edu.raf.BankService.data.dto.ExchangeRatesDto;
import rs.edu.raf.BankService.data.dto.ExchangeTransferDetailsDto;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.data.entities.exchangeCurrency.ExchangeRates;
import rs.edu.raf.BankService.data.entities.exchangeCurrency.ExchangeTransferTransactionDetails;
import rs.edu.raf.BankService.mapper.ExchangeRatesMapper;
import rs.edu.raf.BankService.mapper.ExchangeTransferDetailsMapper;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExchangeRatesMapperTests {

    @InjectMocks
    private ExchangeRatesMapper exchangeRatesMapper;

    @InjectMocks
    private ExchangeTransferDetailsMapper exchangeTransferDetailsMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExchangeRatesToExchangeRatesDto() {
        // Prepare test data
        ExchangeRates exchangeRates = new ExchangeRates();
        exchangeRates.setId(1L);
        exchangeRates.setTimeLastUpdated(System.currentTimeMillis());
        exchangeRates.setTimeNextUpdate(System.currentTimeMillis());
        exchangeRates.setFromCurrency("USD");
        exchangeRates.setToCurrency("EUR");
        exchangeRates.setExchangeRate(0.9013);

        // Invoke mapping method
        ExchangeRatesDto dto = exchangeRatesMapper.exchangeRatesToExchangeRatesDto(exchangeRates);

        // Verify mapping
        assertEquals(exchangeRates.getId(), dto.getId());
        assertEquals(exchangeRates.getTimeLastUpdated(), dto.getTimeLastUpdated());
        assertEquals(exchangeRates.getTimeNextUpdate(), dto.getTimeNextUpdate());
        assertEquals(exchangeRates.getFromCurrency(), dto.getFromCurrency());
        assertEquals(exchangeRates.getToCurrency(), dto.getToCurrency());
        assertEquals(exchangeRates.getExchangeRate(), dto.getExchangeRate());
    }

    @Test
    void testExchangeRatesDtoToExchangeRates() {
        // Prepare test data
        ExchangeRatesDto dto = new ExchangeRatesDto();
        dto.setId(1L);
        dto.setTimeLastUpdated(System.currentTimeMillis());
        dto.setTimeNextUpdate(System.currentTimeMillis());
        dto.setFromCurrency("USD");
        dto.setToCurrency("EUR");
        dto.setExchangeRate(0.9013);

        // Invoke mapping method
        ExchangeRates exchangeRates = exchangeRatesMapper.exchangeRatesDtoToExchangeRates(dto);

        // Verify mapping
        assertEquals(dto.getId(), exchangeRates.getId());
        assertEquals(dto.getTimeLastUpdated(), exchangeRates.getTimeLastUpdated());
        assertEquals(dto.getTimeNextUpdate(), exchangeRates.getTimeNextUpdate());
        assertEquals(dto.getFromCurrency(), exchangeRates.getFromCurrency());
        assertEquals(dto.getToCurrency(), exchangeRates.getToCurrency());
        assertEquals(dto.getExchangeRate(), exchangeRates.getExchangeRate());
    }

    @Test
    void testExchangeTransferDetailsToExchangeTransferDetailsDto() {
        // Prepare test data
        ExchangeTransferTransactionDetails exchangeTransferDetails = new ExchangeTransferTransactionDetails();
        exchangeTransferDetails.setId(1L);
        CashAccount senderCashAccount = new CashAccount();
        senderCashAccount.setAccountNumber("123456");
        exchangeTransferDetails.setSenderCashAccount(senderCashAccount);
        CashAccount receiverCashAccount = new CashAccount();
        receiverCashAccount.setAccountNumber("654321");
        exchangeTransferDetails.setReceiverCashAccount(receiverCashAccount);
        exchangeTransferDetails.setFromCurrency("USD");
        exchangeTransferDetails.setToCurrency("EUR");
        exchangeTransferDetails.setAmount(100.0);
        exchangeTransferDetails.setExchangeRate(0.9013);
        exchangeTransferDetails.setFee(0.0);
        exchangeTransferDetails.setTotalAmount(90.13);
        exchangeTransferDetails.setCreatedAt(LocalDateTime.of(2022, 4, 8, 12, 0));

        // Invoke mapping method
        ExchangeTransferDetailsDto dto = exchangeTransferDetailsMapper.exchangeTransferDetailsToExchangeTransferDetailsDto(exchangeTransferDetails);

        // Verify mapping
        assertEquals(exchangeTransferDetails.getId(), dto.getId());
        assertEquals(exchangeTransferDetails.getSenderCashAccount().getAccountNumber(), dto.getFromAccountNumber());
        assertEquals(exchangeTransferDetails.getReceiverCashAccount().getAccountNumber(), dto.getToAccountNumber());
        assertEquals(exchangeTransferDetails.getFromCurrency(), dto.getFromCurrency());
        assertEquals(exchangeTransferDetails.getToCurrency(), dto.getToCurrency());
        assertEquals(exchangeTransferDetails.getAmount(), dto.getAmount());
        assertEquals(exchangeTransferDetails.getExchangeRate(), dto.getExchangeRate());
        assertEquals(exchangeTransferDetails.getFee(), dto.getFee());
        assertEquals(exchangeTransferDetails.getTotalAmount(), dto.getTotalAmount());
        assertEquals(exchangeTransferDetails.getCreatedAt().toEpochSecond(ZoneOffset.UTC), dto.getDateTimeEpoch());
    }
}
