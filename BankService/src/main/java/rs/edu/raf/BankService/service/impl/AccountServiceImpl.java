package rs.edu.raf.BankService.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.data.dto.AccountNumberDto;
import rs.edu.raf.BankService.data.dto.DomesticCurrencyAccountDto;
import rs.edu.raf.BankService.data.dto.EmailDto;
import rs.edu.raf.BankService.data.dto.ForeignCurrencyAccountDto;
import rs.edu.raf.BankService.data.entities.Account;
import rs.edu.raf.BankService.data.entities.UserAccountUserProfileConnectionToken;
import rs.edu.raf.BankService.data.enums.UserAccountUserProfileLinkState;
import rs.edu.raf.BankService.exception.*;
import rs.edu.raf.BankService.mapper.AccountMapper;
import rs.edu.raf.BankService.repository.AccountRepository;
import rs.edu.raf.BankService.repository.UserAccountUserProfileConnectionTokenRepository;
import rs.edu.raf.BankService.service.AccountService;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;
    private final UserAccountUserProfileConnectionTokenRepository userAccountUserProfileConnectionTokenRepository;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public boolean userAccountUserProfileConnectionAttempt(AccountNumberDto accountNumberDto) {
       Account account = accountRepository.findByAccountNumber(accountNumberDto.getAccountNumber());
        if(account != null){
            UserAccountUserProfileLinkState userAccountUserProfileLinkState = account.getLinkedWithUserProfile();
            if(userAccountUserProfileLinkState.equals(UserAccountUserProfileLinkState.NOT_ASSOCIATED)){
                generateActivationCodeAndSendToQueue(accountNumberDto.getAccountNumber(),account.getEmail());
                account.setLinkedWithUserProfile(UserAccountUserProfileLinkState.IN_PROCESS);
                accountRepository.saveAndFlush(account);
                return true;
            }
            else if(userAccountUserProfileLinkState.equals(UserAccountUserProfileLinkState.IN_PROCESS)){
                throw new UserAccountLinkingWithUserProfileInProcessException(accountNumberDto.getAccountNumber());
            }
            else{
                throw new UserAccountAlreadyAssociatedWithUserProfileException(accountNumberDto.getAccountNumber());
            }
        }
        else{
            throw new AccountNotFoundException(accountNumberDto.getAccountNumber());
        }
    }

    @Override
    public boolean confirmActivationCode(String accountNumber, Integer code) throws ActivationCodeExpiredException {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if(account != null){
            UserAccountUserProfileConnectionToken token = userAccountUserProfileConnectionTokenRepository.findByAccountNumber(accountNumber);
            if(token.getCode().equals(code)){
                if(token.isExpired()){
                    throw new ActivationCodeExpiredException();
                }
                userAccountUserProfileConnectionTokenRepository.delete(token);
                account.setLinkedWithUserProfile(UserAccountUserProfileLinkState.ASSOCIATED);
                accountRepository.saveAndFlush(account);
                return true;
            }
            else{
                throw new ActivationCodeDoesNotMatchException();
            }
        }
        return false;
    }

    @Override
    public DomesticCurrencyAccountDto createDomesticCurrencyAccount(DomesticCurrencyAccountDto dto) throws AccountNumberAlreadyExistException {
        Account account = accountRepository.findByAccountNumber(dto.getAccountNumber());
        if(account != null){
            throw new AccountNumberAlreadyExistException(dto.getAccountNumber());
        }
        accountRepository.saveAndFlush(accountMapper.domesticAccountDtoToDomesticAccount(dto));
        return dto;
    }

    @Override
    public ForeignCurrencyAccountDto createForeignCurrencyAccount(ForeignCurrencyAccountDto dto) throws AccountNumberAlreadyExistException {
        Account account = accountRepository.findByAccountNumber(dto.getAccountNumber());
        if(account != null){
            throw new AccountNumberAlreadyExistException(dto.getAccountNumber());
        }
        accountRepository.saveAndFlush(accountMapper.foreignAccountDtoToForeignAccount(dto));
        return dto;
    }

    private void generateActivationCodeAndSendToQueue(String accountNumber, String email){
        Integer code = generateActivationCode();
        UserAccountUserProfileConnectionToken token = new UserAccountUserProfileConnectionToken(accountNumber, code);
        userAccountUserProfileConnectionTokenRepository.saveAndFlush(token);
        sendActivationCodeToSendToQueue(new EmailDto(email, code));
    }

    private Integer generateActivationCode(){
        return new Random().nextInt(100000, 999999);
    }

    private void sendActivationCodeToSendToQueue(EmailDto emailDto){
        rabbitTemplate.convertAndSend("user-profile-activation-code", emailDto);
    }
}
