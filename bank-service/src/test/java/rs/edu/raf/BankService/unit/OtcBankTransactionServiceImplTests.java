package rs.edu.raf.BankService.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.BankService.data.dto.GenericTransactionDto;
import rs.edu.raf.BankService.data.dto.OtcOfferDto;
import rs.edu.raf.BankService.data.entities.SecuritiesOwnership;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.data.entities.transactions.SecuritiesTransaction;
import rs.edu.raf.BankService.data.enums.TransactionStatus;
import rs.edu.raf.BankService.mapper.TransactionMapper;
import rs.edu.raf.BankService.repository.CashAccountRepository;
import rs.edu.raf.BankService.repository.CashTransactionRepository;
import rs.edu.raf.BankService.repository.SecuritiesOwnershipRepository;
import rs.edu.raf.BankService.service.TransactionService;
import rs.edu.raf.BankService.service.impl.OtcBankTransactionServiceImpl;

public class OtcBankTransactionServiceImplTests {

    @Mock
    private SecuritiesOwnershipRepository securitiesOwnershipRepository;

    @Mock
    private CashAccountRepository cashAccountRepository;

    @Mock
    private TransactionService transactionService;

    @Mock
    private CashTransactionRepository cashTransactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OtcBankTransactionServiceImpl otcBankTransactionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBuyStock_Success() {
        OtcOfferDto otcOfferDto = new OtcOfferDto();
        otcOfferDto.setTicker("AAPL");
        otcOfferDto.setAmount(10);
        otcOfferDto.setPrice(100.0);

        CashAccount banksAccount = new CashAccount();
        banksAccount.setAccountNumber("123456");
        banksAccount.setEmail("banks@bank.rs");
        banksAccount.setAvailableBalance(500.0);

        CashAccount bank3Account = new CashAccount();
        bank3Account.setAccountNumber("789012");
        bank3Account.setEmail("bank3Account@bank.rs");
        bank3Account.setAvailableBalance(1000.0);

        SecuritiesOwnership securitiesOwnership = new SecuritiesOwnership();
        securitiesOwnership.setAccountNumber("123456");
        securitiesOwnership.setEmail("banks@bank.rs");
        securitiesOwnership.setSecuritiesSymbol("AAPL");
        securitiesOwnership.setQuantity(5);
        securitiesOwnership.setQuantityOfPubliclyAvailable(5);
        when(cashAccountRepository.findPrimaryTradingAccount(null)).thenReturn(banksAccount);
        when(cashAccountRepository.findPrimaryTradingAccount("bank3Account@bank.rs")).thenReturn(bank3Account);
        when(securitiesOwnershipRepository.findAllByAccountNumber("123456")).thenReturn(List.of(securitiesOwnership));
        when(transactionMapper.toGenericTransactionDto(any(SecuritiesTransaction.class)))
                .thenReturn(new GenericTransactionDto());

        when(transactionService.reserveFunds(eq(banksAccount), any(Double.class))).thenReturn(true);
        when(transactionService.releaseFunds(eq(banksAccount), any(Double.class))).thenReturn(true);

        GenericTransactionDto result = otcBankTransactionService.buyStock(otcOfferDto);
        assertEquals(null, result.getStatus());
    }

    @Test
    public void testSellStock_Success() {
        // Mocking dependencies
        OtcOfferDto otcOfferDto = new OtcOfferDto();
        otcOfferDto.setTicker("AAPL");
        otcOfferDto.setAmount(5);
        otcOfferDto.setPrice(120.0);

        CashAccount banksAccount = new CashAccount();
        banksAccount.setAccountNumber("123456");
        banksAccount.setEmail("banks@bank.rs");
        banksAccount.setAvailableBalance(500.0);

        CashAccount bank3Account = new CashAccount();
        bank3Account.setAccountNumber("789012");
        bank3Account.setEmail("bank3Account@bank.rs");
        bank3Account.setAvailableBalance(1000.0);

        SecuritiesOwnership securitiesOwnership = new SecuritiesOwnership();
        securitiesOwnership.setAccountNumber("123456");
        securitiesOwnership.setEmail("banks@bank.rs");
        securitiesOwnership.setSecuritiesSymbol("AAPL");
        securitiesOwnership.setQuantity(10);
        securitiesOwnership.setQuantityOfPubliclyAvailable(10);

        when(cashAccountRepository.findPrimaryTradingAccount(null)).thenReturn(banksAccount);
        when(cashAccountRepository.findPrimaryTradingAccount("bank3Account@bank.rs")).thenReturn(bank3Account);
        when(securitiesOwnershipRepository.findAllByAccountNumber("123456")).thenReturn(List.of(securitiesOwnership));
        when(transactionMapper.toGenericTransactionDto(any(SecuritiesTransaction.class)))
                .thenReturn(new GenericTransactionDto());
        when(transactionService.addFunds(eq(banksAccount), any(Double.class))).thenReturn(true);

        GenericTransactionDto result = otcBankTransactionService.sellStock(otcOfferDto);
        assertEquals(null, result.getStatus());
    }
}
