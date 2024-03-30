package rs.edu.raf.StockService.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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

    /**
     * finds all currencies without their inflationLists, because of performance
     *
     * @return List of all currencies without inflationList
     */
    @Cacheable(value = "currency")
    public List<Currency> findAll() {
        return currencyRepository.findAllWithoutInflation();
    }

    @Cacheable(value = "currencyId", key = "#id")
    public Currency findById(Long id) {
        System.out.println("CurrencyServiceImpl.findById" + id);
        Currency currency = currencyRepository.findById(id).orElse(null);
        //zbog lazy load, da bi fetchovali, porblem kod kesiranja
        if (currency != null) {
            currency.getInflationList().size(); // Fetch the collection
        }
        return currency;
    }

    @Cacheable(value = "currencyCode", key = "#currencyCode")
    public Currency findByCurrencyCode(String currencyCode) {
        Currency currency = currencyRepository.findByCurrencyCode(currencyCode);
        System.out.println("CurrencyServiceImpl.findByCurrencyCode" + currencyCode);
        if (currency != null) {
            currency.getInflationList().size(); // Fetch the collection
        }
        return currency;
    }

    @Cacheable(value = "currencyName", key = "#currencyName")
    public Currency findByCurrencyName(String currencyName) {
        Currency currency = currencyRepository.findByCurrencyName(currencyName);
        if (currency != null) {
            currency.getInflationList().size(); // Fetch the collection
        }
        return currency;
    }


}
