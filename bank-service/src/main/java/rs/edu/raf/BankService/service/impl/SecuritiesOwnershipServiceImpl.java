package rs.edu.raf.BankService.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import rs.edu.raf.BankService.data.dto.SecuritiesOwnershipDto;
import rs.edu.raf.BankService.data.entities.SecuritiesOwnership;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.data.enums.ListingType;
import rs.edu.raf.BankService.exception.AccountNotFoundException;
import rs.edu.raf.BankService.mapper.SecuritiesOwnershipMapper;
import rs.edu.raf.BankService.repository.CashAccountRepository;
import rs.edu.raf.BankService.repository.SecuritiesOwnershipRepository;
import rs.edu.raf.BankService.service.SecuritiesOwnershipService;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SecuritiesOwnershipServiceImpl implements SecuritiesOwnershipService {

    public final SecuritiesOwnershipRepository securitiesOwnershipRepository;
    public final SecuritiesOwnershipMapper mapper;
    public final CashAccountRepository cashAccountRepository;

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
    public List<SecuritiesOwnershipDto> getAllPubliclyAvailableSecurityOwnershipsFromCompanies() {
        return securitiesOwnershipRepository.findSecuritiesOwnershipByQuantityOfPubliclyAvailableAndIsBusiness().stream().map(mapper::toDto).toList();
    }

    @Override
    public List<SecuritiesOwnershipDto> getAllPubliclyAvailableSecurityOwnershipsFromPrivates() {
        return securitiesOwnershipRepository.findSecuritiesOwnershipByQuantityOfPubliclyAvailableAndIsPrivate().stream().map(mapper::toDto).toList();
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

    @Override
    public Map<ListingType, BigDecimal> getValuesOfSecurities(String accountNumber) {
        boolean accountExists = securitiesOwnershipRepository.existsByAccountNumber(accountNumber);

        if (!accountExists)
            throw new AccountNotFoundException(accountNumber);

        // Initialize the map with all ListingType values set to zero
        Map<ListingType, BigDecimal> valuesMap = Arrays.stream(ListingType.values())
                .collect(Collectors.toMap(type -> type, type -> BigDecimal.ZERO));

        // Fetch and group the actual values from the repository
        Map<ListingType, BigDecimal> actualValues = securitiesOwnershipRepository.findAllByAccountNumber(accountNumber).stream()
                .collect(Collectors.groupingBy(SecuritiesOwnership::getListingType,
                        Collectors.mapping(SecuritiesOwnership::getValue,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::valueOf, BigDecimal::add))));

        // Merge the actual values into the initialized map
        valuesMap.putAll(actualValues);

        return valuesMap;
    }

    @Override
    public List<SecuritiesOwnershipDto> getBanksPubliclyAvailableSecurities() {
        CashAccount cashAccount = cashAccountRepository.findPrimaryTradingAccount(null);
        if (cashAccount == null) throw new NotFoundException("Banks account not found.");
        List<SecuritiesOwnership> securities = securitiesOwnershipRepository.findAllByAccountNumber(cashAccount.getAccountNumber());
        List<SecuritiesOwnershipDto> publicSecurities = new ArrayList<>();
        for (SecuritiesOwnership securitiesOwnership : securities) {
            if (securitiesOwnership.getQuantityOfPubliclyAvailable() > 0) {
                publicSecurities.add(mapper.toDto(securitiesOwnership));
            }
        }
        return publicSecurities;
    }
}
