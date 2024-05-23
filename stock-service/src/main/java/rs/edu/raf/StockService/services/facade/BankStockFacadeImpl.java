package rs.edu.raf.StockService.services.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.StockService.data.dto.BankStockDto;
import rs.edu.raf.StockService.data.entities.Forex;
import rs.edu.raf.StockService.data.entities.Stock;
import rs.edu.raf.StockService.services.ForexService;
import rs.edu.raf.StockService.services.FuturesContractService;
import rs.edu.raf.StockService.services.OptionService;
import rs.edu.raf.StockService.services.StockService;

@RequiredArgsConstructor
@Service
public class BankStockFacadeImpl {

    private final StockService stockService;
    private final ForexService forexService;
    private final FuturesContractService futuresContractService;
    private final OptionService optionService;

    // za options i future nema price?
    public Double findPriceOfUnit(BankStockDto bankStockDto) {
        Double price;
        String type = bankStockDto.getListingType();
        Long id = bankStockDto.getListingId();
        switch (type) {
            case "FOREX": {
                Forex forex = forexService.findById(id);
                price = forex.getPrice();
                break;
            }
            case "STOCK": {
                Stock stock = stockService.findById(id);
                price = stock.getPrice();
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown listing type: " + type);
        }
        return price;
    }
}
