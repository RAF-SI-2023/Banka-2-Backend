package rs.edu.raf.StockService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.StockService.data.entities.Option;
import rs.edu.raf.StockService.data.enums.OptionType;

import java.util.List;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {

    Option findByOptionType(OptionType optionType);

    Option findByStockListing(String stockListing);

    List<Option> findAllByStockListing(String stockListing);

}
