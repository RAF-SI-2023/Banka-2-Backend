package rs.edu.raf.BankService.service;

import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.data.dto.*;
import rs.edu.raf.BankService.data.entities.Order;

@Service
public interface StockService {


    ForexDto getForexBySymbol(String symbol);

    StockDto getStockBySymbol(String symbol);

    ListingDto getSecuritiesByOrder(Order order);

    ExchangeDto getExchangeExchangeAcronym(String exchangeAcronym);


}
