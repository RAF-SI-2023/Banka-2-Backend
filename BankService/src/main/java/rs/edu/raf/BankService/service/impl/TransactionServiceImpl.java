package rs.edu.raf.BankService.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.data.dto.ExternalTransferTransactionDto;
import rs.edu.raf.BankService.data.dto.GenericTransactionDto;
import rs.edu.raf.BankService.data.dto.InternalTransferTransactionDto;
import rs.edu.raf.BankService.data.dto.TransactionVerificationDto;
import rs.edu.raf.BankService.data.entities.Account;
import rs.edu.raf.BankService.data.entities.ExternalTransferTransaction;
import rs.edu.raf.BankService.data.entities.InternalTransferTransaction;
import rs.edu.raf.BankService.data.entities.Transaction;
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
        Account senderAccount = accountRepository.findByAccountNumber(internalTransferTransactionDto.getSenderAccountNumber());
        Account receiverAccount = accountRepository.findByAccountNumber(internalTransferTransactionDto.getReceiverAccountNumber());

        if (senderAccount == null || receiverAccount == null) {
            throw new AccountNotFoundException("Account not found");
        }
        else if (!senderAccount.getEmployeeId().equals(receiverAccount.getEmployeeId())) {
            throw new InvalidInternalTransferException("Account not from same user");
        }
        else if (internalTransferTransactionDto.getSenderAccountNumber()
                .equals(internalTransferTransactionDto.getReceiverAccountNumber())) {
            throw new InvalidInternalTransferException("Same account number provided");
        }

        InternalTransferTransaction transaction = transactionMapper
                .toInternalTransferTransactionEntity(internalTransferTransactionDto);
        transaction.setSenderAccount(senderAccount);
        transaction.setReceiverAccount(receiverAccount);

        Long senderBalance = senderAccount.getAvailableBalance();
        Long receiverBalance = receiverAccount.getAvailableBalance();
        Long transferAmount = transaction.getAmount();

        if (senderBalance >= transferAmount) {
            senderAccount.setAvailableBalance(senderBalance - transferAmount);
            receiverAccount.setAvailableBalance(receiverBalance + transferAmount);
            transaction.setStatus(TransactionStatus.CONFIRMED);
        }
        else {
            transaction.setStatus(TransactionStatus.DECLINED);
        }

        accountRepository.saveAll(List.of(senderAccount, receiverAccount));

        return transactionMapper.toInternalTransferTransactionDto(transactionRepository.save(transaction));
    }

    @Override
    public ExternalTransferTransactionDto createExternalTransferTransaction(ExternalTransferTransactionDto externalTransferTransactionDto) {
        Account senderAccount = accountRepository.findByAccountNumber(externalTransferTransactionDto.getSenderAccountNumber());
        Account receiverAccount = accountRepository.findByAccountNumber(externalTransferTransactionDto.getReceiverAccountNumber());

        if (senderAccount == null || receiverAccount == null) {
            throw new AccountNotFoundException("Account not found");
        }

        ExternalTransferTransaction transaction = transactionMapper
                .toExternalTransferTransactionEntity(externalTransferTransactionDto);
        transaction.setSenderAccount(senderAccount);
        transaction.setReceiverAccount(receiverAccount);

        Long senderBalance = transaction.getSenderAccount().getAvailableBalance();
        Long transferAmount = transaction.getAmount();

        if (senderBalance >= transferAmount) {
            transaction.setStatus(TransactionStatus.PENDING);

            String token = generateVerificationToken();
            transaction.setVerificationToken(token);

            sendVerificationMessage(senderAccount.getEmail(), token);
        }
        else {
            transaction.setStatus(TransactionStatus.DECLINED);
        }

        accountRepository.saveAll(List.of(senderAccount, receiverAccount));

        return transactionMapper.toExternalTransferTransactionDto(transactionRepository.save(transaction));
    }

    @Override
    public TransactionStatus verifyTransaction(Long transactionId, String verificationToken) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));
        TransactionStatus transactionStatus = transaction.getStatus();

        if (transaction instanceof ExternalTransferTransaction) {
            Long senderBalance = transaction.getSenderAccount().getAvailableBalance();
            Long receiverBalance = transaction.getReceiverAccount().getAvailableBalance();
            Long transferAmount = ((ExternalTransferTransaction) transaction).getAmount();

            Account senderAccount = accountRepository
                    .findByAccountNumber(transaction.getSenderAccount().getAccountNumber());
            Account receiverAccount = accountRepository
                    .findByAccountNumber(transaction.getReceiverAccount().getAccountNumber());

            if (verificationToken.equals(((ExternalTransferTransaction) transaction).getVerificationToken())) {
                senderAccount.setAvailableBalance(senderBalance - transferAmount);
                receiverAccount.setAvailableBalance(receiverBalance + transferAmount);

                transactionStatus = TransactionStatus.CONFIRMED;
                transaction.setStatus(transactionStatus);

                accountRepository.saveAll(List.of(senderAccount, receiverAccount));
            }
            else {
                transactionStatus = TransactionStatus.DECLINED;
                transaction.setStatus(transactionStatus);
            }
        }

        transactionRepository.save(transaction);

        return transactionStatus;
    }

    @Override
    public List<GenericTransactionDto> getTransferTransactions(Long senderAccountId) {
        Account senderAccount = accountRepository.findById(senderAccountId)
                .orElseThrow(() -> new AccountNotFoundException("User not found"));

        return senderAccount.getSentTransactions().stream()
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
                new TransactionVerificationDto(email, token));
    }
}
