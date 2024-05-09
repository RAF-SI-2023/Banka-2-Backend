package rs.edu.raf.BankService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.BankService.data.entities.MarginsTransaction;

@Repository
public interface MarginsTransactionRepository extends JpaRepository<MarginsTransaction, Long> {
}