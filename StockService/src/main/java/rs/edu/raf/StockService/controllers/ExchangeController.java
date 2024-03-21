package rs.edu.raf.StockService.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.StockService.data.entities.Exchange;
import rs.edu.raf.StockService.services.ExchangeService;

import java.util.List;

@RestController
@RequestMapping("/api/exchange")
@CrossOrigin
public class ExchangeController {

    private final ExchangeService exchangeService;

    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Exchange>> findAllExchanges() {
        return ResponseEntity.ok(exchangeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Exchange> findExchangeById(@PathVariable Long id) {
        return ResponseEntity.ok(exchangeService.findById(id));
    }

    @GetMapping("/exchangeName/{exchangeName}")
    public ResponseEntity<Exchange> findExchangeByName(@PathVariable String exchangeName) {
        return ResponseEntity.ok(exchangeService.findByExchangeName(exchangeName));
    }

    @GetMapping("/miCode/{miCode}")
    public ResponseEntity<Exchange> findExchangeByMiCode(@PathVariable String miCode) {
        return ResponseEntity.ok(exchangeService.findByMICode(miCode));
    }


}
