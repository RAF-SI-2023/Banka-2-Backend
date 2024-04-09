package rs.edu.raf.BankService.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.BankService.data.dto.ExchangeTransferDetailsDto;
import rs.edu.raf.BankService.data.entities.exchangeCurrency.ExchangeTransferTransactionDetails;

import java.time.ZoneOffset;

@Component
public class ExchangeTransferDetailsMapper {
    public ExchangeTransferDetailsDto exchangeTransferDetailsToExchangeTransferDetailsDto(ExchangeTransferTransactionDetails exchangeTransferDetails) {
        return new ExchangeTransferDetailsDto(
                exchangeTransferDetails.getId(),
                exchangeTransferDetails.getSenderAccount().getAccountNumber(),
                exchangeTransferDetails.getReceiverAccount().getAccountNumber(),
                exchangeTransferDetails.getFromCurrency(),
                exchangeTransferDetails.getToCurrency(),
                exchangeTransferDetails.getAmount(),
                exchangeTransferDetails.getExchangeRate(),
                exchangeTransferDetails.getFee(),
                exchangeTransferDetails.getTotalAmount(),
                exchangeTransferDetails.getCreatedAt().toEpochSecond(ZoneOffset.UTC));
    }

}
