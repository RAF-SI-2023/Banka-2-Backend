package rs.edu.raf.StockService.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.core.RedisTemplate;
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
import java.util.stream.Collectors;

@Component
public class BootstrapData implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(BootstrapData.class);

    private final CurrencyRepository currencyRepository;

    private final CurrencyInflationRepository currencyInflationRepository;

    private final ResourceLoader resourceLoader;
    /**
     * za redis, ne koristiti ovde najverovatnije
     */
    private final RedisTemplate<String, Currency> redisCurrency;
    private final RedisTemplate<String, CurrencyInflation> redisCurrencyInflation;


    private final OptionServiceImpl optionServiceImpl;

    private final OptionRepository optionRepository;

    public BootstrapData(CurrencyRepository currencyRepository,
                         CurrencyInflationRepository currencyInflationRepository,
                         RedisTemplate<String, Currency> redisTemplate,
                         RedisTemplate<String, CurrencyInflation> redisCurrencyInflation,
                         ResourceLoader resourceLoader,
                         OptionServiceImpl optionServiceImpl,
                         OptionRepository optionRepository) {
        this.currencyRepository = currencyRepository;
        this.currencyInflationRepository = currencyInflationRepository;
        this.resourceLoader = resourceLoader;
        this.redisCurrency = redisTemplate;
        this.redisCurrencyInflation = redisCurrencyInflation;
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
        List<Currency> currencyList;
        List<CurrencyInflation> currencyInflationList;
         /* REDIS
          cacheCurrencies(currencyList);
        cacheCurrencyInflation(currencyInflationList);*/
        if (currencyInflationRepository.count() == 0 || currencyRepository.count() == 0) {
            currencyList = currencyCsvReader.loadCurrencyData();
            currencyList = currencyRepository.saveAll(currencyList);
            currencyInflationList = currencyCsvReader.pullCurrencyInflationData(currencyList);
            currencyInflationRepository.saveAll(currencyInflationList);
        }
        /*za in memory
        //  currencyService.setCurrencyList(currencyList);
        //  currencyInflationService.setCurrencyInflationList(currencyInflationList);
        */

     /*
         * otkomentarisati ako cemo koristiti bazu, a ne in memory listu , slicno i za currencyInflation
         currencyRepository.saveAll(currencyList);    */

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

    //**Iskljucivo za redis, da se sve odmah kesira, nema nekog smisla.*/
 /*   private void cacheCurrencies(List<Currency> currencies) {
        for (Currency currency : currencies) {
            redisCurrency.opsForValue().set("currency: code=" + currency.getCurrencyCode() + " id=" + currency.getId(), currency);
        }
    }

    private void cacheCurrencyInflation(List<CurrencyInflation> currencyInflations) {
        for (CurrencyInflation currencyInflation : currencyInflations) {
            redisCurrencyInflation.opsForValue().set("currencyInflation:id=" + (currencyInflation.getId()) + " currencyId=" + currencyInflation.getCurrencyId(), currencyInflation);
        }
    }*/
}
