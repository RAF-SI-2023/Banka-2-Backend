package rs.edu.raf.StockService.services.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import rs.edu.raf.StockService.data.entities.Exchange;
import rs.edu.raf.StockService.services.ExchangeService;

import java.util.List;

@Service
@Primary
public class InMemoryExchangeServiceImpl implements ExchangeService {

    private List<Exchange> exchanges;

    public void setExchangeList(List<Exchange> exchanges) {
        this.exchanges = exchanges;
    }

    @Override
    public List<Exchange> findAll() {
        return exchanges;
    }

    @Override
    public Exchange findById(Long id) {
        return exchanges.stream().filter(currency -> currency.getId() == (id)).findFirst().orElseThrow(() -> new NotFoundException("Exchange with id: " + id + "not found"));
    }

    @Override
    public Exchange findByExchangeName(String exchangeName) {
        return exchanges.stream().filter(currency -> currency.getExchangeName().equals(exchangeName)).findFirst().orElseThrow(() -> new NotFoundException("Exchange with exchangeName: " + exchangeName + "not found"));
    }

    @Override
    public Exchange findByMICode(String miCode) {
        return exchanges.stream().filter(currency -> currency.getExchangeMICode().equals(miCode)).findFirst().orElseThrow(() -> new NotFoundException("Exchange with miCode: " + miCode + "not found"));
    }
}
