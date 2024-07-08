package rs.edu.raf.BankService.service;

import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.data.entities.transactions.OrderTransaction;

import java.util.List;

@Service
public interface OrderTransactionService {
    OrderTransaction findById(long id);

    OrderTransaction findByOrderId(long orderId);

    List<OrderTransaction> findAll();

    List<OrderTransaction> findAllByAccountNumber(String accountNumber);

    List<OrderTransaction> findAllByEmail(String email);
}
