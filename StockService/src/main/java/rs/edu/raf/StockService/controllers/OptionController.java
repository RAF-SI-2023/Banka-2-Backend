package rs.edu.raf.StockService.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import rs.edu.raf.StockService.data.entities.Option;
import rs.edu.raf.StockService.services.impl.OptionServiceImpl;

import java.util.List;

@RestController("/api/options")
@CrossOrigin
public class OptionController {

    private final OptionServiceImpl optionServiceImpl;

    public OptionController(OptionServiceImpl optionServiceImpl) {
        this.optionServiceImpl = optionServiceImpl;
    }

    @GetMapping("/allOptions")
    public ResponseEntity<List<Option>> findAllOptions() {
        return ResponseEntity.ok(optionServiceImpl.findAll());
    }

    @GetMapping("/allStockOptions/{stockListing}")
    public ResponseEntity<List<Option>> findAllOptionsByStockListing(@PathVariable String stockListing) {
        return ResponseEntity.ok(optionServiceImpl.findAllByStockListing(stockListing));
    }

    @GetMapping("options/{id}")
    public ResponseEntity<Option> findOptionById(@PathVariable Long id) {
        return ResponseEntity.ok(optionServiceImpl.findById(id));
    }

    @GetMapping("/stock/{stockListing}")
    public ResponseEntity<Option> findOptionByStockListing(@PathVariable String stockListing) {
        return ResponseEntity.ok(optionServiceImpl.findByStockListing(stockListing));
    }

}
