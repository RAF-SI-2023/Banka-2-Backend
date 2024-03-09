package rs.edu.raf.StockService.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.edu.raf.StockService.data.entities.Currency;
import rs.edu.raf.StockService.repositories.CurrencyRepository;
import rs.edu.raf.StockService.services.CurrencyService;

import java.util.List;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    @Autowired
    private final CurrencyRepository currencyRepository;

    private List<Currency> currencyList;

    public CurrencyServiceImpl(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;

    }
    //TODO neke metode findAll, findById, findByCurrencyCode, findByCurrencyName

    //write findAll method
    public List<Currency> findAll() {
        return currencyRepository.findAll();
    }

    public Currency findById(Long id) {
        return currencyRepository.findById(id).orElse(null);
    }

    public Currency findByCurrencyCode(String currencyCode) {
        return currencyRepository.findByCurrencyCode(currencyCode);
    }

    public Currency findByCurrencyName(String currencyName) {
        return currencyRepository.findByCurrencyName(currencyName);
    }

    public void setCurrencyList(List<Currency> currencyList) {
        this.currencyList = currencyList;
    }
}
