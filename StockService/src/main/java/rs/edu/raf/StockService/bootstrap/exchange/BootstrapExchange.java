package rs.edu.raf.StockService.bootstrap.exchange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rs.edu.raf.StockService.bootstrap.readers.ExchangeCsvReader;
import rs.edu.raf.StockService.data.entities.Exchange;
import rs.edu.raf.StockService.services.impl.InMemoryExchangeServiceImpl;

import java.util.List;

@Component
public class BootstrapExchange implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(BootstrapExchange.class);
    @Autowired
    private final InMemoryExchangeServiceImpl exchangeService;

    public BootstrapExchange(InMemoryExchangeServiceImpl exchangeService) {
        this.exchangeService = exchangeService;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("DATA LOADING IN PROGRESS...");
        ExchangeCsvReader exchangeCsvReader = new ExchangeCsvReader();
        List<Exchange> exchanges = exchangeCsvReader.loadExchangeCsv();
        exchangeService.setExchangeList(exchanges);
        logger.info("DATA LOADING FINISHED...");
    }
}
