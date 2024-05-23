package rs.edu.raf.StockService.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;
import rs.edu.raf.StockService.data.dto.SecuritiesPriceDto;
import rs.edu.raf.StockService.data.entities.Stock;
import rs.edu.raf.StockService.services.StockService;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
@CrossOrigin
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Stock>> findAllStocks() {
        return ResponseEntity.ok(stockService.findAll());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Stock> findStockById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(stockService.findById(id));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // lose odradjeno
    @GetMapping("/stockSymbol/{symbol}")
    public ResponseEntity<List<Stock>> findStockBySymbolDEPRCATED(@PathVariable String symbol) {
        return ResponseEntity.ok(stockService.findBySymbolDEPRICATED(symbol));
    }

    @GetMapping("/by-symbol/{symbol}")
    public ResponseEntity<?> findStockBySymbol(@PathVariable String symbol) {
        try {
            return ResponseEntity.ok(stockService.findBySymbol(symbol));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/current-price/{symbol}")
    public ResponseEntity<SecuritiesPriceDto> findCurrentStockPriceBySymbol(@PathVariable String symbol) {
        try {
            return ResponseEntity.ok(stockService.findCurrentPriceBySymbol(symbol));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
