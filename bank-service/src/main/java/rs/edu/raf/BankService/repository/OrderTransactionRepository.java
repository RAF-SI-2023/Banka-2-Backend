package rs.edu.raf.BankService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rs.edu.raf.BankService.data.entities.transactions.OrderTransaction;

import java.util.List;
import java.util.Optional;

public interface OrderTransactionRepository extends JpaRepository<OrderTransaction, Long> {
    Optional<OrderTransaction> findOrderTransactionByOrderId(long orderId);

    List<OrderTransaction> findAllByAccountNumber(String accountNumber);

    @Query("select o from  OrderTransaction  o join CashAccount c on c.accountNumber = o.accountNumber")
    List<OrderTransaction> findAllByEmail(String email);
}
