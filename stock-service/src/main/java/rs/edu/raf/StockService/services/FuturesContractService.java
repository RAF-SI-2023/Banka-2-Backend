package rs.edu.raf.StockService.services;

import rs.edu.raf.StockService.data.dto.FuturesContractDto;
import rs.edu.raf.StockService.data.entities.FuturesContract;

import java.util.List;

public interface FuturesContractService {
    List<FuturesContractDto> findAll();

    FuturesContract save(FuturesContract futuresContract);

    FuturesContractDto findById(Long id);

    FuturesContractDto findByName(String name);
}

