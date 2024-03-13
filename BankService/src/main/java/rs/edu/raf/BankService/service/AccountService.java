package rs.edu.raf.BankService.service;

import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.data.dto.AccountNumberDto;
import rs.edu.raf.BankService.data.dto.DomesticCurrencyAccountDto;
import rs.edu.raf.BankService.data.dto.ForeignCurrencyAccountDto;
import rs.edu.raf.BankService.exception.AccountNumberAlreadyExistException;
import rs.edu.raf.BankService.exception.ActivationCodeExpiredException;
import rs.edu.raf.BankService.exception.UserAccountAlreadyAssociatedWithUserProfileException;
import rs.edu.raf.BankService.exception.UserAccountLinkingWithUserProfileInProcessException;

@Service
public interface AccountService {

    boolean userAccountUserProfileConnectionAttempt(AccountNumberDto accountNumberDto)
            throws UserAccountAlreadyAssociatedWithUserProfileException, UserAccountLinkingWithUserProfileInProcessException;

    boolean confirmActivationCode(String accountNumber, Integer code)
            throws ActivationCodeExpiredException, ActivationCodeExpiredException;

    DomesticCurrencyAccountDto createDomesticCurrencyAccount(DomesticCurrencyAccountDto dto)
            throws AccountNumberAlreadyExistException;

    ForeignCurrencyAccountDto createForeignCurrencyAccount(ForeignCurrencyAccountDto dto)
            throws AccountNumberAlreadyExistException;

}
