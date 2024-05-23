package rs.edu.raf.BankService.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.BankService.data.dto.*;
import rs.edu.raf.BankService.data.entities.accounts.*;
import rs.edu.raf.BankService.data.enums.AccountType;
import rs.edu.raf.BankService.data.enums.DomesticCurrencyAccountType;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class AccountMapper {

    public CashAccount accountDtoToAccount(AccountDto dto){
        return new CashAccount(
                dto.getAccountNumber(),
                dto.getEmail(),
                AccountType.valueOf(dto.getAccountType()),
                dto.getCurrencyCode(),
                220.00
        );
    }

    public DomesticCurrencyCashAccount domesticAccountDtoToDomesticAccount(DomesticCurrencyAccountDto dto){
        return new DomesticCurrencyCashAccount(
                dto.getAccountNumber(),
                dto.getEmail(),
                AccountType.DOMESTIC_CURRENCY_ACCOUNT,
                dto.getCurrencyCode(),
                220.00,
                DomesticCurrencyAccountType.valueOf(dto.getDomesticCurrencyAccountType())
        );
    }

    public ForeignCurrencyCashAccount foreignAccountDtoToForeignAccount(ForeignCurrencyAccountDto dto){
        return new ForeignCurrencyCashAccount(
                dto.getAccountNumber(),
                dto.getEmail(),
                AccountType.FOREIGN_CURRENCY_ACCOUNT,
                dto.getCurrencyCode(),
                220.00
        );
    }

    public BusinessCashAccount businessAccountDtoToBusinessAccount(BusinessAccountDto dto){
        return new BusinessCashAccount(
                dto.getAccountNumber(),
                dto.getEmail(),
                AccountType.BUSINESS_ACCOUNT,
                dto.getCurrencyCode(),
                220.00,
                dto.getPIB(),
                dto.getIdentificationNumber()
        );
    }


    public AccountDto accountToAccountDto(CashAccount cashAccount){
        return new AccountDto(
                cashAccount.getId(),
                cashAccount.getAccountNumber(),
                cashAccount.getLinkState(),
                cashAccount.getEmail(),
                cashAccount.isStatus(),
                cashAccount.getAccountType().name(),
                cashAccount.getAvailableBalance(),
                cashAccount.getReservedFunds(),
                cashAccount.getEmployeeId(),
                cashAccount.getCreationDate(),
                cashAccount.getExpirationDate(),
                cashAccount.getCurrencyCode(),
                cashAccount.getMaintenanceFee(),
                cashAccount.isPrimaryTradingAccount()
        );
    }

    public DomesticCurrencyAccountDto domesticCurrencyAccountToDomesticCurrencyAccountDtoDto(DomesticCurrencyCashAccount account){
        return new DomesticCurrencyAccountDto(
                account.getId(),
                account.getAccountNumber(),
                account.getLinkState(),
                account.getEmail(),
                account.isStatus(),
                account.getAccountType().name(),
                account.getAvailableBalance(),
                account.getReservedFunds(),
                account.getEmployeeId(),
                account.getCreationDate(),
                account.getExpirationDate(),
                account.getCurrencyCode(),
                account.getMaintenanceFee(),
                account.getDomesticCurrencyAccountType().name(),
                account.getInterestRate(),
                account.isPrimaryTradingAccount()
        );
    }

    public ForeignCurrencyAccountDto foreignCurrencyAccountToForeignCurrencyAccountDtoDto(ForeignCurrencyCashAccount account){
        return new ForeignCurrencyAccountDto(
                account.getId(),
                account.getAccountNumber(),
                account.getLinkState(),
                account.getEmail(),
                account.isStatus(),
                account.getAccountType().name(),
                account.getAvailableBalance(),
                account.getReservedFunds(),
                account.getEmployeeId(),
                account.getCreationDate(),
                account.getExpirationDate(),
                account.getCurrencyCode(),
                account.getMaintenanceFee(),
                account.getInterestRate(),
                account.getDefaultCurrencyCode(),
                account.getNumberOfAllowedCurrencies(),
                foreignCurrencyHolderListToForeignCurrencyHolderDtoList(account.getForeignCurrencyHolders()),
                account.isPrimaryTradingAccount()
        );
    }

    public BusinessAccountDto businessAccountToBusinessAccountDto(BusinessCashAccount account){
        return new BusinessAccountDto(
                account.getId(),
                account.getAccountNumber(),
                account.getLinkState(),
                account.getEmail(),
                account.isStatus(),
                account.getAccountType().name(),
                account.getAvailableBalance(),
                account.getReservedFunds(),
                account.getEmployeeId(),
                account.getCreationDate(),
                account.getExpirationDate(),
                account.getCurrencyCode(),
                account.getMaintenanceFee(),
                account.getPIB(),
                account.getIdentificationNumber(),
                account.isPrimaryTradingAccount()
        );
    }

    private List<ForeignCurrencyHolderDto> foreignCurrencyHolderListToForeignCurrencyHolderDtoList(List<ForeignCurrencyHolder> list){
        return list.stream()
                .map(this::foreignCurrencyAccountToForeignCurrencyAccountDto)
                .collect(Collectors.toList());
    }

    private ForeignCurrencyHolderDto foreignCurrencyAccountToForeignCurrencyAccountDto(ForeignCurrencyHolder foreignCurrencyHolder){
        return new ForeignCurrencyHolderDto(
                foreignCurrencyHolder.getId(),
                foreignCurrencyHolder.getCurrencyCode(),
                foreignCurrencyHolder.getAvailableBalance(),
                foreignCurrencyHolder.getReservedFunds()
        );
    }

}
