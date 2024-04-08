package rs.edu.raf.BankService.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.BankService.data.dto.ExternalTransferTransactionDto;
import rs.edu.raf.BankService.data.dto.GenericTransactionDto;
import rs.edu.raf.BankService.data.dto.InternalTransferTransactionDto;
import rs.edu.raf.BankService.data.entities.transactions.ExternalTransferTransaction;
import rs.edu.raf.BankService.data.entities.transactions.InternalTransferTransaction;
import rs.edu.raf.BankService.data.entities.transactions.TransferTransaction;

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
    public GenericTransactionDto toGenericTransactionDto(TransferTransaction transferTransaction) {
        GenericTransactionDto dto = new GenericTransactionDto();
        dto.setId(transferTransaction.getId().toString());
        dto.setCreatedAt(transferTransaction.getCreatedAt());
        dto.setStatus(transferTransaction.getStatus());

        if (transferTransaction instanceof ExternalTransferTransaction) {
            dto.setAmount(((ExternalTransferTransaction) transferTransaction).getAmount());
        }
        else if (transferTransaction instanceof InternalTransferTransaction) {
            dto.setAmount(((InternalTransferTransaction) transferTransaction).getAmount());
        }

        return dto;
    }
}
