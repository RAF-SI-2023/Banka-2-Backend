package rs.edu.raf.StockService.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rs.edu.raf.StockService.bootstrap.readers.CurrencyCsvReader;
import rs.edu.raf.StockService.data.entities.Currency;
import rs.edu.raf.StockService.repositories.CurrencyInflationRepository;
import rs.edu.raf.StockService.repositories.CurrencyRepository;
import rs.edu.raf.StockService.services.impl.InMemoryCurrencyServiceImpl;

import java.util.List;

@Component
public class BootstrapData implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(BootstrapData.class);
    @Autowired
    private final CurrencyRepository currencyRepository;
    @Autowired
    private final CurrencyInflationRepository currencyInflationRepository;

    @Autowired
    private final InMemoryCurrencyServiceImpl currencyService;

    public BootstrapData(CurrencyRepository currencyRepository, CurrencyInflationRepository currencyInflationRepository, InMemoryCurrencyServiceImpl currencyService) {
        this.currencyRepository = currencyRepository;
        this.currencyInflationRepository = currencyInflationRepository;
        this.currencyService = currencyService;
    }

    /**
     * trenutno radi sa in-memory listom, a ne sa bazom, ako se bude menjalo, promeniti samo koji servis se koristi
     */
    @Override
    public void run(String... args) throws Exception {
        logger.info("DATA LOADING IN PROGRESS...");
        List<Currency> currencyList = CurrencyCsvReader.loadCurrencyData();
        // otkomentarisati ako cemo koristiti bazu, a ne in memory listu
        //   currencyRepository.saveAll(currencyList);
        currencyService.setCurrencyList(currencyList);
        logger.info("DATA LOADING FINISHED...");
    }
}
