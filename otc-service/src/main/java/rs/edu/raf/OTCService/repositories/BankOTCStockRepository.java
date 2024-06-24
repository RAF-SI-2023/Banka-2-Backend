package rs.edu.raf.OTCService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import rs.edu.raf.OTCService.data.entity.listing.BankOTCStock;

@Repository
@Transactional(isolation = Isolation.SERIALIZABLE)
public interface BankOTCStockRepository extends JpaRepository<BankOTCStock, Long> {
    BankOTCStock findByTicker(String ticker);
}
