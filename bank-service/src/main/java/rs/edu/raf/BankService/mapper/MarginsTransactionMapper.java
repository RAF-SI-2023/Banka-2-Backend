package rs.edu.raf.BankService.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.BankService.data.dto.MarginsTransactionRequestDto;
import rs.edu.raf.BankService.data.dto.MarginsTransactionResponseDto;
import rs.edu.raf.BankService.data.entities.MarginsTransaction;

import java.time.LocalDateTime;

@Component
public class MarginsTransactionMapper {

    public MarginsTransaction toEntity(MarginsTransactionRequestDto dto) {
        MarginsTransaction entity = new MarginsTransaction();
        entity.setCreatedAt(LocalDateTime.now());
        entity.setDescription(dto.getDescription());
        entity.setCurrencyCode(dto.getCurrencyCode());
        entity.setOrderId(dto.getOrderId());
        entity.setType(dto.getType());

        return entity;
    }

    public MarginsTransactionResponseDto toDto(MarginsTransaction entity) {
        MarginsTransactionResponseDto dto = new MarginsTransactionResponseDto();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setCurrencyCode(entity.getCurrencyCode());
        dto.setOrderId(entity.getOrderId());
        dto.setType(entity.getType());
        dto.setInitialMargin(entity.getInvestmentAmount());
        dto.setOrderId(entity.getOrderId());
        dto.setUserId(entity.getUserId());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setMarginsAccountId(entity.getMarginsAccount().getId());

        return dto;
    }
}
