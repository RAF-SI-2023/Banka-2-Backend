package rs.edu.raf.StockService.services;

import rs.edu.raf.StockService.data.entities.Currency;

import java.util.List;

public interface CurrencyService {

    public List<Currency> findAll();

    public Currency findById(Long id);

    public Currency findByCurrencyCode(String currencyCode);

    public Currency findByCurrencyName(String currencyName);
}
