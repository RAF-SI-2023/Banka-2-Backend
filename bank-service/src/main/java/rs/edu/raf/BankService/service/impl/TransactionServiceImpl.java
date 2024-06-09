package rs.edu.raf.BankService.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import rs.edu.raf.BankService.data.dto.*;
import rs.edu.raf.BankService.data.entities.SecuritiesOwnership;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.data.entities.transactions.ExternalTransferTransaction;
import rs.edu.raf.BankService.data.entities.transactions.InternalTransferTransaction;
import rs.edu.raf.BankService.data.entities.transactions.SecuritiesTransaction;
import rs.edu.raf.BankService.data.entities.transactions.TransferTransaction;
import rs.edu.raf.BankService.data.enums.TransactionStatus;
import rs.edu.raf.BankService.exception.AccountNotFoundException;
import rs.edu.raf.BankService.exception.InvalidInternalTransferException;
import rs.edu.raf.BankService.exception.TransactionNotFoundException;
import rs.edu.raf.BankService.mapper.TransactionMapper;
import rs.edu.raf.BankService.repository.CashAccountRepository;
import rs.edu.raf.BankService.repository.CashTransactionRepository;
import rs.edu.raf.BankService.repository.SecuritiesOwnershipRepository;
import rs.edu.raf.BankService.service.ActionAgentProfitService;
import rs.edu.raf.BankService.service.CurrencyExchangeService;
import rs.edu.raf.BankService.service.TransactionService;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {

    private final CashTransactionRepository cashTransactionRepository;
    private final CashAccountRepository cashAccountRepository;
    private final TransactionMapper transactionMapper;
    private final RabbitTemplate rabbitTemplate;
    private final CurrencyExchangeService currencyExchangeService;
    private final SecuritiesOwnershipRepository securitiesOwnershipRepository;
    private final ActionAgentProfitService actionAgentProfitService;

    @Transactional
    @Override
    public InternalTransferTransactionDto createInternalTransferTransaction(InternalTransferTransactionDto internalTransferTransactionDto) {
        CashAccount senderCashAccount = cashAccountRepository.findByAccountNumber(internalTransferTransactionDto.getSenderAccountNumber());
        CashAccount receiverCashAccount = cashAccountRepository.findByAccountNumber(internalTransferTransactionDto.getReceiverAccountNumber());

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

        double senderBalance = senderCashAccount.getAvailableBalance();
        double receiverBalance = receiverCashAccount.getAvailableBalance();
        Long transferAmount = transaction.getAmount();

        if (senderBalance >= transferAmount) {
            senderCashAccount.setAvailableBalance(senderBalance - transferAmount);
            receiverCashAccount.setAvailableBalance(receiverBalance + transferAmount);
            transaction.setStatus(TransactionStatus.CONFIRMED);
        } else {
            transaction.setStatus(TransactionStatus.DECLINED);
        }

        cashAccountRepository.saveAll(List.of(senderCashAccount, receiverCashAccount));

        return transactionMapper.toInternalTransferTransactionDto(cashTransactionRepository.save(transaction));
    }

    @Override
    public ExternalTransferTransactionDto createExternalTransferTransaction(ExternalTransferTransactionDto externalTransferTransactionDto) {
        CashAccount senderCashAccount = cashAccountRepository.findByAccountNumber(externalTransferTransactionDto.getSenderAccountNumber());
        CashAccount receiverCashAccount = cashAccountRepository.findByAccountNumber(externalTransferTransactionDto.getReceiverAccountNumber());

        if (senderCashAccount == null || receiverCashAccount == null) {
            throw new AccountNotFoundException("Account not found");
        }

        ExternalTransferTransaction transaction = transactionMapper
                .toExternalTransferTransactionEntity(externalTransferTransactionDto);
        transaction.setSenderCashAccount(senderCashAccount);
        transaction.setReceiverCashAccount(receiverCashAccount);

        double senderBalance = transaction.getSenderCashAccount().getAvailableBalance();
        Long transferAmount = transaction.getAmount();

        if (senderBalance >= transferAmount) {
            transaction.setStatus(TransactionStatus.PENDING);

            String token = generateVerificationToken();
            transaction.setVerificationToken(token);

            sendVerificationMessage(senderCashAccount.getEmail(), token);
        } else {
            transaction.setStatus(TransactionStatus.DECLINED);
        }

        cashAccountRepository.saveAll(List.of(senderCashAccount, receiverCashAccount));

        return transactionMapper.toExternalTransferTransactionDto(cashTransactionRepository.save(transaction));
    }

    @Override
    public TransactionStatus verifyTransaction(Long transactionId, String verificationToken) {
        TransferTransaction transferTransaction = cashTransactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));
        TransactionStatus transactionStatus = transferTransaction.getStatus();

        if (transferTransaction instanceof ExternalTransferTransaction) {
            double senderBalance = transferTransaction.getSenderCashAccount().getAvailableBalance();
            double receiverBalance = transferTransaction.getReceiverCashAccount().getAvailableBalance();
            Long transferAmount = ((ExternalTransferTransaction) transferTransaction).getAmount();

            CashAccount senderCashAccount = cashAccountRepository
                    .findByAccountNumber(transferTransaction.getSenderCashAccount().getAccountNumber());
            CashAccount receiverCashAccount = cashAccountRepository
                    .findByAccountNumber(transferTransaction.getReceiverCashAccount().getAccountNumber());

            if (verificationToken.equals(((ExternalTransferTransaction) transferTransaction).getVerificationToken())) {
                senderCashAccount.setAvailableBalance(senderBalance - transferAmount);
                receiverCashAccount.setAvailableBalance(receiverBalance + transferAmount);

                transactionStatus = TransactionStatus.CONFIRMED;
                transferTransaction.setStatus(transactionStatus);

                cashAccountRepository.saveAll(List.of(senderCashAccount, receiverCashAccount));
            } else {
                transactionStatus = TransactionStatus.DECLINED;
                transferTransaction.setStatus(transactionStatus);
            }
        }

        cashTransactionRepository.save(transferTransaction);

        return transactionStatus;
    }

    @Override
    public List<GenericTransactionDto> getTransferTransactions(Long userId) {
        CashAccount senderCashAccount = cashAccountRepository.findById(userId)
                .orElseThrow(() -> new AccountNotFoundException("User not found"));

        return senderCashAccount.getSentTransferTransactions().stream()
                .map(transactionMapper::toGenericTransactionDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<GenericTransactionDto> getTransferTransactionsByEmail(String email) {

        List<CashAccount> senderCashAccountList = cashAccountRepository.findAllByEmail(email);

        if (senderCashAccountList.isEmpty()) {
            throw new AccountNotFoundException("User not found");
        }


        List<GenericTransactionDto> transactions = senderCashAccountList.stream()
                .flatMap(cashAccount -> Stream.concat(
                        cashAccount.getSentTransferTransactions().stream(),
                        cashAccount.getReceivedTransferTransactions().stream()))
                .map(transactionMapper::toGenericTransactionDto)
                .collect(Collectors.toList());

        System.out.println(transactions.get(0).getType());
        return transactions;
    }

    @Override
    public InternalTransferTransactionDto depositWithdrawalTransaction(InternalTransferTransactionDto internalTransferTransactionDto) {

        InternalTransferTransaction internalTransferTransaction = null;
        CashAccount cashAccount;

        cashAccount = cashAccountRepository.findByAccountNumber(internalTransferTransactionDto.getSenderAccountNumber());

        if (cashAccount == null) {
            throw new AccountNotFoundException(internalTransferTransactionDto.getSenderAccountNumber());
        }

        if(internalTransferTransactionDto.getAmount() >= 0) {
            cashAccount.setAvailableBalance(cashAccount.getAvailableBalance() + internalTransferTransactionDto.getAmount());
        }
        else {
            if(cashAccount.getAvailableBalance() > Math.abs(internalTransferTransactionDto.getAmount())) {
                cashAccount.setAvailableBalance(cashAccount.getAvailableBalance() - Math.abs(internalTransferTransactionDto.getAmount()));
            }
            else {
                throw new RuntimeException("amount is more then balance on account");
            }
        }

        internalTransferTransaction = transactionMapper.toInternalTransferTransactionEntity(internalTransferTransactionDto);
        cashAccountRepository.save(cashAccount);
        cashTransactionRepository.save(internalTransferTransaction);


        return internalTransferTransactionDto;
    }

    @Override
    public boolean reserveFunds(String accountNumber, double amount) {
        CashAccount cashAccount = cashAccountRepository.findByAccountNumber(accountNumber);
        if (cashAccount == null) {
            throw new AccountNotFoundException("Account not found");
        }
        return reserveFunds(cashAccount, amount);
    }

    @Override
    public boolean reserveFunds(CashAccount cashAccount, double amount) {
        // OVDE EVENTUALNO MOGU TRANSAKCIJE DA SE PRAVE
        double availableBalance = cashAccount.getAvailableBalance();
        double reservedFunds = cashAccount.getReservedFunds();

        if (availableBalance - reservedFunds < amount) {
            throw new RuntimeException("Insufficient funds");
        }

        cashAccount.setReservedFunds(reservedFunds + amount);
        cashAccountRepository.save(cashAccount);
        return true;
    }

    @Override
    public boolean releaseFunds(String accountNumber, double amount) {
        CashAccount cashAccount = cashAccountRepository.findByAccountNumber(accountNumber);
        if (cashAccount == null) {
            throw new AccountNotFoundException("Account not found");
        }
        return releaseFunds(cashAccount, amount);
    }

    @Override
    public boolean releaseFunds(CashAccount cashAccount, double amount) {
        // OVDE EVENTUALNO MOGU TRANSAKCIJE DA SE PRAVE
        double availableBalance = cashAccount.getAvailableBalance();
        double reservedFunds = cashAccount.getReservedFunds();

        if (reservedFunds - amount< -0.01) {
            // this should not happen
            // possible if using random or something, throws exception once in a blue moon, totally unpredictable
            System.out.println("amount = " +  amount +" reserved = "+reservedFunds);
            throw new RuntimeException("Insufficient reserved funds (THIS SOULD NOT HAPPEN)");
        }

        cashAccount.setAvailableBalance(availableBalance - amount);
        cashAccount.setReservedFunds(Math.abs(reservedFunds - amount)<0.01?0:reservedFunds-amount);
        cashAccountRepository.save(cashAccount);
        return true;
    }

    @Override
    public boolean addFunds(CashAccount cashAccount, double amount) {
        // OVDE EVENTUALNO MOGU TRANSAKCIJE DA SE PRAVE
        double availableBalance = cashAccount.getAvailableBalance();
        cashAccount.setAvailableBalance(availableBalance + amount);
        cashAccountRepository.save(cashAccount);
        return true;
    }

    @Override
    public boolean transferFunds(String senderAccountNumber, String receiverAccountNumber, double amount) {
        // OVDE EVENTUALNO MOGU TRANSAKCIJE DA SE PRAVE
        CashAccount senderAccount = cashAccountRepository.findByAccountNumber(senderAccountNumber);
        CashAccount receiverAccount = cashAccountRepository.findByAccountNumber(receiverAccountNumber);
        if (receiverAccount == null || senderAccount == null) {
            throw new NotFoundException("Seller or buyer account not found");
        }
        releaseFunds(senderAccount, amount);
        double fundsToAdd = currencyExchangeService.calculateAmountBetweenCurrencies(senderAccount.getCurrencyCode(), receiverAccount.getCurrencyCode(), amount);
        addFunds(receiverAccount, fundsToAdd);
        return true;
    }

    @Override
    public GenericTransactionDto createSecuritiesTransaction(ContractDto contractDto) {

        SecuritiesTransactionDto securitiesTransactionDto = new SecuritiesTransactionDto();
        securitiesTransactionDto.setAmount(contractDto.getTotalPrice());
        securitiesTransactionDto.setQuantityToTransfer(contractDto.getVolume());
        securitiesTransactionDto.setSecuritiesSymbol(contractDto.getTicker());
        CashAccount buyer = cashAccountRepository.findPrimaryTradingAccount(contractDto.getBuyersEmail());
        CashAccount seller = cashAccountRepository.findPrimaryTradingAccount(contractDto.getSellersEmail());
        if (buyer == null || seller == null)
            throw new RuntimeException("buyer or seller account doesnt exist");


        List<SecuritiesOwnership> buySecurities = securitiesOwnershipRepository.findAllByAccountNumberAndSecuritiesSymbol(buyer.getAccountNumber(), securitiesTransactionDto.getSecuritiesSymbol());
        List<SecuritiesOwnership> sellSecurities = securitiesOwnershipRepository.findAllByAccountNumberAndSecuritiesSymbol(seller.getAccountNumber(), securitiesTransactionDto.getSecuritiesSymbol());

        SecuritiesTransaction transaction = new SecuritiesTransaction();
        transaction.setAmount(securitiesTransactionDto.getAmount());
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setSecuritiesSymbol(securitiesTransactionDto.getSecuritiesSymbol());
        transaction.setQuantityToTransfer(securitiesTransactionDto.getQuantityToTransfer());
        transaction.setReceiverCashAccount(seller);
        transaction.setSenderCashAccount(buyer);
        transaction.setStatus(TransactionStatus.PENDING);


        boolean doNotProcessOrder =
                securitiesTransactionDto.getAmount() > buyer.getAvailableBalance() || sellSecurities.isEmpty() || sellSecurities.get(0).getQuantityOfPubliclyAvailable() < securitiesTransactionDto.getAmount();

        if (!doNotProcessOrder) {
            transaction.setStatus(TransactionStatus.DECLINED);
            cashTransactionRepository.save(transaction);

            throw new RuntimeException("not enough funds/stocks");
        }
        //mora i ovo da se updateuje zar ne?
        //trebalo bi da bude unique email + securityName as a key

        if (buySecurities.isEmpty()) {
            //kreiraj
            SecuritiesOwnership so = new SecuritiesOwnership();
            so.setAccountNumber(buyer.getAccountNumber());
            so.setEmail(buyer.getEmail());
            so.setQuantity(0);
            so.setSecuritiesSymbol(securitiesTransactionDto.getSecuritiesSymbol());
            so.setOwnedByBank(false);
            so.setQuantityOfPubliclyAvailable(0);
            securitiesOwnershipRepository.save(so);
            buySecurities.add(so);
        }

        Integer quantityToProcess = securitiesTransactionDto.getQuantityToTransfer();
        double totalPrice = securitiesTransactionDto.getAmount();
        reserveFunds(buyer.getAccountNumber(),totalPrice);
        transferFunds(buyer.getAccountNumber(), seller.getAccountNumber(), totalPrice);
        //..
        SecuritiesOwnership buyerSo = buySecurities.get(0);
        SecuritiesOwnership sellerSo = sellSecurities.get(0);
        buyerSo.setQuantity(buyerSo.getQuantity() + quantityToProcess);
        sellerSo.setQuantity(sellerSo.getQuantity() - quantityToProcess);
        securitiesOwnershipRepository.saveAll(List.of(buyerSo, sellerSo));

        //TODO

        transaction.setStatus(TransactionStatus.CONFIRMED);
        cashTransactionRepository.save(transaction);
        actionAgentProfitService.createAgentProfit(transaction,sellerSo,quantityToProcess);

        return transactionMapper.toGenericTransactionDto(cashTransactionRepository.save(transaction));

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
