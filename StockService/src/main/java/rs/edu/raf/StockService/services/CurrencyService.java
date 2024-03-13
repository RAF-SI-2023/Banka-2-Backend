package rs.edu.raf.StockService.services;

import rs.edu.raf.StockService.data.entities.Currency;

import java.util.List;

public interface CurrencyService {

    List<Currency> findAll();

    Currency findById(Long id);

    Currency findByCurrencyCode(String currencyCode);

    Currency findByCurrencyName(String currencyName);
}
