package rs.edu.raf.StockService.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.StockService.data.dto.FuturesContractDto;
import rs.edu.raf.StockService.data.dto.SecuritiesDto;
import rs.edu.raf.StockService.data.entities.FuturesContract;
import rs.edu.raf.StockService.data.entities.Option;

@Component
public class SecuritiesMapper {

    public SecuritiesDto convertFuturesContractToSecuritiesDto(FuturesContractDto futuresContract) {
        SecuritiesDto dto = new SecuritiesDto();
        dto.setId(futuresContract.getId());
        dto.setName(futuresContract.getName());
        dto.setType("Futures Contract");
        dto.setSettlementDate(futuresContract.getSettlementDate());
        dto.setPrice(futuresContract.getFuturesContractPrice());
        return dto;
    }

    public SecuritiesDto convertOptionToSecuritiesDto(Option option) {
        SecuritiesDto dto = new SecuritiesDto();
        dto.setId(option.getId());
        dto.setName(option.getStockListing());
        dto.setType("Option");
        dto.setSettlementDate(option.getSettlementDate());
        dto.setPrice(option.getStrikePrice());
        return dto;
    }

}
