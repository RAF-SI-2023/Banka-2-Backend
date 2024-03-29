package rs.edu.raf.StockService.controllers;


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

    @GetMapping("/all")
    public ResponseEntity<List<Option>> findAllOptions() {
        return ResponseEntity.ok(optionServiceImpl.findAll());
    }

    @GetMapping("/stock-listing/{stockListing}")
    public ResponseEntity<List<Option>> findAllOptionsByStockListing(@PathVariable String stockListing) {
        return ResponseEntity.ok(optionServiceImpl.findAllByStockListing(stockListing));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Option> findOptionById(@PathVariable Long id) {
        return ResponseEntity.ok(optionServiceImpl.findById(id));
    }

}
