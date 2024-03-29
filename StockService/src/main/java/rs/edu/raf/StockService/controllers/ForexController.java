package rs.edu.raf.StockService.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        return ResponseEntity.ok(forexService.findById(id));
    }

    //yikes not unique
    @GetMapping("/base-currency/{baseCurrency}")
    public ResponseEntity<Forex> findForexByBaseCurrency(@PathVariable String baseCurrency) {
        return ResponseEntity.ok(forexService.findByBaseCurrency(baseCurrency));
    }

    //yikes not unique
    @GetMapping("/quote-currency/{quoteCurrency}")
    public ResponseEntity<Forex> findForexByQuoteCurrency(@PathVariable String quoteCurrency) {
        return ResponseEntity.ok(forexService.findByQuoteCurrency(quoteCurrency));
    }

}
