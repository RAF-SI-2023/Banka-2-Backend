package rs.edu.raf.BankService.service;

import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.data.dto.*;
import rs.edu.raf.BankService.data.entities.Order;
import rs.edu.raf.BankService.data.enums.ListingType;

@Service
public interface StockService {


    ForexDto getForexById(Long id);

    StockDto getStockById(Long id);

    ListingDto getSecuritiesByOrder(Order order);

    ExchangeDto getExchangeExchangeAcronym(String exchangeAcronym);


}
