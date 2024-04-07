package rs.edu.raf.BankService.repository.credit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.edu.raf.BankService.data.entities.Credit;

import java.util.List;

@Repository
public interface CreditRepository extends JpaRepository<Credit, Long> {


    List<Credit> findAllByAccountNumber(String accountNumber);

    Credit findCreditByCreditNumber(Long creditNumber);
}
