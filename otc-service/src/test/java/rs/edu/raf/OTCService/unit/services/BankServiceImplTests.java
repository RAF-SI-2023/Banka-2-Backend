package rs.edu.raf.OTCService.unit.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.OTCService.service.impl.BankServiceImpl;

public class BankServiceImplTests {

    @Mock
    private RestTemplate restTemplateMock;

    @InjectMocks
    private BankServiceImpl bankService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTransaction_Success() {

    }

    @Test
    void testBuyBank3Stock_Success() {

    }

    @Test
    void testSellStockToBank3_Success() {

    }

    @Test
    void testGetSecurityOwnerships_Success() {

    }
}
