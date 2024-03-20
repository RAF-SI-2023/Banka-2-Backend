package rs.edu.raf.StockService.services.impl;

import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import rs.edu.raf.StockService.data.entities.Option;
import rs.edu.raf.StockService.repositories.OptionRepository;
import rs.edu.raf.StockService.services.OptionService;

import java.util.ArrayList;
import java.util.List;

@Service
public class OptionServiceImpl implements OptionService {

    private final OptionRepository optionRepository;

    public OptionServiceImpl(OptionRepository optionRepository) {

        this.optionRepository = optionRepository;
    }


    @Override
    public List<Option> findAll() {
        return optionRepository.findAll();
    }

    @Override
    public List<Option> findAllByStockListing(String stockListing) {

        List<Option> requestedOptions = optionRepository.findAllByStockListing(stockListing);

        return optionRepository.findAllByStockListing(stockListing);
    }

    @Override
    public Option findById(Long id) {
        return optionRepository.findById(id).orElseThrow(() -> new NotFoundException("Option with id: " + id + " not found."));
    }


    @Override
    public Option findByStockListing(String stockListing) {
        return optionRepository.findByStockListing(stockListing);
    }
}

