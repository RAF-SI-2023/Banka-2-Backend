package rs.edu.raf.StockService.bootstrap.readers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.StockService.bootstrap.exchange.BootstrapExchange;
import rs.edu.raf.StockService.data.entities.Forex;
import rs.edu.raf.StockService.data.entities.Stock;
import rs.edu.raf.StockService.repositories.ForexRepository;
import rs.edu.raf.StockService.repositories.StockRepository;

import java.util.List;

@Component
public class ListingLoaders implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(BootstrapExchange.class);
    private final StockRepository stockRepository;
    private final ForexRepository forexRepository;
    @Value("${iexToken}")
    private String iexToken;
    @Value("${finnhubToken}")
    private String finnhubToken;

    public ListingLoaders(StockRepository stockRepository, ForexRepository forexRepository) {
        this.stockRepository = stockRepository;
        this.forexRepository = forexRepository;
    }

    private void loadStocks() {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        String[] collections = {
                "Technology",
                "Transportation",
                "Finance",
                "Government",
                "Communications",
                "Information",
                "Manufacturing"
        };

        String iexApiUrl, response;

        for (String collection : collections) {
            iexApiUrl = "https://api.iex.cloud/v1/data/core/stock_collection/sector?collectionName=" + collection + "&token=" + iexToken;
            response = restTemplate.getForObject(iexApiUrl, String.class);

            try {
                JsonNode jsonNode = objectMapper.readTree(response);
                for (JsonNode node : jsonNode) {
                    JsonNode symbolNode = node.get("symbol");
                    if (symbolNode != null && !symbolNode.isNull()) {
                        String symbol = symbolNode.asText();

                        String description = node.has("companyName") ? node.get("companyName").asText() : "";
                        String exchange = node.has("primaryExchange") ? node.get("primaryExchange").asText() : "";
                        String lastRefresh = node.has("latestTime") ? node.get("latestTime").asText() : "";
                        double latestPrice = node.has("latestPrice") ? node.get("latestPrice").asDouble() : 0.0;
                        double high = node.has("high") ? node.get("high").asDouble() : 0.0;
                        double low = node.has("low") ? node.get("low").asDouble() : 0.0;
                        double change = node.has("change") ? node.get("change").asDouble() : 0.0;
                        double yield = node.has("ytdChange") ? node.get("ytdChange").asDouble() : 0.0;
                        long volume = node.has("avgTotalVolume") ? node.get("avgTotalVolume").asLong() : 0;

                        Stock stock = new Stock();
                        stock.setSymbol(symbol);
                        stock.setDescription(description);
                        stock.setExchange(exchange);
                        stock.setPrice(latestPrice);
                        stock.setHigh(high);
                        stock.setLow(low);
                        stock.setLow(change);
                        stock.setShares(0);
                        stock.setYield(yield);

                        stockRepository.save(stock);
                    }

                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        }

    }

    private void loadForexes() {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        String finnHubApi = "https://finnhub.io/api/v1/forex/exchange?token=" + finnhubToken;
        String response = restTemplate.getForObject(finnHubApi, String.class);
        String iexApiUrl;
        int i = 0;
        int limit = 30;

        try {
            List<String> exchanges = objectMapper.readValue(response, List.class);

            for (String exchange : exchanges) {
                if (exchange.contains(" ")) {
                    exchange = exchange.replace(" ", "+");
                }

                finnHubApi = "https://finnhub.io/api/v1/forex/symbol?exchange=" + exchange + "&token=" + finnhubToken;
                response = restTemplate.getForObject(finnHubApi, String.class);
                JsonNode rootNode = objectMapper.readTree(response);

                for (JsonNode node : rootNode) {
                    if (i >= limit)
                        break;

                    String symbol = node.get("displaySymbol").asText();
                    String description = node.get("description").asText();

                    String[] currencies = symbol.split("/");

                    String baseCurrency = currencies[0];
                    String quoteCurrency = currencies[1];

                    iexApiUrl = "https://api.iex.cloud/v1/fx/convert?symbols=" + symbol.replace("/", "") + "&amount=73&token=" + iexToken;
                    response = restTemplate.getForObject(iexApiUrl, String.class);

                    JsonNode rootNode1 = objectMapper.readTree(response);

                    for (JsonNode node1 : rootNode1) {
                        String rate = node1.get("rate").asText();
                        String price = node1.get("amount").asText();
                        String lastRefresh = node1.get("timestamp").asText();

                        if (!rate.equals("null")) {
                            Forex forex = new Forex();

                            forex.setSymbol(symbol);
                            forex.setDescription(description);
                            forex.setExchange(exchange);
                            forex.setPrice(Double.valueOf(price));
                            forex.setLastRefresh(Long.valueOf(lastRefresh));
                            forex.setBaseCurrency(baseCurrency);
                            forex.setQuoteCurrency(quoteCurrency);
                            i++;

                            forexRepository.save(forex);
                        }
                    }

                    try {
                        // sleep to avoid error: 429
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("DATA LOADING IN PROGRESS...");

//        loadStocks();
//        loadForexes();

        logger.info("DATA LOADING FINISHED...");
    }

}
