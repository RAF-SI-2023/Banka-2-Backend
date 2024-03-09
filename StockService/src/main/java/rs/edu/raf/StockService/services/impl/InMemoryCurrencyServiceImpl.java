package rs.edu.raf.StockService.services.impl;

import org.springframework.stereotype.Service;
import rs.edu.raf.StockService.data.entities.Currency;
import rs.edu.raf.StockService.services.CurrencyService;

import java.util.List;

@Service
public class InMemoryCurrencyServiceImpl implements CurrencyService {
    private List<Currency> currencyList;

    public void setCurrencyList(List<Currency> currencyList) {
        this.currencyList = currencyList;
    }


    public List<Currency> findAll() {
        return currencyList;
    }

    public Currency findById(Long id) {
        return currencyList.stream().filter(currency -> currency.getId() == (id)).findFirst().orElse(null);
    }

    public Currency findByCurrencyCode(String currencyCode) {
        return currencyList.stream().filter(currency -> currency.getCurrencyCode().equals(currencyCode)).findFirst().orElse(null);
    }

    public Currency findByCurrencyName(String currencyName) {
        return currencyList.stream().filter(currency -> currency.getCurrencyName().equals(currencyName)).findFirst().orElse(null);
    }
}
