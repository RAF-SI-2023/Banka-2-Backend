package rs.edu.raf.StockService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.StockService.data.entities.FuturesContract;

import java.util.Optional;

@Repository
public interface FuturesContractRepository extends JpaRepository<FuturesContract, Long> {

    Optional<FuturesContract> findByName(String name);
}
