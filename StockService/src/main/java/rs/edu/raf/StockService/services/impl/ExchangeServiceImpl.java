package rs.edu.raf.StockService.services.impl;

import org.springframework.stereotype.Service;
import rs.edu.raf.StockService.data.entities.Exchange;
import rs.edu.raf.StockService.repositories.ExchangeRepository;
import rs.edu.raf.StockService.services.ExchangeService;

import java.util.List;

@Service
public class ExchangeServiceImpl implements ExchangeService {

    private final ExchangeRepository exchangeRepository;

    public ExchangeServiceImpl(ExchangeRepository exchangeRepository) {
        this.exchangeRepository = exchangeRepository;
    }

    @Override
    public List<Exchange> findAll() {
        return exchangeRepository.findAll();
    }

    @Override
    public Exchange findById(Long id) {
        return exchangeRepository.findById(id).orElse(null);
    }

    @Override
    public Exchange findByExchangeName(String exchangeName) {
        return exchangeRepository.findByExchangeName(exchangeName);
    }

    @Override
    public Exchange findByMICode(String miCode) {
        return exchangeRepository.findByExchangeMICode(miCode);
    }
}
