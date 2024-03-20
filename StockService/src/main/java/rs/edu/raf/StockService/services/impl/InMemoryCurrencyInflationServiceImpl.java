package rs.edu.raf.StockService.services.impl;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import rs.edu.raf.StockService.data.entities.CurrencyInflation;
import rs.edu.raf.StockService.services.CurrencyInflationService;

import java.util.List;

@Service
public class InMemoryCurrencyInflationServiceImpl implements CurrencyInflationService {

    List<CurrencyInflation> currencyInflationList;

    public void setCurrencyInflationList(List<CurrencyInflation> currencyInflationList) {
        this.currencyInflationList = currencyInflationList;
    }

    @Cacheable(value = "currencyInflation", key = "#currencyId")
    @Override
    public List<CurrencyInflation> findInflationByCurrencyId(long currencyId) {
        return currencyInflationList.stream().filter(currencyInflation -> currencyInflation.getCurrencyId() == currencyId).toList();
    }

    @Cacheable(value = "currencyInflation", key = "#currencyId")
    @Override
    public CurrencyInflation findInflationByCurrencyIdAndYear(long currencyId, long year) {

        return currencyInflationList.stream().filter(currencyInflation -> currencyInflation.getCurrencyId() == currencyId && currencyInflation.getYear() == year).findFirst().orElse(null);
    }


}
