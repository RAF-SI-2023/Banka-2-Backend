package rs.edu.raf.BankService.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.data.entities.SecuritiesOwnership;
import rs.edu.raf.BankService.repository.SecuritiesOwnershipRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class SecuritiesOwnershipServiceImpl {

    final SecuritiesOwnershipRepository securitiesOwnershipRepository;

    public List<SecuritiesOwnership> getSecurityOwnershipsForAccountNumber(String accountNumber) {
        return securitiesOwnershipRepository.findAllByAccountNumber(accountNumber);
    }

    public SecuritiesOwnership createSecurityOwnership(SecuritiesOwnership securitiesOwnership) {
        return securitiesOwnershipRepository.save(securitiesOwnership);
    }

    public SecuritiesOwnership updateSecurityOwnership(SecuritiesOwnership securitiesOwnership) {
        return securitiesOwnershipRepository.save(securitiesOwnership);
    }


}
