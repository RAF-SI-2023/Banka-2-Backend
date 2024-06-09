package rs.edu.raf.StockService.services;

import org.springframework.stereotype.Service;
import rs.edu.raf.StockService.data.dto.SecuritiesDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public interface SecuritiesService {
    List<SecuritiesDto> getSecuritiesBySettlementDate(Optional<LocalDate> futureDate);
}
