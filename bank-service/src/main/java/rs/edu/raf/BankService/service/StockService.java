package rs.edu.raf.BankService.service;

import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.data.dto.ExchangeDto;
import rs.edu.raf.BankService.data.dto.ForexDto;
import rs.edu.raf.BankService.data.dto.ListingDto;
import rs.edu.raf.BankService.data.dto.StockDto;
import rs.edu.raf.BankService.data.entities.Order;

@Service
public interface StockService {


    ForexDto getForexBySymbol(String symbol);

    StockDto getStockBySymbol(String symbol);

    Object getSecuritiesByOrder(Order order);

    ExchangeDto getExchangeExchangeAcronym(String exchangeAcronym);


}
