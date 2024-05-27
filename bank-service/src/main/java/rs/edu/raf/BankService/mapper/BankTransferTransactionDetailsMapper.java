package rs.edu.raf.BankService.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.BankService.data.dto.BankTransferTransactionDetailsDto;
import rs.edu.raf.BankService.data.entities.exchangeCurrency.BankTransferTransactionDetails;

@Component
public class BankTransferTransactionDetailsMapper {

    public BankTransferTransactionDetailsDto convertToDto(BankTransferTransactionDetails entity) {
        return new BankTransferTransactionDetailsDto(
                entity.getId(),
                entity.getExchangeTransferTransactionDetails(),
                entity.getFee(),
                entity.getBoughtCurrency(),
                entity.getSoldCurrency(),
                entity.getAmount(),
                entity.getTotalProfit()
        );
    }

    public BankTransferTransactionDetails convertToEntity(BankTransferTransactionDetailsDto dto) {
        return new BankTransferTransactionDetails(
                dto.getId(),
                dto.getExchangeTransferTransactionDetails(),
                dto.getFee(),
                dto.getBoughtCurrency(),
                dto.getSoldCurrency(),
                dto.getAmount(),
                dto.getTotalProfit()
        );
    }

}
