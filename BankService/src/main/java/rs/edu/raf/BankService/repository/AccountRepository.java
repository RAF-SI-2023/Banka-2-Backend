package rs.edu.raf.BankService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<CashAccount, Long> {

    CashAccount findByAccountNumber(String accountNumber);

    List<CashAccount> findAllByEmail(String email);
}
