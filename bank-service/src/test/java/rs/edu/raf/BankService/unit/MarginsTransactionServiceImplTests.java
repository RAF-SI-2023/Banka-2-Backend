package rs.edu.raf.BankService.unit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import io.cucumber.java.lu.an;
import rs.edu.raf.BankService.data.dto.MarginsTransactionRequestDto;
import rs.edu.raf.BankService.data.dto.MarginsTransactionResponseDto;
import rs.edu.raf.BankService.data.entities.MarginsAccount;
import rs.edu.raf.BankService.data.entities.MarginsTransaction;
import rs.edu.raf.BankService.data.entities.Order;
import rs.edu.raf.BankService.data.enums.ListingType;
import rs.edu.raf.BankService.data.enums.TransactionDirection;
import rs.edu.raf.BankService.mapper.MarginsTransactionMapper;
import rs.edu.raf.BankService.repository.MarginsAccountRepository;
import rs.edu.raf.BankService.repository.MarginsTransactionRepository;
import rs.edu.raf.BankService.service.OrderService;
import rs.edu.raf.BankService.service.impl.MarginsTransactionServiceImpl;

public class MarginsTransactionServiceImplTests {

        @Mock
        private MarginsAccountRepository marginsAccountRepository;

        @Mock
        private MarginsTransactionRepository marginsTransactionRepository;

        @Mock
        private MarginsTransactionMapper transactionMapper;

        @Mock
        private RestTemplate restTemplate;

        @Mock
        private OrderService orderService;

        @InjectMocks
        private MarginsTransactionServiceImpl marginsTransactionService;

        @BeforeEach
        public void setUp() {
                MockitoAnnotations.openMocks(this);
        }

        @Test
        public void testGetFilteredTransactions() {
                MarginsTransaction transaction = new MarginsTransaction();
                when(marginsTransactionRepository.findAll(any(Specification.class)))
                                .thenReturn(Collections.singletonList(transaction));
                when(transactionMapper.toDto(any(MarginsTransaction.class)))
                                .thenReturn(new MarginsTransactionResponseDto());

                List<MarginsTransactionResponseDto> result = marginsTransactionService
                                .getFilteredTransactions("USD", LocalDateTime.now().minusDays(1), LocalDateTime.now());

                assertEquals(1, result.size());
        }

        @Test
        public void testGetTransactionsByAccountId() {
                MarginsTransaction transaction = new MarginsTransaction();
                when(marginsTransactionRepository.findAll(any(Specification.class)))
                                .thenReturn(Collections.singletonList(transaction));
                when(transactionMapper.toDto(any(MarginsTransaction.class)))
                                .thenReturn(new MarginsTransactionResponseDto());

                List<MarginsTransactionResponseDto> result = marginsTransactionService.getTransactionsByAccountId(1L);

                assertEquals(1, result.size());
        }

        @Test
        public void testFindAllByEmail() {
                MarginsTransaction transaction = new MarginsTransaction();
                when(marginsTransactionRepository.findAll(any(Specification.class)))
                                .thenReturn(Collections.singletonList(transaction));
                when(transactionMapper.toDto(any(MarginsTransaction.class)))
                                .thenReturn(new MarginsTransactionResponseDto());

                List<MarginsTransactionResponseDto> result = marginsTransactionService
                                .findAllByEmail("test@example.com");

                assertEquals(1, result.size());
        }

        @Test
        public void testCreateTransaction_Successful() {
             
                MarginsTransactionRequestDto requestDto = new MarginsTransactionRequestDto();
                requestDto.setOrderId(1L);
                requestDto.setInitialMargin(100.0);
                requestDto.setMarginsAccountId(1l);
                requestDto.setMaintenanceMargin(50.0);
                requestDto.setUserId(1l);
                requestDto.setType(TransactionDirection.DEPOSIT);

                Order mockOrder = new Order();
                mockOrder.setListingId(1L);
                mockOrder.setListingType(ListingType.STOCK);
                mockOrder.setQuantity(10);

                when(orderService.findById(1L)).thenReturn(mockOrder);
                when(restTemplate.postForEntity(anyString(), any(), eq(Double.class)))
                                .thenReturn(ResponseEntity.ok(100.0));

                MarginsAccount mockMarginsAccount = new MarginsAccount();
                mockMarginsAccount.setId(1L);
                mockMarginsAccount.setBalance(1000.0);
                mockMarginsAccount.setLoanValue(1000.0);
                mockMarginsAccount.setMaintenanceMargin(200.0);
                mockMarginsAccount.setMarginsTransactions(new ArrayList<>());

                when(marginsAccountRepository.findById(anyLong())).thenReturn(Optional.of(mockMarginsAccount));
                when(marginsAccountRepository.save(any(MarginsAccount.class))).thenReturn(mockMarginsAccount);

                MarginsTransaction mockTransaction = new MarginsTransaction();
                mockTransaction.setId(1L);
                when(transactionMapper.toEntity(any(MarginsTransactionRequestDto.class))).thenReturn(mockTransaction);
                when(transactionMapper.toDto(any(MarginsTransaction.class)))
                                .thenReturn(new MarginsTransactionResponseDto());
                when(marginsTransactionRepository.save(any())).thenReturn(mockTransaction);

                MarginsTransactionResponseDto result = marginsTransactionService.createTransaction(requestDto);

                assertEquals(null, result.getId());
        }

        @Test
        public void testUpdateMarginsAccount_Successful() {
                MarginsTransaction transaction = new MarginsTransaction();
                transaction.setId(1L);
                transaction.setInvestmentAmount(100.0);
                transaction.setLoanValue(500.0);
                transaction.setMaintenanceMargin(50.0);
                transaction.setType(TransactionDirection.DEPOSIT);

                MarginsAccount mockMarginsAccount = new MarginsAccount();
                mockMarginsAccount.setId(1L);
                mockMarginsAccount.setBalance(1000.0);
                mockMarginsAccount.setLoanValue(1000.0);
                mockMarginsAccount.setMaintenanceMargin(200.0);
                mockMarginsAccount.setMarginsTransactions(new ArrayList<>());
                mockMarginsAccount.addTransaction(transaction);

                when(marginsAccountRepository.findById(anyLong())).thenReturn(Optional.of(mockMarginsAccount));
                when(marginsAccountRepository.save(any(MarginsAccount.class))).thenReturn(mockMarginsAccount);
                MarginsAccount result = marginsTransactionService.updateMarginsAccount(1L, transaction);
                assertEquals(900.0, result.getBalance()); // 1000 - 100 (investment amount)
                assertEquals(1500.0, result.getLoanValue()); // 1000 + 500 (loan value)
                assertEquals(250.0, result.getMaintenanceMargin()); // 200 + 50 (maintenance margin)

                verify(marginsAccountRepository, times(1)).findById(1L);
                verify(marginsAccountRepository, times(1)).save(any(MarginsAccount.class));
        }
}
