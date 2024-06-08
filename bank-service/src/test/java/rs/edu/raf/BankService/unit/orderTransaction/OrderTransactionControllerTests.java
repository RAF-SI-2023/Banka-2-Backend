package rs.edu.raf.BankService.unit.orderTransaction;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import rs.edu.raf.BankService.controller.OrderTransactionController;
import rs.edu.raf.BankService.data.entities.transactions.OrderTransaction;
import rs.edu.raf.BankService.service.OrderTransactionService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class OrderTransactionControllerTests {
    @Mock
    private OrderTransactionService orderTransactionService;

    @InjectMocks
    private OrderTransactionController orderTransactionController;

    @Test
    public void testFindAll() {
        OrderTransaction ot1=new OrderTransaction();
        OrderTransaction ot2=new OrderTransaction();
        List<OrderTransaction> expectedResponse = Arrays.asList(ot1, ot2);

        when(orderTransactionService.findAll()).thenReturn(expectedResponse);

        ResponseEntity<?> responseEntity = orderTransactionController.findAll();

        assertEquals(expectedResponse, responseEntity.getBody());
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testFindById() {
        long id=1;
        OrderTransaction ot1=new OrderTransaction();

        when(orderTransactionService.findById(id)).thenReturn(ot1);

        ResponseEntity<?> responseEntity = orderTransactionController.findById(id);

        assertEquals(ot1, responseEntity.getBody());
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testFindByOrderId() {
        long orderId=1;
        OrderTransaction ot1=new OrderTransaction();

        when(orderTransactionService.findByOrderId(orderId)).thenReturn(ot1);

        ResponseEntity<?> responseEntity = orderTransactionController.findByOrderId(orderId);

        assertEquals(ot1, responseEntity.getBody());
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testFindAllByAccountNumber() {
        String accountNumber="123";
        OrderTransaction ot1=new OrderTransaction();
        OrderTransaction ot2=new OrderTransaction();
        List<OrderTransaction> expectedResponse = Arrays.asList(ot1, ot2);

        when(orderTransactionService.findAllByAccountNumber(accountNumber)).thenReturn(expectedResponse);

        ResponseEntity<?> responseEntity = orderTransactionController.findAllByAccountNumber(accountNumber);

        assertEquals(expectedResponse, responseEntity.getBody());
        assertEquals(200, responseEntity.getStatusCodeValue());
    }
}
