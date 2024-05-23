package rs.edu.raf.BankService.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.BankService.data.dto.ExternalTransferTransactionDto;
import rs.edu.raf.BankService.data.dto.GenericTransactionDto;
import rs.edu.raf.BankService.data.dto.InternalTransferTransactionDto;
import rs.edu.raf.BankService.data.entities.exchangeCurrency.ExchangeTransferTransactionDetails;
import rs.edu.raf.BankService.data.entities.transactions.ExternalTransferTransaction;
import rs.edu.raf.BankService.data.entities.transactions.InternalTransferTransaction;
import rs.edu.raf.BankService.data.entities.transactions.SecuritiesTransaction;
import rs.edu.raf.BankService.data.entities.transactions.TransferTransaction;
import rs.edu.raf.BankService.data.enums.TransactionType;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class TransactionMapper {

    public InternalTransferTransactionDto toInternalTransferTransactionDto(InternalTransferTransaction transaction) {
        InternalTransferTransactionDto dto = new InternalTransferTransactionDto();
        dto.setId(transaction.getId().toString());
        dto.setAmount(transaction.getAmount());
        dto.setStatus(transaction.getStatus());
        dto.setCreatedAt(transaction.getCreatedAt());
        dto.setSenderAccountNumber(transaction.getSenderCashAccount().getAccountNumber());
        dto.setReceiverAccountNumber(transaction.getReceiverCashAccount().getAccountNumber());

        return dto;
    }

    public InternalTransferTransaction toInternalTransferTransactionEntity(InternalTransferTransactionDto dto) {
        InternalTransferTransaction entity = new InternalTransferTransaction();
        entity.setAmount(dto.getAmount());
        entity.setCreatedAt(LocalDateTime.now());

        return entity;
    }

    public ExternalTransferTransactionDto toExternalTransferTransactionDto(ExternalTransferTransaction transaction) {
        ExternalTransferTransactionDto dto = new ExternalTransferTransactionDto();
        dto.setId(transaction.getId().toString());
        dto.setAmount(transaction.getAmount());
        dto.setStatus(transaction.getStatus());
        dto.setCreatedAt(transaction.getCreatedAt());
        dto.setSenderAccountNumber(transaction.getSenderCashAccount().getAccountNumber());
        dto.setReceiverAccountNumber(transaction.getReceiverCashAccount().getAccountNumber());
        dto.setCreatedAt(transaction.getCreatedAt());
        dto.setReferenceNumber(transaction.getReferenceNumber());
        dto.setTransactionCode(transaction.getTransactionCode());
        dto.setTransactionPurpose(transaction.getTransactionPurpose());

        return dto;
    }

    public ExternalTransferTransaction toExternalTransferTransactionEntity(ExternalTransferTransactionDto dto) {
        ExternalTransferTransaction entity = new ExternalTransferTransaction();
        entity.setTransactionPurpose(dto.getTransactionPurpose());
        entity.setReferenceNumber(dto.getReferenceNumber());
        entity.setTransactionCode(dto.getTransactionCode());
        entity.setAmount(dto.getAmount());
        entity.setCreatedAt(LocalDateTime.now());

        return entity;
    }

    // mapira transakcije za prikaz u listi(po specifikaciji)
    public GenericTransactionDto toGenericTransactionDto(TransferTransaction transferTransaction) {
        GenericTransactionDto dto = new GenericTransactionDto();
        dto.setId(transferTransaction.getId().toString());
        dto.setCreatedAt(transferTransaction.getCreatedAt().toEpochSecond(ZoneOffset.UTC));
        dto.setStatus(transferTransaction.getStatus());


        if (transferTransaction instanceof ExternalTransferTransaction) {
            dto.setAmount(((ExternalTransferTransaction) transferTransaction).getAmount());
            dto.setType(TransactionType.EXTERNAL);
        } else if (transferTransaction instanceof InternalTransferTransaction) {
            dto.setAmount(((InternalTransferTransaction) transferTransaction).getAmount());
            dto.setType(TransactionType.INTERNAL);
        } else if (transferTransaction instanceof ExchangeTransferTransactionDetails) {
            dto.setAmount((long) ((ExchangeTransferTransactionDetails) transferTransaction).getAmount());
            dto.setType(TransactionType.EXCHANGE);
        } else if (transferTransaction instanceof SecuritiesTransaction) {
            Double amount = ((SecuritiesTransaction) transferTransaction).getAmount();
            dto.setAmount(amount.longValue());
            dto.setType(TransactionType.SECURITY);
        }


        return dto;
    }
}
