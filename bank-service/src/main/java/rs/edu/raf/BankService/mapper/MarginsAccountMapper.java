package rs.edu.raf.BankService.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.BankService.data.dto.MarginsAccountRequestDto;
import rs.edu.raf.BankService.data.dto.MarginsAccountResponseDto;
import rs.edu.raf.BankService.data.entities.MarginsAccount;

import java.util.Random;

@Component
public class MarginsAccountMapper {

    public MarginsAccountResponseDto toDto(MarginsAccount marginsAccount) {
        MarginsAccountResponseDto dto = new MarginsAccountResponseDto();
        dto.setId(marginsAccount.getId());
        dto.setEmail(marginsAccount.getEmail());
        dto.setUserId(marginsAccount.getUserId());
        dto.setAccountNumber(marginsAccount.getAccountNumber());
        dto.setBalance(marginsAccount.getBalance());
        dto.setMarginCall(marginsAccount.isMarginCall());
        dto.setType(marginsAccount.getListingType());
        dto.setMaintenanceMargin(marginsAccount.getMaintenanceMargin());
        dto.setCurrencyCode(marginsAccount.getCurrencyCode());
        dto.setLoanValue(marginsAccount.getLoanValue());

        return dto;
    }

    public MarginsAccount toEntity(MarginsAccountRequestDto marginsAccountRequestDto) {
        MarginsAccount entity = new MarginsAccount();
        entity.setUserId(marginsAccountRequestDto.getUserId());
        entity.setEmail(marginsAccountRequestDto.getEmail());
        entity.setAccountNumber(generateAccountNumber());
        entity.setBalance(marginsAccountRequestDto.getBalance());
        entity.setMarginCall(marginsAccountRequestDto.isMarginCall());
        entity.setMaintenanceMargin(marginsAccountRequestDto.getMaintenanceMargin());
        entity.setCurrencyCode(marginsAccountRequestDto.getCurrencyCode());
        entity.setLoanValue(marginsAccountRequestDto.getLoanValue());

        return entity;
    }

    private String generateAccountNumber() {
        Random random = new Random();
        int number = 10000000 + random.nextInt(90000000);
        return String.valueOf(number);
    }
}
