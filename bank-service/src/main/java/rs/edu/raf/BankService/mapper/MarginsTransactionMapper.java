package rs.edu.raf.BankService.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.BankService.data.dto.MarginsTransactionDto;
import rs.edu.raf.BankService.data.entities.MarginsTransaction;

import java.time.LocalDateTime;

@Component
public class MarginsTransactionMapper {

    public MarginsTransaction toEntity(MarginsTransactionDto dto) {
        MarginsTransaction entity = new MarginsTransaction();
        entity.setCreatedAt(LocalDateTime.now());
        entity.setDescription(dto.getDescription());
        entity.setCurrencyCode(dto.getCurrencyCode());
        entity.setOrderId(dto.getOrderId());

        return entity;
    }
}
