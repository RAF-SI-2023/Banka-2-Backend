package rs.edu.raf.BankService.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.BankService.data.dto.ExchangeRequestDto;
import rs.edu.raf.BankService.service.CurrencyExchangeService;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(value = "/api/currency-exchange", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "bankApi")
public class CurrencyExchangeController {

    private final CurrencyExchangeService currencyExchangeService;

    @GetMapping("/exchange-rate/from/{fromCurrency}")
    public ResponseEntity<?> getAllExchangeRates(@PathVariable String fromCurrency) {
        try {
            return ResponseEntity.ok(currencyExchangeService.getExchangeRatesForCurrency(fromCurrency));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/exchange-currency")
    public ResponseEntity<?> exchangeCurrency(@RequestBody ExchangeRequestDto exchangeRequestDto) {
        try {
            return ResponseEntity.ok(currencyExchangeService.exchangeCurrency(exchangeRequestDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
