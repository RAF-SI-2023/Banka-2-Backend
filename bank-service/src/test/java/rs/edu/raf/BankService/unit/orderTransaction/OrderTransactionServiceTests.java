package rs.edu.raf.BankService.unit.orderTransaction;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.BankService.data.entities.transactions.OrderTransaction;
import rs.edu.raf.BankService.repository.OrderTransactionRepository;
import rs.edu.raf.BankService.service.impl.OrderTransactionServiceImpl;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderTransactionServiceTests {
    @Mock
    private OrderTransactionRepository orderTransactionRepository;

    @InjectMocks
    private OrderTransactionServiceImpl orderTransactionService;

    @Test
    public void testFindById_Found() {
        long id=1;

        OrderTransaction ot=new OrderTransaction();
        ot.setId(id);
        Optional<OrderTransaction> otOptional=Optional.of(ot);

        when(orderTransactionRepository.findById(id)).thenReturn(otOptional);

        assertEquals(orderTransactionService.findById(id), otOptional.get());
    }

    @Test
    public void testFindById_NotFound() {
        long id=-1;

        when(orderTransactionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> orderTransactionService.findById(id));
    }

    @Test
    public void testFindByOrderId_OrderFound() {
        long orderId=1;

        OrderTransaction ot=new OrderTransaction();
        ot.setId(orderId);
        Optional<OrderTransaction> otOptional=Optional.of(ot);

        when(orderTransactionRepository.findOrderTransactionByOrderId(orderId)).thenReturn(otOptional);

        assert(orderTransactionService.findByOrderId(orderId).equals(otOptional.get()));
    }

    @Test
    public void testFindByOrderId_OrderNotFound() {
        long orderId=-1;

        when(orderTransactionRepository.findOrderTransactionByOrderId(orderId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> orderTransactionService.findByOrderId(orderId));
    }

    @Test
    public void testFindAll() {
        ArrayList<OrderTransaction> orderTransactions=new ArrayList<>();
        orderTransactions.add(new OrderTransaction());
        orderTransactions.add(new OrderTransaction());

        when(orderTransactionRepository.findAll()).thenReturn(orderTransactions);

        assert(orderTransactionService.findAll().equals(orderTransactions));
    }

    @Test
    public void testFindAllByAccountNumber() {
        String accountNumber="123";

        ArrayList<OrderTransaction> orderTransactions=new ArrayList<>();
        orderTransactions.add(new OrderTransaction());
        orderTransactions.add(new OrderTransaction());

        when(orderTransactionRepository.findAllByAccountNumber(accountNumber)).thenReturn(orderTransactions);

        assert(orderTransactionService.findAllByAccountNumber(accountNumber).equals(orderTransactions));
    }
}
