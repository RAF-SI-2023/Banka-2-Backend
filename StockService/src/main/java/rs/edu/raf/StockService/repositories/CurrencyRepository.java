package rs.edu.raf.StockService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.edu.raf.StockService.data.entities.Currency;

import java.util.List;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Currency findByCurrencyCode(String currencyCode);

    Currency findByCurrencyName(String currencyName);

    //jer u suprotnom treba previse vremena da povuce sve podatke, a to nikom nije fun

    /**
     * Used for explicitely findALl method, without inflationList, because it is not needed in some cases and because of performance
     *
     * @return List of all currencies without inflationList
     */
    @Query("SELECT new Currency(c.currencyCode,c.currencyName,c.currencyPolity,c.currencySymbol) FROM Currency c")
    List<Currency> findAllWithoutInflation();
}
