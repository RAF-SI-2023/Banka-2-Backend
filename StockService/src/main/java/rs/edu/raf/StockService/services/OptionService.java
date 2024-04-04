package rs.edu.raf.StockService.services;

import rs.edu.raf.StockService.data.entities.Option;

import java.util.List;
import java.util.Optional;

public interface OptionService {

    /**
     * Videti da li moze po jos nekim metodama da se pretrazuje, da li je potrebno i slicno.
     */

    List<Option> findAll();

    List<Option> findAllByStockListing(String stockListing);

    Option findById(Long id);

    Option findByStockListing(String stockListing);

    List<Option> loadOptions(String stockListing);

    public void checkIfOptionExistsAndUpdate(Option option);
}
