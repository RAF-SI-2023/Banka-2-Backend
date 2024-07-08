package rs.edu.raf.OTCService.unit.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.OTCService.data.entity.listing.BankOTCStock;
import rs.edu.raf.OTCService.data.entity.offer.Offer;
import rs.edu.raf.OTCService.service.impl.UtilityServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UtilityServiceImplTest {

    @InjectMocks
    private UtilityServiceImpl utilityService;

    @Mock
    private List<Offer> offerListMock;

    @Mock
    private List<BankOTCStock> bankOTCStocksMock;

    @BeforeEach
    void setUp() {
        offerListMock = new ArrayList<>();
        Offer offer1 = new Offer();
        offer1.setOfferId(1L);
        offer1.setPrice(2000.0);
        Offer offer2 = new Offer();
        offer2.setOfferId(2l);
        offer2.setPrice(1200.0);
        offerListMock.add(offer1);
        offerListMock.add(offer2);

        bankOTCStocksMock = new ArrayList<>();
        bankOTCStocksMock.add(new BankOTCStock(1L, "AAPL", 500));
        bankOTCStocksMock.add(new BankOTCStock(2L, "GOOG", 700));
    }

    @Test
    public void testFindOfferWithHighestPrice() {
        Offer highestPriceOffer = utilityService.findOfferWithHighestPrice(offerListMock);
        assertEquals(1l, highestPriceOffer.getOfferId());
    }

    @Test
    public void testFindOfferWithLowestPrice() {
        Offer lowestPriceOffer = utilityService.findOfferWithLowestPrice(offerListMock);
        assertEquals(2L, lowestPriceOffer.getOfferId());
    }

    @Test
    public void testFindStockWithHighestAmount() {
        BankOTCStock highestAmountStock = utilityService.findStockWithHighestAmount(bankOTCStocksMock);
        assertEquals(2L, highestAmountStock.getId());
    }

    @Test
    public void testGenerateAccountNumber() {
        String accountNumber = utilityService.generateAccountNumber(10);
        assertNotNull(accountNumber);
        assertEquals(10, accountNumber.length());
        assertTrue(accountNumber.matches("\\d{10}"));
    }

    @Test
    public void testGenerateAccountNumber_invalidLength() {
        assertThrows(IllegalArgumentException.class, () -> utilityService.generateAccountNumber(0));
        assertThrows(IllegalArgumentException.class, () -> utilityService.generateAccountNumber(-5));
    }

    @Test
    public void testFindOfferWithHighestPrice_emptyList() {
        assertThrows(IllegalArgumentException.class, () -> utilityService.findOfferWithHighestPrice(new ArrayList<>()));
    }

    @Test
    public void testFindOfferWithLowestPrice_emptyList() {
        assertThrows(IllegalArgumentException.class, () -> utilityService.findOfferWithLowestPrice(new ArrayList<>()));
    }

    @Test
    public void testFindStockWithHighestAmount_emptyList() {
        assertThrows(IndexOutOfBoundsException.class,
                () -> utilityService.findStockWithHighestAmount(new ArrayList<>()));
    }

    @Test
    public void testFilterEmails() {
        List<String> emails = Arrays.asList(
                "user1@example.com",
                "user2@test.com",
                "user3@example.com",
                "user4@test.com");

        List<String> filteredEmails = utilityService.filterEmails(emails, "example.com");
        assertEquals(2, filteredEmails.size());
        assertTrue(filteredEmails.contains("user1@example.com"));
        assertTrue(filteredEmails.contains("user3@example.com"));
    }

    @Test
    public void testFindPrimeNumbers() {
        List<Integer> expectedPrimes = Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19, 23, 29);

        List<Integer> primeNumbers = utilityService.findPrimeNumbers(30);
        assertEquals(expectedPrimes, primeNumbers);
    }

    @Test
    public void testReverseString() {
        String input = "hello";
        String reversed = utilityService.reverseString(input);
        assertEquals("olleh", reversed);
    }

    @Test
    public void testReverseString_nullInput() {
        assertThrows(IllegalArgumentException.class, () -> utilityService.reverseString(null));
    }
}
