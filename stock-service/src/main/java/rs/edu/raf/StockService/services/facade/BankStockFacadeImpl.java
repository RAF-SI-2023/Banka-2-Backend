package rs.edu.raf.StockService.services.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.StockService.data.dto.BankStockDto;
import rs.edu.raf.StockService.data.dto.FuturesContractDto;
import rs.edu.raf.StockService.data.entities.Forex;
import rs.edu.raf.StockService.data.entities.FuturesContract;
import rs.edu.raf.StockService.data.entities.Option;
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
    // TODO dodaj cene, ako postoje, ako ne sve na 100
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
            case "FUTURE": {
                try {
                    FuturesContractDto future = futuresContractService.findById(id);
                    price = future.getFuturesContractPrice();
                }
                catch (Exception e) {
                    price = 100.0;
                }
                break;
            }
            case "OPTION": {
                price = 100.0;
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown listing type: " + type);
        }
        return price;
    }
}
