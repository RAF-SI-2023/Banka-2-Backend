package rs.edu.raf.BankService.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.data.dto.SecuritiesOwnershipDto;
import rs.edu.raf.BankService.data.entities.SecuritiesOwnership;
import rs.edu.raf.BankService.mapper.SecuritiesOwnershipMapper;
import rs.edu.raf.BankService.repository.SecuritiesOwnershipRepository;
import rs.edu.raf.BankService.service.SecuritiesOwnershipService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SecuritiesOwnershipServiceImpl implements SecuritiesOwnershipService {

    public final SecuritiesOwnershipRepository securitiesOwnershipRepository;
    public final SecuritiesOwnershipMapper mapper;

    public List<SecuritiesOwnershipDto> getSecurityOwnershipsForAccountNumber(String accountNumber) {
        return securitiesOwnershipRepository.findAllByAccountNumber(accountNumber).stream().map(mapper::toDto).toList();
    }

    @Override
    public List<SecuritiesOwnershipDto> getSecurityOwnershipsBySecurity(String securitySymbol) {
        return securitiesOwnershipRepository.findAllBySecuritiesSymbol(securitySymbol).stream().map(mapper::toDto).toList();
    }

    @Override
    public List<SecuritiesOwnershipDto> getAllPubliclyAvailableSecurityOwnerships() {
        return securitiesOwnershipRepository.findSecuritiesOwnershipByQuantityOfPubliclyAvailable().stream().map(mapper::toDto).toList();
    }

    @Override
    public SecuritiesOwnershipDto updatePubliclyAvailableQuantity(SecuritiesOwnershipDto soDto) {
        Optional<SecuritiesOwnership> so = securitiesOwnershipRepository.findById(soDto.getId());
        if (!so.isPresent()) {
            throw new RuntimeException("no security ownership with id=" + soDto.getId());
        }
        SecuritiesOwnership security = so.get();
        if (soDto.getQuantityOfPubliclyAvailable() < 0 || soDto.getQuantityOfPubliclyAvailable() > security.getQuantity()) {
            throw new RuntimeException("cannot set amount to more than u have");
        }
        security.setQuantityOfPubliclyAvailable(soDto.getQuantityOfPubliclyAvailable());
        return mapper.toDto(securitiesOwnershipRepository.save(security));
    }


}
