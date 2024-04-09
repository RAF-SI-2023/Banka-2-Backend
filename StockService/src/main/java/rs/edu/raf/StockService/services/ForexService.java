package rs.edu.raf.StockService.services;

import rs.edu.raf.StockService.data.entities.Forex;

import java.util.List;

public interface ForexService {
    List<Forex> findAll();

    Forex findById(Long id);

    List<Forex> findByBaseCurrency(String baseCurrency);

    List<Forex> findByQuoteCurrency(String quoteCurrency);
}
