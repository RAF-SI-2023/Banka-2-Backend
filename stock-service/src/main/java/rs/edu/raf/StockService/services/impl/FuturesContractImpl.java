package rs.edu.raf.StockService.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import rs.edu.raf.StockService.data.dto.FuturesContractDto;
import rs.edu.raf.StockService.data.entities.FuturesContract;
import rs.edu.raf.StockService.data.entities.Option;
import rs.edu.raf.StockService.mapper.FuturesContractMapper;
import rs.edu.raf.StockService.repositories.FuturesContractRepository;
import rs.edu.raf.StockService.services.FuturesContractService;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
public class FuturesContractImpl implements FuturesContractService {

    private final FuturesContractRepository futuresContractRepository;
    private final FuturesContractMapper mapper;

    @Override
    public List<FuturesContractDto> findAll() {
        return


                futuresContractRepository.findAll().stream().map(mapper::futuresContractToFuturesContractDto).toList();
    }

    @Override
    public FuturesContract save(FuturesContract futuresContract) {
        return futuresContractRepository.save(futuresContract);
    }

    @Override
    public FuturesContractDto findById(Long id) {
        Optional<FuturesContract> futuresContractOptional=futuresContractRepository.findById(id);
        if(futuresContractOptional.isPresent()) {
            return mapper.futuresContractToFuturesContractDto(futuresContractOptional.get());
        }
        throw new RuntimeException("Future contract with id "+ id + "not found");
    }

    @Override
    public FuturesContractDto findByName(String name) {
        Optional<FuturesContract> futuresContractOptional=futuresContractRepository.findByName(name);
        if(futuresContractOptional.isPresent()){
            return mapper.futuresContractToFuturesContractDto(futuresContractOptional.get());
        }else throw new NotFoundException("Futures contract with name "+ name + "not found");


    }
}
