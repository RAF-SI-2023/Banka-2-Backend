package rs.edu.raf.BankService.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.data.entities.transactions.OrderTransaction;
import rs.edu.raf.BankService.repository.OrderTransactionRepository;
import rs.edu.raf.BankService.service.OrderTransactionService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderTransactionServiceImpl implements OrderTransactionService {
    private final OrderTransactionRepository orderTransactionRepository;
    @Override
    public OrderTransaction findById(long id) {
        Optional<OrderTransaction> ot=orderTransactionRepository.findById(id);
        if(ot.isPresent())
            return ot.get();
        else throw new RuntimeException("OrderTransaction with id "+id + " doenst exist");
    }

    @Override
    public OrderTransaction findByOrderId(long orderId) {
        Optional<OrderTransaction> ot=orderTransactionRepository.findOrderTransactionByOrderId(orderId);
        if(ot.isPresent())
            return ot.get();
        else throw new RuntimeException("OrderTransaction with orderId "+orderId + " doenst exist");
    }

    @Override
    public List<OrderTransaction> findAll() {
        return orderTransactionRepository.findAll();
    }

    @Override
    public List<OrderTransaction> findAllByAccountNumber(String accountNumber) {
        return orderTransactionRepository.findAllByAccountNumber(accountNumber);
    }

    @Override
    public List<OrderTransaction> findAllByEmail(String email) {
        return orderTransactionRepository.findAllByEmail(email);
    }
}
