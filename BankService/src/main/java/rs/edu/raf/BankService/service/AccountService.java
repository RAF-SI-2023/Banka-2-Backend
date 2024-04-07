package rs.edu.raf.BankService.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
<<<<<<< HEAD
import rs.edu.raf.BankService.data.dto.AccountNumberDto;
import rs.edu.raf.BankService.data.dto.BusinessAccountDto;
import rs.edu.raf.BankService.data.dto.DomesticCurrencyAccountDto;
import rs.edu.raf.BankService.data.dto.ForeignCurrencyAccountDto;
import rs.edu.raf.BankService.data.entities.Account;
=======
import rs.edu.raf.BankService.data.dto.*;
>>>>>>> b23355a (#151 Podrzano placanje uz verifikaciju)
import rs.edu.raf.BankService.exception.*;

import java.util.List;

@Service
public interface AccountService {

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

    List<Account> findAccountsByEmail(String email);
    SavedAccountDto updateSavedAccount(Long accountId, String savedAccountNumber, SavedAccountDto dto);

    void deleteSavedAccount(Long accountId, String savedAccountNumber);
}
