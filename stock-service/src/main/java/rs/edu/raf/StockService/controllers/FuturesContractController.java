package rs.edu.raf.StockService.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.StockService.data.dto.FuturesContractDto;
import rs.edu.raf.StockService.services.FuturesContractService;

import java.util.List;

@RestController
@RequestMapping("/api/futures")
@RequiredArgsConstructor
@CrossOrigin
public class FuturesContractController {

    private final FuturesContractService futuresContractService;

    @GetMapping
    public ResponseEntity<?> allFutureContracts() {
        List<FuturesContractDto> futuresContracts = futuresContractService.findAll();
        return ResponseEntity.ok(futuresContracts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findFutureContractById(@PathVariable Long id) {
        try {
            FuturesContractDto futuresContract = futuresContractService.findById(id);
            return ResponseEntity.ok(futuresContract);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).build();
        }

    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> findFutureContractByName(@PathVariable String name) {
        try {
            FuturesContractDto futuresContractDto = futuresContractService.findByName(name);
            return ResponseEntity.ok(futuresContractDto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).build();
        }

    }

}
