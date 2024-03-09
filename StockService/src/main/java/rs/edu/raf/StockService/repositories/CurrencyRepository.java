package rs.edu.raf.StockService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.StockService.data.entities.Currency;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Currency findByCurrencyCode(String currencyCode);

    Currency findByCurrencyName(String currencyName);

}
