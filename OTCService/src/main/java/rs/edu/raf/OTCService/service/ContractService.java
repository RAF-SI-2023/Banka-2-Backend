package rs.edu.raf.OTCService.service;

import org.springframework.stereotype.Service;
import rs.edu.raf.OTCService.data.dto.ContractDto;
import rs.edu.raf.OTCService.data.entity.Contract;

import java.util.List;

@Service
public interface ContractService {
    List<ContractDto> getAllWaitingContracts();

    List<ContractDto> getAllContracts();

    ContractDto createContract(ContractDto contractDto);


    ContractDto getContractById(Long id);

    ContractDto bankApproveContractById(Long id);

    ContractDto sellerApproveContractById(Long id);

    ContractDto bankDenyContractById(Long id, String message);

    ContractDto sellerDenyContractById(Long id, String message);
}
