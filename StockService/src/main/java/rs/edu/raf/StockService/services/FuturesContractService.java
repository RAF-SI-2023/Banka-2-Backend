package rs.edu.raf.StockService.services;

import org.springframework.stereotype.Service;
import rs.edu.raf.StockService.data.entities.FuturesContract;

import java.util.List;

public interface FuturesContractService {
    List<FuturesContract> findAll();

    FuturesContract save(FuturesContract futuresContract);

    FuturesContract findById(Long id);
}
