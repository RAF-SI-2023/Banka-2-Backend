package rs.edu.raf.StockService.services;

import rs.edu.raf.StockService.data.entities.Stock;

import java.util.List;

public interface StockService {
    List<Stock> findAll();

    Stock findById(Long id);

    List<Stock> findBySymbol(String symbol);
}
