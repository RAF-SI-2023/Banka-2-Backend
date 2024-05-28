package rs.edu.raf.StockService.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.StockService.data.dto.FuturesContractDto;
import rs.edu.raf.StockService.data.entities.FuturesContract;

import java.util.Random;

@Component
public class FuturesContractMapper {

    public FuturesContractDto futuresContractToFuturesContractDto(FuturesContract futuresContract) {
        return new FuturesContractDto(
                futuresContract.getId(),
                futuresContract.getName(),
                futuresContract.getCode(),
                futuresContract.getContractSize(),
                futuresContract.getContractUnit(),
                futuresContract.getOpenInterest(),
                futuresContract.getSettlementDate(),
                futuresContract.getMaintenanceMargin(),
                futuresContract.getType(),
                new Random().nextDouble(100.0,2000),
                new Random().nextDouble(50.0,6000)
        );
    }

    public FuturesContract futuresContractDtoToFuturesContract(FuturesContractDto futuresContractDto) {
        return new FuturesContract(
                futuresContractDto.getId(),
                futuresContractDto.getName(),
                futuresContractDto.getCode(),
                futuresContractDto.getContractSize(),
                futuresContractDto.getContractUnit(),
                futuresContractDto.getOpenInterest(),
                futuresContractDto.getSettlementDate(),
                futuresContractDto.getMaintenanceMargin(),
                futuresContractDto.getType()
        );
    }
}
