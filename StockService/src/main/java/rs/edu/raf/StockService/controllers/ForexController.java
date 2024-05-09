package rs.edu.raf.StockService.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;
import rs.edu.raf.StockService.data.dto.SecuritiesPriceDto;
import rs.edu.raf.StockService.data.entities.Forex;
import rs.edu.raf.StockService.services.ForexService;

import java.util.List;

@RestController
@RequestMapping("/api/forex")
@CrossOrigin
public class ForexController {

    private final ForexService forexService;

    public ForexController(ForexService forexService) {
        this.forexService = forexService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Forex>> findAllForexes() {
        return ResponseEntity.ok(forexService.findAll());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Forex> findForexById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(forexService.findById(id));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/by-symbol/{symbol}")
    public ResponseEntity<Forex> findForexBySymbol(@PathVariable String symbol) {
        try {
            return ResponseEntity.ok(forexService.findBySymbol(symbol));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/base-currency/{baseCurrency}")
    public ResponseEntity<List<Forex>> findForexByBaseCurrency(@PathVariable String baseCurrency) {
        return ResponseEntity.ok(forexService.findByBaseCurrency(baseCurrency));
    }

    @GetMapping("/quote-currency/{quoteCurrency}")
    public ResponseEntity<List<Forex>> findForexByQuoteCurrency(@PathVariable String quoteCurrency) {
        return ResponseEntity.ok(forexService.findByQuoteCurrency(quoteCurrency));
    }

    @GetMapping("/current-price/{symbol}")
    public ResponseEntity<SecuritiesPriceDto> findCurrentStockPriceBySymbol(@PathVariable String symbol) {
        try {
            return ResponseEntity.ok(forexService.findCurrentPriceBySymbol(symbol));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
