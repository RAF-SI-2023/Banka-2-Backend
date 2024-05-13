package rs.edu.raf.BankService.service;

import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.data.dto.SecuritiesOwnershipDto;
import rs.edu.raf.BankService.data.entities.SecuritiesOwnership;

import java.util.List;

@Service
public interface SecuritiesOwnershipService {
    List<SecuritiesOwnershipDto> getSecurityOwnershipsForAccountNumber(String accountNumber);

    List<SecuritiesOwnershipDto> getSecurityOwnershipsBySecurity(String securitySymbol);

    List<SecuritiesOwnershipDto> getAllPubliclyAvailableSecurityOwnerships();

    SecuritiesOwnershipDto updatePubliclyAvailableQuantity(SecuritiesOwnershipDto soDto);

}
