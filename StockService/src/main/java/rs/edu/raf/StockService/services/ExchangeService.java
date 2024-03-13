package rs.edu.raf.StockService.services;

import rs.edu.raf.StockService.data.entities.Exchange;

import java.util.List;

public interface ExchangeService {
    List<Exchange> findAll();

    Exchange findById(Long id);

    Exchange findByExchangeName(String exchangeName);

    Exchange findByMICode(String miCode);
}
