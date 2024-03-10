package rs.edu.raf.StockService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.StockService.data.entities.Exchange;

@Repository
public interface ExchangeRepository extends JpaRepository<Exchange, Long> {
    Exchange findByExchangeName(String exchangeName);
    Exchange findByExchangeMICode(String miCode);
}
