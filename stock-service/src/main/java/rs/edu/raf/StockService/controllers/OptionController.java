package rs.edu.raf.StockService.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.StockService.data.entities.Option;
import rs.edu.raf.StockService.services.impl.OptionServiceImpl;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/options")
public class OptionController {

    private final OptionServiceImpl optionServiceImpl;

    public OptionController(OptionServiceImpl optionServiceImpl) {
        this.optionServiceImpl = optionServiceImpl;
    }

    @GetMapping("/stock-listing/{stockListing}")
    public ResponseEntity<List<Option>> findAllOptionsByStockListing(@PathVariable String stockListing) {
        if (stockListing == null || stockListing.trim().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<Option> lista = optionServiceImpl.findAllByStockListing(stockListing.trim());
        if (lista.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(lista);
    }

}
