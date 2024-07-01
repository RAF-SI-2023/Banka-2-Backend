package rs.edu.raf.OTCService.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.OTCService.data.dto.ContractDto;
import rs.edu.raf.OTCService.data.entity.Contract;
import rs.edu.raf.OTCService.data.enums.ContractStatus;
import rs.edu.raf.OTCService.data.enums.ContractType;
import rs.edu.raf.OTCService.mappers.ContractMapper;
import rs.edu.raf.OTCService.repositories.ContractRepository;
import rs.edu.raf.OTCService.service.BankService;
import rs.edu.raf.OTCService.service.ContractService;
import rs.edu.raf.OTCService.util.SpringSecurityUtil;

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
        String email = SpringSecurityUtil.getPrincipalEmail();
        if (SpringSecurityUtil.isUser())
            return contractRepository.getAllWaitingContracts().stream().filter(
                    val -> val.getBuyersEmail().equals(email)
                            || val.getSellersEmail().equals(email))
                    .map(mapper::contractToDto).toList();
        else
            return contractRepository.getAllWaitingContracts().stream().map(mapper::contractToDto).toList();
    }

    @Override
    public List<ContractDto> getAllApprovedContracts() {
        String email = SpringSecurityUtil.getPrincipalEmail();
        if (SpringSecurityUtil.isUser())
            return contractRepository.getAllApprovedContracts().stream().filter(
                    val -> val.getBuyersEmail().equals(email)
                            || val.getSellersEmail().equals(email))
                    .map(mapper::contractToDto).toList();
        else
            return contractRepository.getAllApprovedContracts().stream().map(mapper::contractToDto).toList();
    }

    @Override
    public List<ContractDto> getAllRejectedContracts() {
        String email = SpringSecurityUtil.getPrincipalEmail();
        if (SpringSecurityUtil.isUser())
            return contractRepository.getAllRejectedContracts().stream().filter(
                    val -> val.getBuyersEmail().equals(email)
                            || val.getSellersEmail().equals(email))
                    .map(mapper::contractToDto).toList();
        else
            return contractRepository.getAllRejectedContracts().stream().map(mapper::contractToDto).toList();
    }

    @Override
    public List<ContractDto> getAllContracts() {
        String email = SpringSecurityUtil.getPrincipalEmail();
        if (SpringSecurityUtil.isUser())
            return contractRepository.findAll().stream().filter(
                    val -> val.getBuyersEmail().equals(email)
                            || val.getSellersEmail().equals(email))
                    .map(mapper::contractToDto).toList();
        else
            return contractRepository.findAll().stream().map(mapper::contractToDto).toList();
    }

    @Override
    public ContractDto createContract(ContractDto contractDto) {
        // provera
        Contract toBeCreated = new Contract();
        toBeCreated.setContractStatus(ContractStatus.WAITING);
        toBeCreated.setDateTimeCreated(System.currentTimeMillis());
        toBeCreated.setVolume(contractDto.getVolume());
        toBeCreated.setTotalPrice(contractDto.getTotalPrice());
        toBeCreated.setTicker(contractDto.getTicker());
        toBeCreated.setBuyersEmail(contractDto.getBuyersEmail());
        toBeCreated.setSellersEmail(contractDto.getSellersEmail());
        toBeCreated.setBuyersPIB(contractDto.getBuyersPIB());
        toBeCreated.setSellersPIB(contractDto.getSellersPIB());
        toBeCreated.setSellerConfirmation(false);
        toBeCreated.setBankConfirmation(false);
        if (contractDto.getSellersEmail() != null && contractDto.getSellersEmail() != "")
            toBeCreated.setContractType(ContractType.PRIVATE_CONTRACT);
        else
            toBeCreated.setContractType(ContractType.LEGAL_ENTITY_CONTRACT);
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
        if (contract.getBankConfirmation())
            throw new RuntimeException("Contract with id " + id + "is already confirmed by bank");
        contract.setBankConfirmation(true);

        if (contract.getBankConfirmation() && contract.getSellerConfirmation()) {
            contract.setContractStatus(ContractStatus.APPROVED);
            contract.setDateTimeRealized(System.currentTimeMillis());
            // TODO OVDE videti da li i dalje postoje securitiesOwnership vezano za ovaj
            // contract pre slanja same transakcije.
            try {
                bankService.createTransaction(mapper.contractToDto(contract));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
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
        if (contract.getSellerConfirmation())
            throw new RuntimeException("Contract with id " + id + "is already confirmed by seller");
        String loggedEmail = SpringSecurityUtil.getPrincipalEmail();
        if (!contract.getSellersEmail().equalsIgnoreCase(loggedEmail)) {
            throw new RuntimeException("You are not the seller");
        }

        contract.setSellerConfirmation(true);

        if (contract.getBankConfirmation() && contract.getSellerConfirmation()) {
            contract.setContractStatus(ContractStatus.APPROVED);
            contract.setDateTimeRealized(System.currentTimeMillis());

            // TODO OVDE videti da li i dalje postoje securitiesOwnership vezano za ovaj
            // contract pre slanja same transakcije.
            bankService.createTransaction(mapper.contractToDto(contract));

        }

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
        if (!contract.getBankConfirmation())
            throw new RuntimeException("Contract with id " + id + "is already denied by bank");
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
        if (!contract.getSellerConfirmation())
            throw new RuntimeException("Contract with id " + id + "is already denied by seller");
        String loggedEmail = SpringSecurityUtil.getPrincipalEmail();
        if (!contract.getSellersEmail().equalsIgnoreCase(loggedEmail)) {
            throw new RuntimeException("You are not the seller");
        }
        contract.setSellerConfirmation(false);
        contract.setComment(message);
        contract.setContractStatus(ContractStatus.REJECTED);
        return mapper.contractToDto(contractRepository.save(contract));
    }
}
