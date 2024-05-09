package rs.edu.raf.BankService.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.BankService.data.dto.MarginsAccountDto;
import rs.edu.raf.BankService.data.entities.MarginsAccount;

@Component
public class MarginsAccountMapper {

    public MarginsAccountDto toDto(MarginsAccount marginsAccount) {
        MarginsAccountDto dto = new MarginsAccountDto();
        dto.setId(marginsAccount.getId());
        dto.setAccountNumber(marginsAccount.getAccountNumber());
        dto.setBalance(marginsAccount.getBalance());
        dto.setMarginCall(marginsAccount.isMarginCall());
        dto.setType(marginsAccount.getListingType());
        dto.setMaintenanceMargin(marginsAccount.getMaintenanceMargin());
        dto.setCurrencyCode(marginsAccount.getCurrencyCode());
        dto.setLoanValue(marginsAccount.getLoanValue());

        return dto;
    }

    public MarginsAccount toEntity(MarginsAccountDto marginsAccountDto) {
        MarginsAccount entity = new MarginsAccount();
        entity.setAccountNumber(marginsAccountDto.getAccountNumber());
        entity.setBalance(marginsAccountDto.getBalance());
        entity.setMarginCall(marginsAccountDto.isMarginCall());
        entity.setListingType(marginsAccountDto.getType());
        entity.setMaintenanceMargin(marginsAccountDto.getMaintenanceMargin());
        entity.setCurrencyCode(marginsAccountDto.getCurrencyCode());
        entity.setLoanValue(marginsAccountDto.getLoanValue());

        return entity;
    }
}
