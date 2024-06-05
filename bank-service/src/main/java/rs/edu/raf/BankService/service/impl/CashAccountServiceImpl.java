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
import rs.edu.raf.BankService.data.entities.SavedAccount;
import rs.edu.raf.BankService.data.entities.UserAccountUserProfileActivationCode;
import rs.edu.raf.BankService.data.entities.accounts.BusinessCashAccount;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.data.entities.accounts.DomesticCurrencyCashAccount;
import rs.edu.raf.BankService.data.entities.accounts.ForeignCurrencyCashAccount;
import rs.edu.raf.BankService.data.entities.transactions.AdditionTransferTransaction;
import rs.edu.raf.BankService.data.entities.transactions.SubtractionTransferTransaction;
import rs.edu.raf.BankService.data.enums.TransactionStatus;
import rs.edu.raf.BankService.data.enums.UserAccountUserProfileLinkState;
import rs.edu.raf.BankService.exception.*;
import rs.edu.raf.BankService.mapper.AccountMapper;
import rs.edu.raf.BankService.mapper.TransactionMapper;
import rs.edu.raf.BankService.repository.CashAccountRepository;
import rs.edu.raf.BankService.repository.CashTransactionRepository;
import rs.edu.raf.BankService.repository.UserAccountUserProfileActivationCodeRepository;
import rs.edu.raf.BankService.service.CashAccountService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CashAccountServiceImpl implements CashAccountService {

    private static final Logger logger = LoggerFactory.getLogger(CashAccountServiceImpl.class);

    private final AccountMapper accountMapper;
    private final TransactionMapper transactionMapper;
    private final CashAccountRepository cashAccountRepository;
    private final UserAccountUserProfileActivationCodeRepository userAccountUserProfileActivationCodeRepository;
    private final CashTransactionRepository cashTransactionRepository;
    private final RabbitTemplate rabbitTemplate;


    @Override
    public boolean userAccountUserProfileConnectionAttempt(AccountNumberDto accountNumberDto) {
        CashAccount cashAccount = cashAccountRepository.findByAccountNumber(accountNumberDto.getAccountNumber());
        if (cashAccount != null) {
            UserAccountUserProfileLinkState userAccountUserProfileLinkState = cashAccount.getLinkState();
            if (userAccountUserProfileLinkState.equals(UserAccountUserProfileLinkState.NOT_ASSOCIATED)) {
                generateActivationCodeAndSendToQueue(accountNumberDto.getAccountNumber(), cashAccount.getEmail());
                cashAccount.setLinkState(UserAccountUserProfileLinkState.IN_PROCESS);
                cashAccountRepository.saveAndFlush(cashAccount);
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
        CashAccount cashAccount = cashAccountRepository.findByAccountNumber(accountNumber);
        if (cashAccount != null) {
            UserAccountUserProfileActivationCode token = userAccountUserProfileActivationCodeRepository.findByAccountNumber(accountNumber);
            if (Integer.valueOf(token.getCode()).equals(code)) {
                if (token.isExpired()) {
                    throw new ActivationCodeExpiredException();
                }
                userAccountUserProfileActivationCodeRepository.delete(token);
                cashAccount.setLinkState(UserAccountUserProfileLinkState.ASSOCIATED);
                cashAccountRepository.saveAndFlush(cashAccount);
                return true;
            } else {
                throw new ActivationCodeDoesNotMatchException();
            }
        }
        return false;
    }

    @Override
    public DomesticCurrencyAccountDto createDomesticCurrencyAccount(DomesticCurrencyAccountDto dto) throws AccountNumberAlreadyExistException {
        CashAccount cashAccount = cashAccountRepository.findByAccountNumber(dto.getAccountNumber());
        if (cashAccount != null) {
            throw new AccountNumberAlreadyExistException(dto.getAccountNumber());
        }
        cashAccountRepository.saveAndFlush(accountMapper.domesticAccountDtoToDomesticAccount(dto));
        becomePrimaryAccount(accountMapper.domesticAccountDtoToDomesticAccount(dto));

        return dto;
    }

    @Override
    public ForeignCurrencyAccountDto createForeignCurrencyAccount(ForeignCurrencyAccountDto dto) throws AccountNumberAlreadyExistException {
        CashAccount cashAccount = cashAccountRepository.findByAccountNumber(dto.getAccountNumber());
        if (cashAccount != null) {
            throw new AccountNumberAlreadyExistException(dto.getAccountNumber());
        }
        cashAccountRepository.save(accountMapper.foreignAccountDtoToForeignAccount(dto));
        becomePrimaryAccount(accountMapper.foreignAccountDtoToForeignAccount(dto));
        return dto;
    }

    @Override
    public BusinessAccountDto createBusinessAccount(BusinessAccountDto dto) throws AccountNumberAlreadyExistException {
        CashAccount cashAccount = cashAccountRepository.findByAccountNumber(dto.getAccountNumber());
        if (cashAccount != null) {
            throw new AccountNumberAlreadyExistException(dto.getAccountNumber());
        }
        cashAccountRepository.save(accountMapper.businessAccountDtoToBusinessAccount(dto));
        becomePrimaryAccount(accountMapper.businessAccountDtoToBusinessAccount(dto));
        return dto;
    }

    @Override
    public List<AccountDto> findAccountsByEmail(String email) {
        List<CashAccount> cashAccounts = cashAccountRepository.findAllByEmail(email);
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

    @Override
    public List<AccountDto> findAccountByMoneyStatus(MoneyStatusDto moneyStatusDto) throws AccountNotFoundException{
        List<CashAccount> cashAccounts = cashAccountRepository.findAll();
        List<AccountDto> accountsDto = new ArrayList<>();
        if(cashAccounts != null){
            for (CashAccount ca : cashAccounts) {
                if ( (ca.getCurrencyCode().equals(moneyStatusDto.getCurrency())) && (moneyStatusDto.getAvailableBalance() == ca.getAvailableBalance())
                        && (ca.getReservedFunds() == moneyStatusDto.getReservedFunds()) && ((ca.getReservedFunds()+ca.getAvailableBalance()) == moneyStatusDto.getTotal())
                        && (ca instanceof DomesticCurrencyCashAccount)) {
                    accountsDto.add(accountMapper.domesticCurrencyAccountToDomesticCurrencyAccountDtoDto((DomesticCurrencyCashAccount) ca));
                } else if ((ca.getCurrencyCode().equals(moneyStatusDto.getCurrency())) && (moneyStatusDto.getAvailableBalance() == ca.getAvailableBalance())
                        && (ca.getReservedFunds() == moneyStatusDto.getReservedFunds()) && ((ca.getReservedFunds()+ca.getAvailableBalance()) == moneyStatusDto.getTotal())
                        &&(ca instanceof ForeignCurrencyCashAccount)) {
                    accountsDto.add(accountMapper.foreignCurrencyAccountToForeignCurrencyAccountDtoDto((ForeignCurrencyCashAccount) ca));
                } else if ((ca.getCurrencyCode().equals(moneyStatusDto.getCurrency())) && (moneyStatusDto.getAvailableBalance() == ca.getAvailableBalance())
                        && (ca.getReservedFunds() == moneyStatusDto.getReservedFunds()) && ((ca.getReservedFunds()+ca.getAvailableBalance()) == moneyStatusDto.getTotal())
                        &&(ca instanceof BusinessCashAccount)) {
                    accountsDto.add(accountMapper.businessAccountToBusinessAccountDto((BusinessCashAccount) ca));
                }
            }
        }
        else{
            throw new AccountNotFoundException("");
        }
        return accountsDto;
    }



    @Transactional(dontRollbackOn = Exception.class)
    @Scheduled(cron = "0 */5 * * * *") //every 5 minute
    @SchedulerLock(name = "tasksScheduler-1")
    public void executeScheduledTasks() {
        logger.info("Executing scheduled tasks");
        userAccountUserProfileActivationCodeRepository.findAll().forEach(token -> {
            if (token.isExpired()) {
                userAccountUserProfileActivationCodeRepository.delete(token);
                CashAccount cashAccount = cashAccountRepository.findByAccountNumber(token.getAccountNumber());
                cashAccount.setLinkState(UserAccountUserProfileLinkState.NOT_ASSOCIATED);
                cashAccountRepository.saveAndFlush(cashAccount);
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
        CashAccount cashAccount = cashAccountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        SavedAccount savedAccount = new SavedAccount();
        savedAccount.setName(dto.getName());
        savedAccount.setAccountNumber(dto.getAccountNumber());

        cashAccount.getSavedAccounts().add(savedAccount);
        cashAccountRepository.save(cashAccount);

        becomePrimaryAccount(cashAccount);

        return dto;
    }

    @Override
    public void becomePrimaryAccount(CashAccount cashAccount) {
        List<CashAccount> cashAccounts = cashAccountRepository.findAllByEmail(cashAccount.getEmail());

        System.out.println(cashAccounts.size()+" VELICINA");
        if (cashAccounts.size() == 1) {
            cashAccount.setPrimaryTradingAccount(true);
            cashAccountRepository.save(cashAccount);
        }

    }

    @Transactional
    @Override
    public SavedAccountDto updateSavedAccount(Long accountId, String savedAccountNumber, SavedAccountDto dto) {
        CashAccount cashAccount = cashAccountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        SavedAccount savedAccountToUpdate = cashAccount.getSavedAccounts().stream()
                .filter(savedAccount -> savedAccount.getAccountNumber().equals(savedAccountNumber))
                .findFirst()
                .orElseThrow(() -> new AccountNotFoundException("Saved account not found"));

        savedAccountToUpdate.setName(dto.getName());
        savedAccountToUpdate.setAccountNumber(dto.getAccountNumber());

        cashAccountRepository.save(cashAccount);

        return dto;
    }

    @Override
    public void deleteSavedAccount(Long accountId, String savedAccountNumber) {
        CashAccount cashAccount = cashAccountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        cashAccount.getSavedAccounts().removeIf(savedAccount -> savedAccount.getAccountNumber().equals(savedAccountNumber));
        cashAccountRepository.save(cashAccount);
    }

    @Override
    public boolean setIsAccountPrimaryForTrading(String accountNumber, boolean usedForSecurities) {
        CashAccount cashAccount = cashAccountRepository.findByAccountNumber(accountNumber);
        if (cashAccount != null) {
            cashAccount.setPrimaryTradingAccount(usedForSecurities);
            cashAccountRepository.saveAndFlush(cashAccount);
            return true;
        }
        return false;
    }

    @Override
    public boolean setIsAccountPrimaryForTrading(String accountNumber) {
        return false;
    }

    @Override
    public boolean depositWithdrawalAddition(DepositWithdrawalDto depositWithdrawalDto) {

        CashAccount cashAccount = cashAccountRepository.findByAccountNumber(depositWithdrawalDto.getAccountNumber());
        if(cashAccount == null){
            throw new AccountNotFoundException(depositWithdrawalDto.getAccountNumber());
        }

        cashAccount.setAvailableBalance(cashAccount.getAvailableBalance()+depositWithdrawalDto.getAmount());
        cashAccountRepository.save(cashAccount);

        AdditionTransferTransaction additionTransferTransaction = new AdditionTransferTransaction();
        additionTransferTransaction.setAmount(depositWithdrawalDto.getAmount());
        additionTransferTransaction.setSenderCashAccount(cashAccount);
        additionTransferTransaction.setReceiverCashAccount(cashAccount);
        additionTransferTransaction.setCreatedAt(LocalDateTime.now());
        additionTransferTransaction.setStatus(TransactionStatus.CONFIRMED);

        cashTransactionRepository.save(additionTransferTransaction);

        return true;
    }

    @Override
    public boolean depositWithdrawalSubtraction(DepositWithdrawalDto depositWithdrawalDto) {

       CashAccount cashAccount = cashAccountRepository.findByAccountNumber(depositWithdrawalDto.getAccountNumber());

        if(cashAccount == null){
            throw new AccountNotFoundException(depositWithdrawalDto.getAccountNumber());
        }

        if(Math.abs(depositWithdrawalDto.getAmount()) < cashAccount.getAvailableBalance()) {
            cashAccount.setAvailableBalance(cashAccount.getAvailableBalance() - Math.abs(depositWithdrawalDto.getAmount()));
            cashAccountRepository.save(cashAccount);

        }
        else{
            throw new RuntimeException("Not enough money in balance to pay");
        }


        SubtractionTransferTransaction transferTransaction = new SubtractionTransferTransaction();
        transferTransaction.setAmount(depositWithdrawalDto.getAmount());
        transferTransaction.setSenderCashAccount(cashAccount);
        transferTransaction.setReceiverCashAccount(cashAccount);
        transferTransaction.setCreatedAt(LocalDateTime.now());
        transferTransaction.setStatus(TransactionStatus.CONFIRMED);


        cashTransactionRepository.save(transferTransaction);

        return true;
    }



}
