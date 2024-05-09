package rs.edu.raf.StockService.services;

import rs.edu.raf.StockService.data.dto.SecuritiesPriceDto;
import rs.edu.raf.StockService.data.entities.Stock;

import java.util.List;

public interface StockService {
    List<Stock> findAll();

    Stock findById(Long id);

    List<Stock> findBySymbolDEPRICATED(String symbol);

    Stock findBySymbol(String symbol);
    SecuritiesPriceDto findCurrentPriceBySymbol(String symbol);
}
