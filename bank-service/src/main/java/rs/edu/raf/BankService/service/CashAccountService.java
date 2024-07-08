package rs.edu.raf.BankService.service;

import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.data.dto.*;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.exception.*;

import java.util.List;

@Service
public interface CashAccountService {

    boolean userAccountUserProfileConnectionAttempt(AccountNumberDto accountNumberDto)
            throws UserAccountAlreadyAssociatedWithUserProfileException, UserAccountInProcessOfBindingWithUserProfileException, AccountNotFoundException;

    boolean confirmActivationCode(String accountNumber, Integer code)
            throws ActivationCodeExpiredException, ActivationCodeDoesNotMatchException;

    DomesticCurrencyAccountDto createDomesticCurrencyAccount(DomesticCurrencyAccountDto dto)
            throws AccountNumberAlreadyExistException;

    ForeignCurrencyAccountDto createForeignCurrencyAccount(ForeignCurrencyAccountDto dto)
            throws AccountNumberAlreadyExistException;

    BusinessAccountDto createBusinessAccount(BusinessAccountDto dto)
            throws AccountNumberAlreadyExistException;

    SavedAccountDto createSavedAccount(Long accountId, SavedAccountDto dto);

    void becomePrimaryAccount(CashAccount cashAccount);

    List<AccountDto> findAccountsByEmail(String email);

    List<AccountValuesDto> findBankAccounts() throws AccountNotFoundException;

    boolean depositWithdrawalAddition(DepositWithdrawalDto depositWithdrawalDto) throws AccountNotFoundException;

    boolean depositWithdrawalSubtraction(DepositWithdrawalDto depositWithdrawalDto) throws AccountNotFoundException;


    //AccountDto findAccountByNumber(AccountNumberDto accountNumberDto) throws AccountNotFoundException;

    SavedAccountDto updateSavedAccount(Long accountId, String savedAccountNumber, SavedAccountDto dto);

    void deleteSavedAccount(Long accountId, String savedAccountNumber);

    boolean setIsAccountPrimaryForTrading(String accountNumber, boolean usedForSecurities);

    boolean setIsAccountPrimaryForTrading(String accountNumber);
}
