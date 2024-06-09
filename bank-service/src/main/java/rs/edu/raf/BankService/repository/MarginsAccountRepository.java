package rs.edu.raf.BankService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.edu.raf.BankService.data.entities.MarginsAccount;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarginsAccountRepository extends JpaRepository<MarginsAccount, Long> {

    Optional<MarginsAccount> findByUserId(Long userId);

    @Query("SELECT m FROM MarginsAccount m LEFT JOIN FETCH m.marginsTransactions")
    List<MarginsAccount> findAllWithTransactions();
}
