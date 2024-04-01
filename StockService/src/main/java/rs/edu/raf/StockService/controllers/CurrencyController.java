package rs.edu.raf.StockService.controllers;


import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.StockService.data.entities.Currency;
import rs.edu.raf.StockService.data.entities.CurrencyInflation;
import rs.edu.raf.StockService.services.CurrencyInflationService;
import rs.edu.raf.StockService.services.CurrencyService;
import rs.edu.raf.StockService.services.impl.CurrencyInflationServiceImpl;
import rs.edu.raf.StockService.services.impl.CurrencyServiceImpl;
import rs.edu.raf.StockService.services.impl.InMemoryCurrencyInflationServiceImpl;
import rs.edu.raf.StockService.services.impl.InMemoryCurrencyServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/currency")
@CrossOrigin
public class CurrencyController {
    private final CurrencyService currencyServiceImpl;
    private final CurrencyInflationService currencyInflationServiceImpl;

    /**
     * dodati security anotacije, videti koje metode su jos potrebne,
     * i videti implementaciju CurrencyServisa u zavisnosti od db/InMemory, slicno i za CurrencyInflationService
     */
    public CurrencyController(CurrencyServiceImpl currencyServiceImpl,
                              CurrencyInflationServiceImpl currencyInflationServiceImpl) {
        this.currencyServiceImpl = currencyServiceImpl;
        this.currencyInflationServiceImpl = currencyInflationServiceImpl;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Currency>> findAllCurrency() {
        return ResponseEntity.ok(currencyServiceImpl.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Currency> findCurrencyById(@PathVariable Long id) {
        return ResponseEntity.ok(currencyServiceImpl.findById(id));
    }

    @GetMapping("/code/{currencyCode}")
    public ResponseEntity<Currency> findCurrencyByCurrencyCode(@PathVariable String currencyCode) {
        return ResponseEntity.ok(currencyServiceImpl.findByCurrencyCode(currencyCode));
    }

    @GetMapping("/inflation/currency-id/{currencyId}")
    public ResponseEntity<List<CurrencyInflation>> findInflationByCurrencyId(@PathVariable Long currencyId) {
        return ResponseEntity.ok(currencyInflationServiceImpl.findInflationByCurrencyId(currencyId));
    }


    @GetMapping("/inflation")
    public ResponseEntity<CurrencyInflation> findInflationByCurrencyIdAndYear(@Param("currencyId") Long currencyId, @Param("year") long year) {
        return ResponseEntity.ok(currencyInflationServiceImpl.findInflationByCurrencyIdAndYear(currencyId, year));
    }

}
