package rs.edu.raf.BankService.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.data.dto.ExternalTransferTransactionDto;
import rs.edu.raf.BankService.data.dto.GenericTransactionDto;
import rs.edu.raf.BankService.data.dto.InternalTransferTransactionDto;
import rs.edu.raf.BankService.data.dto.TransferTransactionVerificationDto;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.data.entities.transactions.ExternalTransferTransaction;
import rs.edu.raf.BankService.data.entities.transactions.InternalTransferTransaction;
import rs.edu.raf.BankService.data.entities.transactions.TransferTransaction;
import rs.edu.raf.BankService.data.enums.TransactionStatus;
import rs.edu.raf.BankService.exception.AccountNotFoundException;
import rs.edu.raf.BankService.exception.InvalidInternalTransferException;
import rs.edu.raf.BankService.exception.TransactionNotFoundException;
import rs.edu.raf.BankService.mapper.TransactionMapper;
import rs.edu.raf.BankService.repository.AccountRepository;
import rs.edu.raf.BankService.repository.TransactionRepository;
import rs.edu.raf.BankService.service.TransactionService;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    @Override
    public InternalTransferTransactionDto createInternalTransferTransaction(InternalTransferTransactionDto internalTransferTransactionDto) {
        CashAccount senderCashAccount = accountRepository.findByAccountNumber(internalTransferTransactionDto.getSenderAccountNumber());
        CashAccount receiverCashAccount = accountRepository.findByAccountNumber(internalTransferTransactionDto.getReceiverAccountNumber());

        if (senderCashAccount == null || receiverCashAccount == null) {
            throw new AccountNotFoundException("Account not found");
        } else if (!senderCashAccount.getEmail().equals(receiverCashAccount.getEmail())) {
            throw new InvalidInternalTransferException("Account not from same user");
        } else if (internalTransferTransactionDto.getSenderAccountNumber()
                .equals(internalTransferTransactionDto.getReceiverAccountNumber())) {
            throw new InvalidInternalTransferException("Same account number provided");
        } else if (senderCashAccount.getAccountType() != receiverCashAccount.getAccountType()) {
            throw new InvalidInternalTransferException("Different account types selected");
        } else if (!senderCashAccount.getCurrencyCode().equals(receiverCashAccount.getCurrencyCode())) {
            throw new InvalidInternalTransferException("Different currencies between accounts sent");
        }

        InternalTransferTransaction transaction = transactionMapper
                .toInternalTransferTransactionEntity(internalTransferTransactionDto);
        transaction.setSenderCashAccount(senderCashAccount);
        transaction.setReceiverCashAccount(receiverCashAccount);

        Long senderBalance = senderCashAccount.getAvailableBalance();
        Long receiverBalance = receiverCashAccount.getAvailableBalance();
        Long transferAmount = transaction.getAmount();

        if (senderBalance >= transferAmount) {
            senderCashAccount.setAvailableBalance(senderBalance - transferAmount);
            receiverCashAccount.setAvailableBalance(receiverBalance + transferAmount);
            transaction.setStatus(TransactionStatus.CONFIRMED);
        }
        else {
            transaction.setStatus(TransactionStatus.DECLINED);
        }

        accountRepository.saveAll(List.of(senderCashAccount, receiverCashAccount));

        return transactionMapper.toInternalTransferTransactionDto(transactionRepository.save(transaction));
    }

    @Override
    public ExternalTransferTransactionDto createExternalTransferTransaction(ExternalTransferTransactionDto externalTransferTransactionDto) {
        CashAccount senderCashAccount = accountRepository.findByAccountNumber(externalTransferTransactionDto.getSenderAccountNumber());
        CashAccount receiverCashAccount = accountRepository.findByAccountNumber(externalTransferTransactionDto.getReceiverAccountNumber());

        if (senderCashAccount == null || receiverCashAccount == null) {
            throw new AccountNotFoundException("Account not found");
        }

        ExternalTransferTransaction transaction = transactionMapper
                .toExternalTransferTransactionEntity(externalTransferTransactionDto);
        transaction.setSenderCashAccount(senderCashAccount);
        transaction.setReceiverCashAccount(receiverCashAccount);

        Long senderBalance = transaction.getSenderCashAccount().getAvailableBalance();
        Long transferAmount = transaction.getAmount();

        if (senderBalance >= transferAmount) {
            transaction.setStatus(TransactionStatus.PENDING);

            String token = generateVerificationToken();
            transaction.setVerificationToken(token);

            sendVerificationMessage(senderCashAccount.getEmail(), token);
        }
        else {
            transaction.setStatus(TransactionStatus.DECLINED);
        }

        accountRepository.saveAll(List.of(senderCashAccount, receiverCashAccount));

        return transactionMapper.toExternalTransferTransactionDto(transactionRepository.save(transaction));
    }

    @Override
    public TransactionStatus verifyTransaction(Long transactionId, String verificationToken) {
        TransferTransaction transferTransaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));
        TransactionStatus transactionStatus = transferTransaction.getStatus();

        if (transferTransaction instanceof ExternalTransferTransaction) {
            Long senderBalance = transferTransaction.getSenderCashAccount().getAvailableBalance();
            Long receiverBalance = transferTransaction.getReceiverCashAccount().getAvailableBalance();
            Long transferAmount = ((ExternalTransferTransaction) transferTransaction).getAmount();

            CashAccount senderCashAccount = accountRepository
                    .findByAccountNumber(transferTransaction.getSenderCashAccount().getAccountNumber());
            CashAccount receiverCashAccount = accountRepository
                    .findByAccountNumber(transferTransaction.getReceiverCashAccount().getAccountNumber());

            if (verificationToken.equals(((ExternalTransferTransaction) transferTransaction).getVerificationToken())) {
                senderCashAccount.setAvailableBalance(senderBalance - transferAmount);
                receiverCashAccount.setAvailableBalance(receiverBalance + transferAmount);

                transactionStatus = TransactionStatus.CONFIRMED;
                transferTransaction.setStatus(transactionStatus);

                accountRepository.saveAll(List.of(senderCashAccount, receiverCashAccount));
            }
            else {
                transactionStatus = TransactionStatus.DECLINED;
                transferTransaction.setStatus(transactionStatus);
            }
        }

        transactionRepository.save(transferTransaction);

        return transactionStatus;
    }

    @Override
    public List<GenericTransactionDto> getTransferTransactions(Long senderAccountId) {
        CashAccount senderCashAccount = accountRepository.findById(senderAccountId)
                .orElseThrow(() -> new AccountNotFoundException("User not found"));

        return senderCashAccount.getSentTransferTransactions().stream()
                .map(transactionMapper::toGenericTransactionDto)
                .collect(Collectors.toList());
    }

    private String generateVerificationToken() {
        Random random = new Random();
        int randomFiveDigitNumber = 10000 + random.nextInt(90000);

        return Integer.toString(randomFiveDigitNumber);
    }

    private void sendVerificationMessage(String email, String token) {
        rabbitTemplate.convertAndSend(
                "transaction-verification",
                new TransferTransactionVerificationDto(email, token));
    }
}
