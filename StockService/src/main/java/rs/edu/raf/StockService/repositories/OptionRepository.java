package rs.edu.raf.StockService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.edu.raf.StockService.data.entities.Option;
import rs.edu.raf.StockService.data.enums.OptionType;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {

    Option findByOptionType(OptionType optionType);

    Option findByStockListing(String stockListing);

   // Option findByStockListingStrikePrice(String stockListing);

    @Query("SELECT o FROM Option o WHERE o.stockListing = :stockListing")
    List<Option> findAllByStockListing(@Param("stockListing") String stockListing);

    @Query("SELECT o FROM Option o WHERE o.stockListing = :#{#option.stockListing} " +
            "AND o.settlementDate = :#{#option.settlementDate} " +
            "AND o.strikePrice = :#{#option.strikePrice} " +
            "AND o.optionType = :#{#option.optionType}")
    Optional<Option> findOption(@Param("option") Option option);
}
