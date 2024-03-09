package rs.edu.raf.StockService.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.edu.raf.StockService.data.entities.CurrencyInflation;
import rs.edu.raf.StockService.repositories.CurrencyInflationRepository;

import java.util.List;

@Service
public class CurrencyInflationServiceImpl {
    @Autowired
    private final CurrencyInflationRepository currencyInflationRepository;

    /**
     * videti koje metode ovde jos mogu da se pozivaju, koje su potrebne i slicno.
     */
    public CurrencyInflationServiceImpl(CurrencyInflationRepository currencyInflationRepository) {
        this.currencyInflationRepository = currencyInflationRepository;
    }

    public List<CurrencyInflation> findInflationByCurrencyId(long currencyId) {
        return currencyInflationRepository.findByCurrencyId(currencyId).orElse(null);
    }

    public CurrencyInflation findInflationByCurrencyIdAndYear(long currencyId, long year) {
        return currencyInflationRepository.findByCurrencyIdAndYear(currencyId, year).orElse(null);
    }

}
