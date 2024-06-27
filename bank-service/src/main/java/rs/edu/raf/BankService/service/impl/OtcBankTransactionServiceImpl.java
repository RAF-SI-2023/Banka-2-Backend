package rs.edu.raf.BankService.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import rs.edu.raf.BankService.data.dto.GenericTransactionDto;
import rs.edu.raf.BankService.data.dto.OtcOfferDto;
import rs.edu.raf.BankService.data.entities.SecuritiesOwnership;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.data.entities.transactions.SecuritiesTransaction;
import rs.edu.raf.BankService.data.enums.TransactionStatus;
import rs.edu.raf.BankService.mapper.TransactionMapper;
import rs.edu.raf.BankService.repository.CashAccountRepository;
import rs.edu.raf.BankService.repository.CashTransactionRepository;
import rs.edu.raf.BankService.repository.SecuritiesOwnershipRepository;
import rs.edu.raf.BankService.service.OtcBankTransactionService;
import rs.edu.raf.BankService.service.TransactionService;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class OtcBankTransactionServiceImpl implements OtcBankTransactionService {
    private final String bank3Email = "bank3Account@bank.rs";
    private final SecuritiesOwnershipRepository securitiesOwnershipRepository;
    private final CashAccountRepository cashAccountRepository;
    private final TransactionService transactionService;
    private final CashTransactionRepository cashTransactionRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public GenericTransactionDto buyStock(OtcOfferDto otcOfferDto) {
        CashAccount banksAccount = getBanksCashAccount();
        CashAccount bank3Account = getBanks3Account();

        SecuritiesTransaction transaction = createSecuritiesTransaction(banksAccount, bank3Account, otcOfferDto);

        double price = otcOfferDto.getPrice();
        if(price > banksAccount.getAvailableBalance()){
            transaction.setStatus(TransactionStatus.DECLINED);
            cashTransactionRepository.save(transaction);
            return transactionMapper.toGenericTransactionDto(transaction);
        }

        SecuritiesOwnership security = getSecurity(banksAccount, otcOfferDto.getTicker());

        //Videti za oporezivanje
        banksAccount.setAvailableBalance(banksAccount.getAvailableBalance() - price);
        cashAccountRepository.save(banksAccount);

        security.setQuantity(security.getQuantity() + otcOfferDto.getAmount());
        securitiesOwnershipRepository.save(security);

        transaction.setStatus(TransactionStatus.CONFIRMED);
        cashTransactionRepository.save(transaction);

        return transactionMapper.toGenericTransactionDto(transaction);
    }

    @Override
    public GenericTransactionDto sellStock(OtcOfferDto otcOfferDto) {
        CashAccount banksAccount = getBanksCashAccount();
        CashAccount bank3Account = getBanks3Account();

        SecuritiesOwnership security = getSecurity(banksAccount, otcOfferDto.getTicker());

        SecuritiesTransaction transaction = createSecuritiesTransaction(bank3Account, banksAccount, otcOfferDto);

        if(security == null || security.getQuantity() < otcOfferDto.getAmount()){
            transaction.setStatus(TransactionStatus.DECLINED);
            cashTransactionRepository.save(transaction);
            return transactionMapper.toGenericTransactionDto(transaction);
        }

        security.setQuantity(security.getQuantity() - otcOfferDto.getAmount());
        securitiesOwnershipRepository.save(security);

        double price = otcOfferDto.getPrice();
        //Videti za oporezivanje
        banksAccount.setAvailableBalance(banksAccount.getAvailableBalance() + price);
        cashAccountRepository.save(banksAccount);

        transaction.setStatus(TransactionStatus.CONFIRMED);
        cashTransactionRepository.save(transaction);

        return transactionMapper.toGenericTransactionDto(transaction);

    }

    private SecuritiesOwnership getSecurity(CashAccount banksAccount, String ticker){
        List<SecuritiesOwnership> securitiesOwnerships = securitiesOwnershipRepository.findAllByAccountNumber(banksAccount.getAccountNumber());
        return securitiesOwnerships.stream()
                .filter(securitiesOwnership -> securitiesOwnership.getSecuritiesSymbol().equals(ticker))
                .findFirst()
                .orElseGet(() -> {
                    SecuritiesOwnership newSecurity = new SecuritiesOwnership();
                    newSecurity.setAccountNumber(banksAccount.getAccountNumber());
                    newSecurity.setEmail(banksAccount.getEmail());
                    newSecurity.setQuantity(0);
                    newSecurity.setSecuritiesSymbol(ticker);
                    newSecurity.setOwnedByBank(true);
                    newSecurity.setQuantityOfPubliclyAvailable(0);
                    return newSecurity;
                });
    }

    private CashAccount getBanksCashAccount(){
        CashAccount cashAccount = cashAccountRepository.findPrimaryTradingAccount(null);
        if(cashAccount == null) throw new NotFoundException("Banks account not found.");
        return cashAccount;
    }

    private CashAccount getBanks3Account(){
        CashAccount cashAccount = cashAccountRepository.findPrimaryTradingAccount(bank3Email);
        if(cashAccount == null) throw new NotFoundException("Bank3 account not found.");
        return cashAccount;
    }

    private SecuritiesTransaction createSecuritiesTransaction(CashAccount sender, CashAccount receiver, OtcOfferDto otcOfferDto){
        SecuritiesTransaction transaction = new SecuritiesTransaction();
        transaction.setAmount(otcOfferDto.getPrice());
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setSecuritiesSymbol(otcOfferDto.getTicker());
        transaction.setQuantityToTransfer(otcOfferDto.getAmount());
        transaction.setReceiverCashAccount(receiver);
        transaction.setSenderCashAccount(sender);
        transaction.setStatus(TransactionStatus.PENDING);
        return transaction;
    }
}
