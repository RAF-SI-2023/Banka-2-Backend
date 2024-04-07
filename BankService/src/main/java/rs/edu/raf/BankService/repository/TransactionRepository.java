package rs.edu.raf.BankService.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.BankService.data.entities.Account;
import rs.edu.raf.BankService.data.entities.Transaction;
import rs.edu.raf.BankService.data.enums.TransactionStatus;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
