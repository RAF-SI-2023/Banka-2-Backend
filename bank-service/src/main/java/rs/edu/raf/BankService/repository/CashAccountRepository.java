package rs.edu.raf.BankService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;

import java.util.List;

@Repository
public interface CashAccountRepository extends JpaRepository<CashAccount, Long> {

    CashAccount findByAccountNumber(String accountNumber);

    List<CashAccount> findAllByEmail(String email);

    @Query("SELECT " +
            "account " +
            "FROM " +
            "CashAccount account " +
            "WHERE " +
            "((:email IS NULL AND account.ownedByBank=true) OR (account.email=:email)) AND account.isPrimaryTradingAccount=true"
    )
    CashAccount findPrimaryTradingAccount(String email);


}
