package rs.edu.raf.StockService.services;


import rs.edu.raf.StockService.data.entities.CurrencyInflation;

import java.util.List;


public interface CurrencyInflationService {

    List<CurrencyInflation> findInflationByCurrencyId(long currencyId);

    CurrencyInflation findInflationByCurrencyIdAndYear(long currencyId, long year);

}
