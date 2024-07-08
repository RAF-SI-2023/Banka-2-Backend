package rs.edu.raf.OTCService.unit.services;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.OTCService.data.dto.ContractDto;
import rs.edu.raf.OTCService.data.dto.GenericTransactionDto;
import rs.edu.raf.OTCService.data.dto.SecurityOwnershipDto;
import rs.edu.raf.OTCService.data.dto.testing.MyOfferDto;
import rs.edu.raf.OTCService.data.dto.testing.OfferDto;
import rs.edu.raf.OTCService.data.enums.TransactionStatus;
import rs.edu.raf.OTCService.service.impl.BankServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BankServiceImplTest {

    private RestTemplate restTemplateMock = mock(RestTemplate.class);

    @InjectMocks
    private BankServiceImpl bankService;

    @Test
    public void testCreateTransaction() {
        ContractDto contractDto = new ContractDto();
        GenericTransactionDto transactionDto = new GenericTransactionDto("123", 1000L, 1625154000000L,
                TransactionStatus.CONFIRMED, null);

        ResponseEntity<String> responseEntityMock = ResponseEntity
                .ok("{ \"id\": \"123\", \"amount\": 1000, \"createdAt\": 1625154000000, \"status\": \"CONFIRMED\" }");

        when(restTemplateMock.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(GenericTransactionDto.class)))
                .thenReturn((ResponseEntity) responseEntityMock);

        assertThrows(Exception.class, () -> {
            bankService.createTransaction(contractDto);
        });
    }

    @Test
    public void testBuyBank3Stock() {
        MyOfferDto myOfferDto = new MyOfferDto();
        GenericTransactionDto transactionDto = new GenericTransactionDto("123", 1000L, 1625154000000L,
                TransactionStatus.CONFIRMED, null);

        ResponseEntity<GenericTransactionDto> responseEntityMock = ResponseEntity.ok(transactionDto);

        when(restTemplateMock.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(GenericTransactionDto.class)))
                .thenReturn(responseEntityMock);

        assertThrows(Exception.class, () -> {
            bankService.buyBank3Stock(myOfferDto);
        });

    }

    @Test
    public void testSellStockToBank3() {
        OfferDto offerDto = new OfferDto();
        GenericTransactionDto transactionDto = new GenericTransactionDto("123", 1000L, 1625154000000L,
                TransactionStatus.CONFIRMED, null);

        ResponseEntity<GenericTransactionDto> responseEntityMock = ResponseEntity.ok(transactionDto);

        when(restTemplateMock.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(GenericTransactionDto.class)))
                .thenReturn(responseEntityMock);

        assertThrows(Exception.class, () -> {
            bankService.sellStockToBank3(offerDto);
        });
    }

    @Test
    public void testGetSecurityOwnerships() {
        List<SecurityOwnershipDto> ownershipDtoList = new ArrayList<>();

        ResponseEntity<List<SecurityOwnershipDto>> responseEntityMock = ResponseEntity.ok(ownershipDtoList);

        when(restTemplateMock.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)))
                .thenReturn(responseEntityMock);

        assertThrows(Exception.class, () -> {
            bankService.getSecurityOwnerships();
        });
    }
}