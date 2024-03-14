package rs.edu.raf.BankService.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.BankService.data.dto.AccountDto;
import rs.edu.raf.BankService.data.dto.DomesticCurrencyAccountDto;
import rs.edu.raf.BankService.data.dto.ForeignCurrencyAccountDto;
import rs.edu.raf.BankService.data.dto.ForeignCurrencyHolderDto;
import rs.edu.raf.BankService.data.entities.Account;
import rs.edu.raf.BankService.data.entities.DomesticCurrencyAccount;
import rs.edu.raf.BankService.data.entities.ForeignCurrencyAccount;
import rs.edu.raf.BankService.data.entities.ForeignCurrencyHolder;
import rs.edu.raf.BankService.data.enums.AccountType;
import rs.edu.raf.BankService.data.enums.DomesticCurrencyAccountType;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class AccountMapper {

    public Account accountDtoToAccount(AccountDto dto){
        return new Account(
                dto.getAccountNumber(),
                dto.getEmail(),
                AccountType.valueOf(dto.getAccountType()),
                dto.getCurrencyCode(),
                220.00
        );
    }

    public DomesticCurrencyAccount domesticAccountDtoToDomesticAccount(DomesticCurrencyAccountDto dto){
        return new DomesticCurrencyAccount(
                dto.getAccountNumber(),
                dto.getEmail(),
                AccountType.DOMESTIC_CURRENCY_ACCOUNT,
                dto.getCurrencyCode(),
                220.00,
                DomesticCurrencyAccountType.valueOf(dto.getDomesticCurrencyAccountType())
        );
    }

    public ForeignCurrencyAccount foreignAccountDtoToForeignAccount(ForeignCurrencyAccountDto dto){
        return new ForeignCurrencyAccount(
                dto.getAccountNumber(),
                dto.getEmail(),
                AccountType.FOREIGN_CURRENCY_ACCOUNT,
                dto.getCurrencyCode(),
                220.00
        );
    }

    public AccountDto accountToAccountDto(Account account){
        return new AccountDto(
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
                account.getMaintenanceFee()
        );
    }

    public DomesticCurrencyAccountDto domesticCurrencyAccountToDomesticCurrencyAccountDtoDto(DomesticCurrencyAccount account){
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
                account.getInterestRate()
        );
    }

    public ForeignCurrencyAccountDto foreignCurrencyAccountToForeignCurrencyAccountDtoDto(ForeignCurrencyAccount account){
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
                foreignCurrencyHolderListToForeignCurrencyHolderDtoList(account.getForeignCurrencyHolders())
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
