package rs.edu.raf.StockService.services.impl;

import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import rs.edu.raf.StockService.data.dto.SecuritiesPriceDto;
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
        return stockRepository.findById(id).orElseThrow(() -> new NotFoundException("Stock with id: " + id + "not found"));
    }

    @Override
    public List<Stock> findBySymbolDEPRICATED(String symbol) {
        return stockRepository.findStocksBySymbol(symbol);
    }

    @Override
    public Stock findBySymbol(String symbol) {
        Stock stock = stockRepository.findBySymbol(symbol);
        if (stock == null) {
            throw new NotFoundException("Stock with symbol: " + symbol + "not found");
        }
        return stock;
    }

    @Override
    public SecuritiesPriceDto findCurrentPriceBySymbol(String symbol) {
        Stock stock = stockRepository.findBySymbol(symbol);
        if (stock == null) {
            throw new NotFoundException("Stock with symbol: " + symbol + "not found");
        }
        return new SecuritiesPriceDto(stock.getPrice(), stock.getHigh(), stock.getLow());
    }
}
