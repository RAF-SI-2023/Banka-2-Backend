package rs.edu.raf.BankService.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.BankService.data.dto.BankTransferTransactionDetailsDto;
import rs.edu.raf.BankService.data.entities.exchangeCurrency.BankTransferTransactionDetails;
import rs.edu.raf.BankService.data.entities.exchangeCurrency.ExchangeTransferTransactionDetails;
import rs.edu.raf.BankService.data.entities.transactions.TransferTransaction;
import rs.edu.raf.BankService.repository.CashTransactionRepository;

import java.util.Optional;

@Component
public class BankTransferTransactionDetailsMapper {

    private CashTransactionRepository cashTransactionRepository;

    public BankTransferTransactionDetailsDto convertToDto(BankTransferTransactionDetails entity) {
        return new BankTransferTransactionDetailsDto(
                entity.getId(),
                entity.getFee(),
                entity.getBoughtCurrency(),
                entity.getSoldCurrency(),
                entity.getAmount(),
                entity.getTotalProfit(),
                entity.getExchangeTransferTransactionDetails().getId()
        );
    }

    public BankTransferTransactionDetails convertToEntity(BankTransferTransactionDetailsDto dto) {

        Optional<TransferTransaction> exchangeDetails = cashTransactionRepository.findById(dto.getTransferTransactionDetailsId());

        if (exchangeDetails.isEmpty()) {
            return null;
        }

        BankTransferTransactionDetails entity = new BankTransferTransactionDetails(
                dto.getId(),
                dto.getFee(),
                dto.getBoughtCurrency(),
                dto.getSoldCurrency(),
                dto.getAmount(),
                dto.getTotalProfit()
        );
        entity.setExchangeTransferTransactionDetails((ExchangeTransferTransactionDetails) exchangeDetails.get());
        return entity;

    }

}
