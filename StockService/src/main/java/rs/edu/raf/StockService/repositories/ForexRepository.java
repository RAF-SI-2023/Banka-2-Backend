package rs.edu.raf.StockService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.StockService.data.entities.Forex;

@Repository
public interface ForexRepository extends JpaRepository<Forex, Long> {
    Forex findForexByBaseCurrency(String baseCurrency);

    Forex findForexByQuoteCurrency(String quoteCurrency);
}
