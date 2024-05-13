package rs.edu.raf.OTCService.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.OTCService.data.dto.ContractDto;
import rs.edu.raf.OTCService.data.entity.Contract;
import rs.edu.raf.OTCService.data.enums.ContractStatus;
import rs.edu.raf.OTCService.mappers.ContractMapper;
import rs.edu.raf.OTCService.repositories.ContractRepository;
import rs.edu.raf.OTCService.service.BankService;
import rs.edu.raf.OTCService.service.ContractService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {
    private final ContractRepository contractRepository;
    private final ContractMapper mapper;
    private final BankService bankService;


    @Override
    public List<ContractDto> getAllWaitingContracts() {
        return contractRepository.getAllWaitingContracts().stream().map(mapper::contractToDto).toList();
    }

    @Override
    public List<ContractDto> getAllContracts() {
        return contractRepository.findAll().stream().map(mapper::contractToDto).toList();
    }

    @Override
    public ContractDto createContract(ContractDto contractDto) {
        //provera
        Contract toBeCreated = new Contract();
        toBeCreated.setContractStatus(ContractStatus.WAITING);
        toBeCreated.setDateTimeCreated(System.currentTimeMillis());
        toBeCreated.setVolume(contractDto.getVolume());
        toBeCreated.setTotalPrice(contractDto.getTotalPrice());
        toBeCreated.setTicker(contractDto.getTicker());
        toBeCreated.setSellerConfirmation(false);
        toBeCreated.setBankConfirmation(false);
        return mapper.contractToDto(contractRepository.save(toBeCreated));
    }

    @Override
    public ContractDto getContractById(Long id) {
        Optional<Contract> c = contractRepository.findById(id);
        if (c.isPresent())
            return mapper.contractToDto(c.get());
        else
            throw new RuntimeException("Contract with id " + id + " not found");
    }

    @Override
    public ContractDto bankApproveContractById(Long id) {
        Optional<Contract> c = contractRepository.findById(id);
        Contract contract = null;
        if (c.isPresent())
            contract = c.get();
        else
            throw new RuntimeException("Contract with id " + id + " not found");
        if (contract.getBankConfirmation() != null)
            throw new RuntimeException("Contract with id " + id + "is already confirmed by bank");
        contract.setBankConfirmation(true);


        if (contract.getBankConfirmation() && contract.getSellerConfirmation()) {
            contract.setContractStatus(ContractStatus.APPROVED);
            contract.setDateTimeRealized(System.currentTimeMillis());
            bankService.createTransaction(mapper.contractToDto(contract));
            //bankService.createOrder(new OrderDto(OrderStatus.APPROVED, OrderActionType.BUY,));
        }


        return mapper.contractToDto(contractRepository.save(contract));

    }

    @Override
    public ContractDto sellerApproveContractById(Long id) {
        Optional<Contract> c = contractRepository.findById(id);
        Contract contract = null;
        if (c.isPresent())
            contract = c.get();
        else
            throw new RuntimeException("Contract with id " + id + " not found");
        if (contract.getSellerConfirmation() != null)
            throw new RuntimeException("Contract with id " + id + "is already confirmed by seller");
        contract.setSellerConfirmation(true);


        if (contract.getBankConfirmation() && contract.getSellerConfirmation()) {
            contract.setContractStatus(ContractStatus.APPROVED);
            contract.setDateTimeRealized(System.currentTimeMillis());
            bankService.createTransaction(mapper.contractToDto(contract));
            //    bankService.createOrder();
        }
        //todo ovde posalji order ako su oba true na izvrsavanje
        return mapper.contractToDto(contractRepository.save(contract));
    }

    @Override
    public ContractDto bankDenyContractById(Long id, String message) {
        Optional<Contract> c = contractRepository.findById(id);
        Contract contract = null;
        if (c.isPresent())
            contract = c.get();
        else
            throw new RuntimeException("Contract with id " + id + " not found");
        if (contract.getSellerConfirmation() != null)
            throw new RuntimeException("Contract with id " + id + "is already confirmed by seller");
        contract.setBankConfirmation(false);
        contract.setComment(message);
        contract.setContractStatus(ContractStatus.REJECTED);
        return mapper.contractToDto(contractRepository.save(contract));
    }

    @Override
    public ContractDto sellerDenyContractById(Long id, String message) {
        Optional<Contract> c = contractRepository.findById(id);
        Contract contract = null;
        if (c.isPresent())
            contract = c.get();
        else
            throw new RuntimeException("Contract with id " + id + " not found");
        if (contract.getSellerConfirmation() != null)
            throw new RuntimeException("Contract with id " + id + "is already confirmed by seller");
        contract.setSellerConfirmation(false);
        contract.setComment(message);
        contract.setContractStatus(ContractStatus.REJECTED);
        return mapper.contractToDto(contractRepository.save(contract));
    }
}
