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
import rs.edu.raf.StockService.repositories.CurrencyInflationRepository;
import rs.edu.raf.StockService.repositories.CurrencyRepository;
import rs.edu.raf.StockService.services.CurrencyInflationService;
import rs.edu.raf.StockService.services.CurrencyService;
import rs.edu.raf.StockService.services.impl.CurrencyInflationServiceImpl;
import rs.edu.raf.StockService.services.impl.CurrencyServiceImpl;
import rs.edu.raf.StockService.services.impl.InMemoryCurrencyInflationServiceImpl;
import rs.edu.raf.StockService.services.impl.InMemoryCurrencyServiceImpl;

import java.util.ArrayList;
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


    public BootstrapData(CurrencyRepository currencyRepository,
                         CurrencyInflationRepository currencyInflationRepository,
                         ResourceLoader resourceLoader,
                         RedisTemplate<String, Currency> redisTemplate,
                         RedisTemplate<String, CurrencyInflation> redisCurrencyInflation) {
        this.currencyRepository = currencyRepository;
        this.currencyInflationRepository = currencyInflationRepository;
        this.resourceLoader = resourceLoader;
        this.redisCurrency = redisTemplate;
        this.redisCurrencyInflation = redisCurrencyInflation;
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
