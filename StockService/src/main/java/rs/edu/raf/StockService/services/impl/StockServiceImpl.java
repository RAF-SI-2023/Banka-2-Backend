package rs.edu.raf.StockService.services.impl;

import org.springframework.stereotype.Service;
import rs.edu.raf.StockService.data.entities.Stock;
import rs.edu.raf.StockService.repositories.StockRepository;
import rs.edu.raf.StockService.services.StockService;

import java.util.List;

@Service
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    public StockServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public List<Stock> findAll() {
        return stockRepository.findAll();
    }

    @Override
    public Stock findById(Long id) {
        return stockRepository.findById(id).orElse(null);
    }

    @Override
    public List<Stock> findBySymbol(String symbol) {
        return stockRepository.findStocksBySymbol(symbol);
    }
}
