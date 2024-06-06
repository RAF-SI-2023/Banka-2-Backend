package rs.edu.raf.StockService.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.StockService.data.dto.SecuritiesDto;
import rs.edu.raf.StockService.services.SecuritiesService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/securities")
@CrossOrigin
@RequiredArgsConstructor
public class SecuritiesController {

    private final SecuritiesService securitiesService;

    @GetMapping("/get-by-settlement-date")
    public ResponseEntity<List<SecuritiesDto>> getSecuritiesBySettlementDate(@RequestParam(required = false) LocalDate futureDate) {
        List<SecuritiesDto> securities = securitiesService.getSecuritiesBySettlementDate(Optional.ofNullable(futureDate));
        return ResponseEntity.ok(securities);
    }
}
