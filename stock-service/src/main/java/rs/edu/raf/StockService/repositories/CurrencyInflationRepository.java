package rs.edu.raf.StockService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.StockService.data.entities.CurrencyInflation;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyInflationRepository extends JpaRepository<CurrencyInflation, Long> {
    Optional<List<CurrencyInflation>> findByCurrencyId(long currencyId);

    Optional<CurrencyInflation> findByCurrencyIdAndYear(long currencyId, long year);


}
