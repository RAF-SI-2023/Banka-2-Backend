package rs.edu.raf.StockService.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;
import org.yaml.snakeyaml.util.UriEncoder;
import rs.edu.raf.StockService.data.entities.Exchange;
import rs.edu.raf.StockService.services.ExchangeService;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

    @GetMapping("/id/{id}")
    public ResponseEntity<Exchange> findExchangeById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(exchangeService.findById(id));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/exchange-name")
    public ResponseEntity<Exchange> findExchangeByName(@RequestParam("exchange") String exchangeName) {
        try {
            return ResponseEntity.ok(exchangeService.findByExchangeName(exchangeName));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
    }

    @GetMapping("/mi-code/{miCode}")
    public ResponseEntity<Exchange> findExchangeByMiCode(@PathVariable String miCode) {
        try {
            return ResponseEntity.ok(exchangeService.findByMICode(miCode));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/exchange-acronym")
    public ResponseEntity<Exchange> findExchangeByExchangeAcronym(@RequestParam("exchange") String exchangeAcronym) {
        try {
            return ResponseEntity.ok(exchangeService.findByExchangeAcronym(exchangeAcronym));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


}
