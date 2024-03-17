package rs.edu.raf.StockService.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import rs.edu.raf.StockService.bootstrap.readers.CurrencyCsvReader;
import rs.edu.raf.StockService.data.entities.Currency;
import rs.edu.raf.StockService.data.entities.CurrencyInflation;
import rs.edu.raf.StockService.data.entities.Option;
import rs.edu.raf.StockService.data.enums.OptionType;
import rs.edu.raf.StockService.repositories.CurrencyInflationRepository;
import rs.edu.raf.StockService.repositories.CurrencyRepository;
import rs.edu.raf.StockService.repositories.OptionRepository;
import rs.edu.raf.StockService.services.impl.InMemoryCurrencyInflationServiceImpl;
import rs.edu.raf.StockService.services.impl.InMemoryCurrencyServiceImpl;
import rs.edu.raf.StockService.services.impl.OptionServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Component
public class BootstrapData implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(BootstrapData.class);

    private final CurrencyRepository currencyRepository;

    private final CurrencyInflationRepository currencyInflationRepository;

    private final InMemoryCurrencyServiceImpl currencyService;

    private final InMemoryCurrencyInflationServiceImpl currencyInflationService;
    private final ResourceLoader resourceLoader;

    private final OptionServiceImpl optionServiceImpl;

    private final OptionRepository optionRepository;

    public BootstrapData(CurrencyRepository currencyRepository,
                         CurrencyInflationRepository currencyInflationRepository,
                         InMemoryCurrencyServiceImpl currencyService,
                         InMemoryCurrencyInflationServiceImpl currencyInflationService, ResourceLoader resourceLoader, OptionServiceImpl optionServiceImpl, OptionRepository optionRepository) {
        this.currencyRepository = currencyRepository;
        this.currencyInflationRepository = currencyInflationRepository;
        this.currencyService = currencyService;
        this.currencyInflationService = currencyInflationService;
        this.resourceLoader = resourceLoader;
        this.optionServiceImpl = optionServiceImpl;
        this.optionRepository = optionRepository;
    }

    /**
     * trenutno radi sa in-memory listom, a ne sa bazom, ako se bude menjalo, promeniti samo koji servis se koristi
     */
    @Override
    public void run(String... args) {
        logger.info("DATA LOADING IN PROGRESS...");
        CurrencyCsvReader currencyCsvReader = new CurrencyCsvReader(resourceLoader);
        List<Currency> currencyList = currencyCsvReader.loadCurrencyData();
        List<CurrencyInflation> currencyInflationList = currencyCsvReader.pullCurrencyInflationData(currencyList);
        /*
         * otkomentarisati ako cemo koristiti bazu, a ne in memory listu , slicno i za currencyInflation
         currencyRepository.saveAll(currencyList);    */
        currencyService.setCurrencyList(currencyList);
        currencyInflationService.setCurrencyInflationList(currencyInflationList);

        LocalDate currentDate = LocalDate.now();
        LocalDateTime localDateTime = currentDate.atStartOfDay();
        long milliseconds = localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
        for (int i = 1; i <=3 ; i++) {
            Option option = new Option();
            option.setStockListing("STOCK" + i);
            option.setOptionType(OptionType.PUT);
            option.setStrikePrice("100" + i);
            option.setImpliedVolatility("420" + i);
            option.setOpenInterest(120+"i");
            option.setSettlementDate(milliseconds);

            optionRepository.save(option);

        }
        Option option = new Option();
        option.setStockListing("STOCK" + 1);
        option.setOptionType(OptionType.PUT);
        option.setStrikePrice("100" + 1);
        option.setImpliedVolatility("420" + 1);
        option.setOpenInterest(120+"i");
        option.setSettlementDate(milliseconds);
        optionRepository.save(option);
        logger.info("DATA LOADING FINISHED...");
    }
}
