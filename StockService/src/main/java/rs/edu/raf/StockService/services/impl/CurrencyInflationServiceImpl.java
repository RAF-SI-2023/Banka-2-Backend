package rs.edu.raf.StockService.services.impl;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import rs.edu.raf.StockService.data.entities.CurrencyInflation;
import rs.edu.raf.StockService.repositories.CurrencyInflationRepository;
import rs.edu.raf.StockService.services.CurrencyInflationService;

import java.util.List;

@Service
public class CurrencyInflationServiceImpl implements CurrencyInflationService {


    private final CurrencyInflationRepository currencyInflationRepository;

    /**
     * videti koje metode ovde jos mogu da se pozivaju, koje su potrebne i slicno.
     */
    public CurrencyInflationServiceImpl(CurrencyInflationRepository currencyInflationRepository) {
        this.currencyInflationRepository = currencyInflationRepository;
    }

    @Cacheable(value = "currencyInflationId", key = "#currencyId")
    public List<CurrencyInflation> findInflationByCurrencyId(long currencyId) {
        return currencyInflationRepository.findByCurrencyId(currencyId).orElse(null);
    }

    @Cacheable(value = "currencyInflationIdAndYear", key = "#currencyId && #year")
    public CurrencyInflation findInflationByCurrencyIdAndYear(long currencyId, long year) {
        return currencyInflationRepository.findByCurrencyIdAndYear(currencyId, year).orElse(null);
    }

}
