package rs.edu.raf.StockService.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.edu.raf.StockService.data.entities.CurrencyInflation;
import rs.edu.raf.StockService.repositories.CurrencyInflationRepository;

import java.util.List;

@Service
public class CurrencyInflationService {
    @Autowired
    private final CurrencyInflationRepository currencyInflationRepository;

    public CurrencyInflationService(CurrencyInflationRepository currencyInflationRepository) {
        this.currencyInflationRepository = currencyInflationRepository;
    }

    //TODO metode neke
    // findInflationByCurrencyId, findInflationByCurrencyIdAndYear
    public List<CurrencyInflation> findInflationByCurrencyId(long currencyId) {
        return currencyInflationRepository.findByCurrencyId(currencyId).orElse(null);
    }

    public CurrencyInflation findInflationByCurrencyIdAndYear(long currencyId, long year) {
        return currencyInflationRepository.findByCurrencyIdAndYear(currencyId, year).orElse(null);
    }

}
