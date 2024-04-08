package rs.edu.raf.StockService.services.impl;

import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import rs.edu.raf.StockService.data.entities.Forex;
import rs.edu.raf.StockService.repositories.ForexRepository;
import rs.edu.raf.StockService.services.ForexService;

import java.util.List;

@Service
public class ForexServiceImpl implements ForexService {

    private final ForexRepository forexRepository;

    public ForexServiceImpl(ForexRepository forexRepository) {
        this.forexRepository = forexRepository;
    }

    @Override
    public List<Forex> findAll() {
        return forexRepository.findAll();
    }

    @Override
    public Forex findById(Long id) {
        return forexRepository.findById(id).orElseThrow(() -> new NotFoundException("Forex with id: " + id + "not found"));
    }

    @Override
    public List<Forex> findByBaseCurrency(String baseCurrency) {
        return forexRepository.findForexesByBaseCurrency(baseCurrency);
    }

    @Override
    public List<Forex> findByQuoteCurrency(String quoteCurrency) {
        return forexRepository.findForexesByQuoteCurrency(quoteCurrency);
    }
}
