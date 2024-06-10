package rs.edu.raf.StockService.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.StockService.data.dto.BankStockDto;
import rs.edu.raf.StockService.services.facade.BankStockFacadeImpl;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/bank-stock")
@CrossOrigin
public class BankServiceCommunicationController {

    private final BankStockFacadeImpl bankStockFacade;

    @PostMapping("/listing-price")
    public Double findPrice(@RequestBody BankStockDto bankStockDto) {
        return bankStockFacade.findPriceOfUnit(bankStockDto);
    }
}
