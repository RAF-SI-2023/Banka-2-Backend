package rs.edu.raf.StockService.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.edu.raf.StockService.data.entities.FuturesContract;
import rs.edu.raf.StockService.services.FuturesContractService;

import java.util.List;

import static org.antlr.v4.runtime.tree.xpath.XPath.findAll;

@RestController
@RequestMapping("/api/futures")
@RequiredArgsConstructor
public class FuturesContractController {

    private final FuturesContractService futuresContractService;

    @GetMapping
    public ResponseEntity<?> allFutureContracts() {
        List<FuturesContract> futuresContracts = futuresContractService.findAll();
        return ResponseEntity.ok(futuresContracts);
    }
}
