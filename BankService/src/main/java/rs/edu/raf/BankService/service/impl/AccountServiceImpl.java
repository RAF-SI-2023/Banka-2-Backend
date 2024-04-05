package rs.edu.raf.BankService.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.bootstrap.BootstrapData;
import rs.edu.raf.BankService.data.dto.*;
import rs.edu.raf.BankService.data.entities.Account;
import rs.edu.raf.BankService.data.entities.UserAccountUserProfileActivationCode;
import rs.edu.raf.BankService.data.enums.UserAccountUserProfileLinkState;
import rs.edu.raf.BankService.exception.*;
import rs.edu.raf.BankService.mapper.AccountMapper;
import rs.edu.raf.BankService.repository.AccountRepository;
import rs.edu.raf.BankService.repository.UserAccountUserProfileActivationCodeRepository;
import rs.edu.raf.BankService.service.AccountService;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;
    private final UserAccountUserProfileActivationCodeRepository userAccountUserProfileActivationCodeRepository;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public boolean userAccountUserProfileConnectionAttempt(AccountNumberDto accountNumberDto) {
        Account account = accountRepository.findByAccountNumber(accountNumberDto.getAccountNumber());
        if (account != null) {
            UserAccountUserProfileLinkState userAccountUserProfileLinkState = account.getLinkState();
            if (userAccountUserProfileLinkState.equals(UserAccountUserProfileLinkState.NOT_ASSOCIATED)) {
                generateActivationCodeAndSendToQueue(accountNumberDto.getAccountNumber(), account.getEmail());
                account.setLinkState(UserAccountUserProfileLinkState.IN_PROCESS);
                accountRepository.saveAndFlush(account);
                return true;
            } else if (userAccountUserProfileLinkState.equals(UserAccountUserProfileLinkState.IN_PROCESS)) {
                throw new UserAccountInProcessOfBindingWithUserProfileException(accountNumberDto.getAccountNumber());
            } else {
                throw new UserAccountAlreadyAssociatedWithUserProfileException(accountNumberDto.getAccountNumber());
            }
        } else {
            throw new AccountNotFoundException(accountNumberDto.getAccountNumber());
        }
    }

    @Override
    public boolean confirmActivationCode(String accountNumber, Integer code) throws ActivationCodeExpiredException {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account != null) {
            UserAccountUserProfileActivationCode token = userAccountUserProfileActivationCodeRepository.findByAccountNumber(accountNumber);
            if (Integer.valueOf(token.getCode()).equals(code)) {
                if (token.isExpired()) {
                    throw new ActivationCodeExpiredException();
                }
                userAccountUserProfileActivationCodeRepository.delete(token);
                account.setLinkState(UserAccountUserProfileLinkState.ASSOCIATED);
                accountRepository.saveAndFlush(account);
                return true;
            } else {
                throw new ActivationCodeDoesNotMatchException();
            }
        }
        return false;
    }

    @Override
    public DomesticCurrencyAccountDto createDomesticCurrencyAccount(DomesticCurrencyAccountDto dto) throws AccountNumberAlreadyExistException {
        Account account = accountRepository.findByAccountNumber(dto.getAccountNumber());
        if (account != null) {
            throw new AccountNumberAlreadyExistException(dto.getAccountNumber());
        }
        accountRepository.saveAndFlush(accountMapper.domesticAccountDtoToDomesticAccount(dto));
        return dto;
    }

    @Override
    public ForeignCurrencyAccountDto createForeignCurrencyAccount(ForeignCurrencyAccountDto dto) throws AccountNumberAlreadyExistException {
        Account account = accountRepository.findByAccountNumber(dto.getAccountNumber());
        if (account != null) {
            throw new AccountNumberAlreadyExistException(dto.getAccountNumber());
        }
        accountRepository.save(accountMapper.foreignAccountDtoToForeignAccount(dto));
        return dto;
    }

    @Override
    public BusinessAccountDto createBusinessAccount(BusinessAccountDto dto) throws AccountNumberAlreadyExistException {
        Account account = accountRepository.findByAccountNumber(dto.getAccountNumber());
        if (account != null) {
            throw new AccountNumberAlreadyExistException(dto.getAccountNumber());
        }
        accountRepository.save(accountMapper.businessAccountDtoToBusinessAccount(dto));
        return dto;
    }

    @Override
    public List<Account> findAccountsByEmail(String email) {
        List<Account> accounts = accountRepository.findAllByEmail(email);
        if (accounts.isEmpty()) {
            throw new AccountNotFoundException(email);
        }
        return accounts;
    }

    @Transactional(dontRollbackOn = Exception.class)
    @Scheduled(cron = "0 */5 * * * *") //every 5 minute
    @SchedulerLock(name = "tasksScheduler-1")
    public void executeScheduledTasks() {
        logger.info("Executing scheduled tasks");
        userAccountUserProfileActivationCodeRepository.findAll().forEach(token -> {
            if (token.isExpired()) {
                userAccountUserProfileActivationCodeRepository.delete(token);
                Account account = accountRepository.findByAccountNumber(token.getAccountNumber());
                account.setLinkState(UserAccountUserProfileLinkState.NOT_ASSOCIATED);
                accountRepository.saveAndFlush(account);
            }
        });
    }


    private void generateActivationCodeAndSendToQueue(String accountNumber, String email) {
        String code = generateActivationCode();
        UserAccountUserProfileActivationCode token = new UserAccountUserProfileActivationCode(accountNumber, code);
        userAccountUserProfileActivationCodeRepository.saveAndFlush(token);
        sendActivationCodeToSendToQueue(new EmailDto(email, code));
    }

    private String generateActivationCode() {
        return String.valueOf(new Random().nextInt(100000, 999999));
    }

    private void sendActivationCodeToSendToQueue(EmailDto emailDto) {
        rabbitTemplate.convertAndSend("user-profile-activation-code", emailDto);
    }

}
