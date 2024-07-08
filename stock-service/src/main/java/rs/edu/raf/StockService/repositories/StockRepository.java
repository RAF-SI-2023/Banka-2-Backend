package rs.edu.raf.StockService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.StockService.data.entities.Stock;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    List<Stock> findStocksBySymbol(String symbol);

    Stock findBySymbol(String symbol);
}
