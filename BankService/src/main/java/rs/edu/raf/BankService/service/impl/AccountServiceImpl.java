package rs.edu.raf.BankService.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.data.dto.*;
import rs.edu.raf.BankService.data.entities.accounts.*;
import rs.edu.raf.BankService.data.entities.SavedAccount;
import rs.edu.raf.BankService.data.entities.UserAccountUserProfileActivationCode;
import rs.edu.raf.BankService.data.entities.accounts.BusinessCashAccount;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
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
        CashAccount cashAccount = accountRepository.findByAccountNumber(accountNumberDto.getAccountNumber());
        if (cashAccount != null) {
            UserAccountUserProfileLinkState userAccountUserProfileLinkState = cashAccount.getLinkState();
            if (userAccountUserProfileLinkState.equals(UserAccountUserProfileLinkState.NOT_ASSOCIATED)) {
                generateActivationCodeAndSendToQueue(accountNumberDto.getAccountNumber(), cashAccount.getEmail());
                cashAccount.setLinkState(UserAccountUserProfileLinkState.IN_PROCESS);
                accountRepository.saveAndFlush(cashAccount);
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
        CashAccount cashAccount = accountRepository.findByAccountNumber(accountNumber);
        if (cashAccount != null) {
            UserAccountUserProfileActivationCode token = userAccountUserProfileActivationCodeRepository.findByAccountNumber(accountNumber);
            if (Integer.valueOf(token.getCode()).equals(code)) {
                if (token.isExpired()) {
                    throw new ActivationCodeExpiredException();
                }
                userAccountUserProfileActivationCodeRepository.delete(token);
                cashAccount.setLinkState(UserAccountUserProfileLinkState.ASSOCIATED);
                accountRepository.saveAndFlush(cashAccount);
                return true;
            } else {
                throw new ActivationCodeDoesNotMatchException();
            }
        }
        return false;
    }

    @Override
    public DomesticCurrencyAccountDto createDomesticCurrencyAccount(DomesticCurrencyAccountDto dto) throws AccountNumberAlreadyExistException {
        CashAccount cashAccount = accountRepository.findByAccountNumber(dto.getAccountNumber());
        if (cashAccount != null) {
            throw new AccountNumberAlreadyExistException(dto.getAccountNumber());
        }
        accountRepository.saveAndFlush(accountMapper.domesticAccountDtoToDomesticAccount(dto));
        return dto;
    }

    @Override
    public ForeignCurrencyAccountDto createForeignCurrencyAccount(ForeignCurrencyAccountDto dto) throws AccountNumberAlreadyExistException {
        CashAccount cashAccount = accountRepository.findByAccountNumber(dto.getAccountNumber());
        if (cashAccount != null) {
            throw new AccountNumberAlreadyExistException(dto.getAccountNumber());
        }
        accountRepository.save(accountMapper.foreignAccountDtoToForeignAccount(dto));
        return dto;
    }

    @Override
    public BusinessAccountDto createBusinessAccount(BusinessAccountDto dto) throws AccountNumberAlreadyExistException {
        CashAccount cashAccount = accountRepository.findByAccountNumber(dto.getAccountNumber());
        if (cashAccount != null) {
            throw new AccountNumberAlreadyExistException(dto.getAccountNumber());
        }
        accountRepository.save(accountMapper.businessAccountDtoToBusinessAccount(dto));
        return dto;
    }

    @Override
    public List<AccountDto> findAccountsByEmail(String email) {
        List<CashAccount> cashAccounts = accountRepository.findAllByEmail(email);
        if (cashAccounts.isEmpty()) {
            return List.of();
        }
        //     return accounts.stream().map(accountMapper::accountToAccountDto).toList();
        return cashAccounts.stream().map((account) -> {
            if (account instanceof DomesticCurrencyCashAccount) {
                return accountMapper.domesticCurrencyAccountToDomesticCurrencyAccountDtoDto((DomesticCurrencyCashAccount) account);
            } else if (account instanceof ForeignCurrencyCashAccount) {
                return accountMapper.foreignCurrencyAccountToForeignCurrencyAccountDtoDto((ForeignCurrencyCashAccount) account);
            } else if (account instanceof BusinessCashAccount) {
                return accountMapper.businessAccountToBusinessAccountDto((BusinessCashAccount) account);
            }
            return null;
        }).toList();

    }

    @Transactional(dontRollbackOn = Exception.class)
    @Scheduled(cron = "0 */5 * * * *") //every 5 minute
    @SchedulerLock(name = "tasksScheduler-1")
    public void executeScheduledTasks() {
        logger.info("Executing scheduled tasks");
        userAccountUserProfileActivationCodeRepository.findAll().forEach(token -> {
            if (token.isExpired()) {
                userAccountUserProfileActivationCodeRepository.delete(token);
                CashAccount cashAccount = accountRepository.findByAccountNumber(token.getAccountNumber());
                cashAccount.setLinkState(UserAccountUserProfileLinkState.NOT_ASSOCIATED);
                accountRepository.saveAndFlush(cashAccount);
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

    @Transactional
    @Override
    public SavedAccountDto createSavedAccount(Long accountId, SavedAccountDto dto) {
        CashAccount cashAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        SavedAccount savedAccount = new SavedAccount();
        savedAccount.setName(dto.getName());
        savedAccount.setAccountNumber(dto.getAccountNumber());

        cashAccount.getSavedAccounts().add(savedAccount);
        accountRepository.save(cashAccount);

        return dto;
    }

    @Transactional
    @Override
    public SavedAccountDto updateSavedAccount(Long accountId, String savedAccountNumber, SavedAccountDto dto) {
        CashAccount cashAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        SavedAccount savedAccountToUpdate = cashAccount.getSavedAccounts().stream()
                .filter(savedAccount -> savedAccount.getAccountNumber().equals(savedAccountNumber))
                .findFirst()
                .orElseThrow(() -> new AccountNotFoundException("Saved account not found"));

        savedAccountToUpdate.setName(dto.getName());
        savedAccountToUpdate.setAccountNumber(dto.getAccountNumber());

        accountRepository.save(cashAccount);

        return dto;
    }

    @Override
    public void deleteSavedAccount(Long accountId, String savedAccountNumber) {
        CashAccount cashAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        cashAccount.getSavedAccounts().removeIf(savedAccount -> savedAccount.getAccountNumber().equals(savedAccountNumber));
        accountRepository.save(cashAccount);
    }
}
