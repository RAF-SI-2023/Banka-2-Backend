package rs.edu.raf.StockService.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rs.edu.raf.StockService.bootstrap.readers.CurrencyCsvReader;
import rs.edu.raf.StockService.data.entities.Currency;
import rs.edu.raf.StockService.data.entities.CurrencyInflation;
import rs.edu.raf.StockService.repositories.CurrencyInflationRepository;
import rs.edu.raf.StockService.repositories.CurrencyRepository;
import rs.edu.raf.StockService.services.impl.InMemoryCurrencyInflationServiceImpl;
import rs.edu.raf.StockService.services.impl.InMemoryCurrencyServiceImpl;

import java.util.List;

@Component
public class BootstrapData implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(BootstrapData.class);

    private final CurrencyRepository currencyRepository;

    private final CurrencyInflationRepository currencyInflationRepository;

    private final InMemoryCurrencyServiceImpl currencyService;

    private final InMemoryCurrencyInflationServiceImpl currencyInflationService;

    public BootstrapData(CurrencyRepository currencyRepository,
                         CurrencyInflationRepository currencyInflationRepository,
                         InMemoryCurrencyServiceImpl currencyService,
                         InMemoryCurrencyInflationServiceImpl currencyInflationService) {
        this.currencyRepository = currencyRepository;
        this.currencyInflationRepository = currencyInflationRepository;
        this.currencyService = currencyService;
        this.currencyInflationService = currencyInflationService;
    }

    /**
     * trenutno radi sa in-memory listom, a ne sa bazom, ako se bude menjalo, promeniti samo koji servis se koristi
     */
    @Override
    public void run(String... args) {
        logger.info("DATA LOADING IN PROGRESS...");
        List<Currency> currencyList = CurrencyCsvReader.loadCurrencyData();
        List<CurrencyInflation> currencyInflationList = CurrencyCsvReader.pullCurrencyInflationData(currencyList);
        /*
         * otkomentarisati ako cemo koristiti bazu, a ne in memory listu , slicno i za currencyInflation
         currencyRepository.saveAll(currencyList);    */
        currencyService.setCurrencyList(currencyList);
        currencyInflationService.setCurrencyInflationList(currencyInflationList);
        logger.info("DATA LOADING FINISHED...");
    }
}
