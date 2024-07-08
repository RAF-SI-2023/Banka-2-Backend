package rs.edu.raf.StockService.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import rs.edu.raf.StockService.bootstrap.readers.CurrencyCsvReader;
import rs.edu.raf.StockService.bootstrap.readers.FuturesContractCsvReader;
import rs.edu.raf.StockService.data.entities.Currency;
import rs.edu.raf.StockService.data.entities.CurrencyInflation;
import rs.edu.raf.StockService.data.entities.FuturesContract;
import rs.edu.raf.StockService.repositories.*;
import rs.edu.raf.StockService.services.impl.OptionServiceImpl;

import java.util.List;

@Component
public class BootstrapData implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(BootstrapData.class);

    private final CurrencyRepository currencyRepository;

    private final CurrencyInflationRepository currencyInflationRepository;

    private final FuturesContractRepository futuresContractRepository;

    private final ResourceLoader resourceLoader;
    /**
     * za redis, ne koristiti ovde najverovatnije
     */
    private final RedisTemplate<String, Currency> redisCurrency;
    private final RedisTemplate<String, CurrencyInflation> redisCurrencyInflation;


    private final OptionServiceImpl optionServiceImpl;

    private final OptionRepository optionRepository;

    private final StockRepository stockRepository;

    private final ForexRepository forexRepository;

    public BootstrapData(CurrencyRepository currencyRepository,
                         CurrencyInflationRepository currencyInflationRepository, FuturesContractRepository futuresContractRepository,
                         RedisTemplate<String, Currency> redisTemplate,
                         RedisTemplate<String, CurrencyInflation> redisCurrencyInflation,
                         ResourceLoader resourceLoader,
                         OptionServiceImpl optionServiceImpl,
                         OptionRepository optionRepository,
                         StockRepository stockRepository,
                         ForexRepository forexRepository) {
        this.currencyRepository = currencyRepository;
        this.currencyInflationRepository = currencyInflationRepository;
        this.futuresContractRepository = futuresContractRepository;
        this.resourceLoader = resourceLoader;
        this.redisCurrency = redisTemplate;
        this.redisCurrencyInflation = redisCurrencyInflation;
        this.optionServiceImpl = optionServiceImpl;
        this.optionRepository = optionRepository;
        this.stockRepository = stockRepository;
        this.forexRepository = forexRepository;
    }

    /**
     * trenutno radi sa in-memory listom, a ne sa bazom, ako se bude menjalo, promeniti samo koji servis se koristi
     */
    @Override
    public void run(String... args) {
        try {
            logger.info("StockService: SOME DATA LOADING IN PROGRESS...");
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

            if (futuresContractRepository.count() == 0) {
                FuturesContractCsvReader futuresContractCsvReader = new FuturesContractCsvReader(resourceLoader);
                List<FuturesContract> futuresContracts = futuresContractCsvReader.readFromFile();
                futuresContractRepository.saveAll(futuresContracts);
            }

            logger.info("StockService: SOME DATA LOADING FINISHED...");
        } catch (Exception e) {
            e.printStackTrace();
        }

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
