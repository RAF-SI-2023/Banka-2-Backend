package rs.edu.raf.BankService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.BankService.data.entities.MarginsAccount;

@Repository
public interface MarginsAccountRepository extends JpaRepository<MarginsAccount, Long> {
}
