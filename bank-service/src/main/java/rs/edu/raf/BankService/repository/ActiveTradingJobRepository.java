package rs.edu.raf.BankService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.BankService.data.entities.ActiveTradingJob;

import java.util.Optional;

@Repository
public interface ActiveTradingJobRepository extends JpaRepository<ActiveTradingJob, Long> {
    boolean deleteActiveTradingJobByOrderId(long orderId);

    Optional<ActiveTradingJob> findActiveTradingJobByOrderId(long orderId);
}
