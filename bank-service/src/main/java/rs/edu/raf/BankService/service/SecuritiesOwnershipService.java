package rs.edu.raf.BankService.service;

import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.data.dto.SecuritiesOwnershipDto;
import rs.edu.raf.BankService.data.enums.ListingType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public interface SecuritiesOwnershipService {
    List<SecuritiesOwnershipDto> getSecurityOwnershipsForAccountNumber(String accountNumber);

    List<SecuritiesOwnershipDto> getSecurityOwnershipsBySecurity(String securitySymbol);

    List<SecuritiesOwnershipDto> getAllPubliclyAvailableSecurityOwnerships();


    List<SecuritiesOwnershipDto> getAllPubliclyAvailableSecurityOwnershipsFromCompanies();

    List<SecuritiesOwnershipDto> getAllPubliclyAvailableSecurityOwnershipsFromPrivates();

    SecuritiesOwnershipDto updatePubliclyAvailableQuantity(SecuritiesOwnershipDto soDto);

    Map<ListingType, BigDecimal> getValuesOfSecurities(String accountNumber);
}
