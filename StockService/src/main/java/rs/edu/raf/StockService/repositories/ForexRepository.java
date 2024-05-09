package rs.edu.raf.StockService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.StockService.data.entities.Forex;

import java.util.List;

@Repository
public interface ForexRepository extends JpaRepository<Forex, Long> {

    List<Forex> findForexesByBaseCurrency(String baseCurrency);
    List<Forex> findForexesByQuoteCurrency(String quoteCurrency);
    Forex findForexBySymbol(String symbol);
}
