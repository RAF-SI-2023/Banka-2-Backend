package rs.edu.raf.BankService.service;

import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.data.dto.AccountNumberDto;
import rs.edu.raf.BankService.data.dto.BusinessAccountDto;
import rs.edu.raf.BankService.data.dto.DomesticCurrencyAccountDto;
import rs.edu.raf.BankService.data.dto.ForeignCurrencyAccountDto;
import rs.edu.raf.BankService.data.entities.Account;
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


    List<Account> findAccountsByEmail(String email);
}
