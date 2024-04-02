package rs.edu.raf.StockService.bootstrap.readers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.StockService.bootstrap.exchange.BootstrapExchange;
import rs.edu.raf.StockService.repositories.ForexRepository;
import rs.edu.raf.StockService.repositories.StockRepository;

public class ListingLoaders implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(BootstrapExchange.class);
    private final StockRepository stockRepository;
    private final ForexRepository forexRepository;

    public ListingLoaders(StockRepository stockRepository, ForexRepository forexRepository) {
        this.stockRepository = stockRepository;
        this.forexRepository = forexRepository;
    }

    private void loadStocks() {
        // IEX api key:
        String iexApiUrl = "https://api.iex.cloud/v1/data/CORE/STOCK_COLLECTION/list?collectionName=mostactive&token=";

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(iexApiUrl, String.class);

        System.out.println(response);

        // alpha_vintage_api_key:
        String vintageApiUrl = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=NVDA&apikey=";
        response = restTemplate.getForObject(vintageApiUrl, String.class);

        System.out.println(response);
    }

    private void loadForexes() {
        // Forex berze - Finnhub API
        String finnHubApi = "https://finnhub.io/api/v1/forex/exchange?token=";

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(finnHubApi, String.class);

        System.out.println(response);

        // Forex parovi - Finnhub API
        finnHubApi = "https://finnhub.io/api/v1/forex/symbol?exchange=oanda&token=";
        response = restTemplate.getForObject(finnHubApi, String.class);

        System.out.println(response);

    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("DATA LOADING IN PROGRESS...");

//        loadStocks();
        loadForexes();

        logger.info("DATA LOADING FINISHED...");
    }

}
