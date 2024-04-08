package rs.edu.raf.StockService.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.StockService.data.entities.FuturesContract;
import rs.edu.raf.StockService.repositories.FuturesContractRepository;
import rs.edu.raf.StockService.services.FuturesContractService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FuturesContractImpl implements FuturesContractService {

    private final FuturesContractRepository futuresContractRepository;

    @Override
    public List<FuturesContract> findAll() {
        return futuresContractRepository.findAll();
    }

    @Override
    public FuturesContract save(FuturesContract futuresContract) {
        return futuresContractRepository.save(futuresContract);
    }
}
