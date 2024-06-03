package rs.edu.raf.BankService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.BankService.data.entities.profit.BankProfit;

public interface BankProfitRepository extends JpaRepository<BankProfit, Long> {
}
