package rs.edu.raf.BankService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.BankService.data.dto.GenericTransactionDto;
import rs.edu.raf.BankService.data.dto.OtcOfferDto;
import rs.edu.raf.BankService.service.OtcBankTransactionService;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/otc-transaction", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class OtcBankTransactionController {
    private final OtcBankTransactionService otcBankTransactionService;

    @PostMapping("/buy-stock")
    public ResponseEntity<GenericTransactionDto> buyOtcStock(@RequestBody OtcOfferDto otcOfferDto){
        return ResponseEntity.ok(otcBankTransactionService.buyStock(otcOfferDto));
    }
    @PostMapping("/sell-stock")
    public ResponseEntity<GenericTransactionDto> sellOtcStock(@RequestBody OtcOfferDto otcOfferDto){
        return ResponseEntity.ok(otcBankTransactionService.sellStock(otcOfferDto));
    }
}
