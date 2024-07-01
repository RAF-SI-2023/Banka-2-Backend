package rs.edu.raf.BankService.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.BankService.data.dto.MarginsTransactionResponseDto;
import rs.edu.raf.BankService.data.entities.MarginsTransaction;
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

}
