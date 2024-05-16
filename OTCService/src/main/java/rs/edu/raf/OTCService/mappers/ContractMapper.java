package rs.edu.raf.OTCService.mappers;

import org.springframework.stereotype.Component;
import rs.edu.raf.OTCService.data.dto.ContractDto;
import rs.edu.raf.OTCService.data.entity.Contract;


@Component
public class ContractMapper {
    public Contract dtoToContract(ContractDto dto) {
        return new Contract(
                dto.getId(),
                dto.getBankConfirmation(),
                dto.getSellerConfirmation(),
                dto.getComment(),
                dto.getDateTimeCreated(),
                dto.getDateTimeRealized(),
                dto.getContractNumber(),
                dto.getDescription(),
                dto.getTicker(),
                dto.getVolume(),
                dto.getTotalPrice(),
                dto.getContractStatus(),
                dto.getBuyersPIB(),
                dto.getSellersPIB(),
                dto.getBuyersEmail(),
                dto.getSellersEmail(),
                dto.getContractType()
        );
    }

    public ContractDto contractToDto(Contract contract) {
        return new ContractDto(
                contract.getId(),
                contract.getBankConfirmation(),
                contract.getSellerConfirmation(),
                contract.getComment(),
                contract.getDateTimeCreated(),
                contract.getDateTimeRealized(),
                contract.getContractNumber(),
                contract.getDescription(),
                contract.getTicker(),
                contract.getVolume(),
                contract.getTotalPrice(),
                contract.getContractStatus(),
                contract.getBuyersPIB(),
                contract.getSellersPIB(),
                contract.getBuyersEmail(),
                contract.getSellersEmail(),
                contract.getContractType()
        );
    }


}
