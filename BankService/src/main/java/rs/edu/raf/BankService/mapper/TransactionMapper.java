package rs.edu.raf.BankService.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.BankService.data.dto.ExternalTransferTransactionDto;
import rs.edu.raf.BankService.data.dto.GenericTransactionDto;
import rs.edu.raf.BankService.data.dto.InternalTransferTransactionDto;
import rs.edu.raf.BankService.data.entities.ExternalTransferTransaction;
import rs.edu.raf.BankService.data.entities.InternalTransferTransaction;
import rs.edu.raf.BankService.data.entities.Transaction;

import java.time.LocalDateTime;

@Component
public class TransactionMapper {

    public InternalTransferTransactionDto toInternalTransferTransactionDto(InternalTransferTransaction transaction) {
        InternalTransferTransactionDto dto = new InternalTransferTransactionDto();
        dto.setId(transaction.getId().toString());
        dto.setAmount(transaction.getAmount());
        dto.setStatus(transaction.getStatus());
        dto.setCreatedAt(transaction.getCreatedAt());
        dto.setSenderAccountNumber(transaction.getSenderAccount().getAccountNumber());
        dto.setReceiverAccountNumber(transaction.getReceiverAccount().getAccountNumber());

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
        dto.setSenderAccountNumber(transaction.getSenderAccount().getAccountNumber());
        dto.setReceiverAccountNumber(transaction.getReceiverAccount().getAccountNumber());
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
    public GenericTransactionDto toGenericTransactionDto(Transaction transaction) {
        GenericTransactionDto dto = new GenericTransactionDto();
        dto.setId(transaction.getId().toString());
        dto.setCreatedAt(transaction.getCreatedAt());
        dto.setStatus(transaction.getStatus());

        if (transaction instanceof ExternalTransferTransaction) {
            dto.setAmount(((ExternalTransferTransaction) transaction).getAmount());
        }
        else if (transaction instanceof InternalTransferTransaction) {
            dto.setAmount(((InternalTransferTransaction) transaction).getAmount());
        }

        return dto;
    }
}
