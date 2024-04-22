package rs.edu.raf.BankService.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.edu.raf.BankService.data.dto.CreditDto;
import rs.edu.raf.BankService.data.dto.CreditRequestDto;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.data.entities.credit.Credit;
import rs.edu.raf.BankService.data.entities.credit.CreditRequest;
import rs.edu.raf.BankService.data.enums.CreditRequestStatus;
import rs.edu.raf.BankService.mapper.CreditMapper;
import rs.edu.raf.BankService.repository.AccountRepository;
import rs.edu.raf.BankService.repository.credit.CreditRepository;
import rs.edu.raf.BankService.repository.credit.CreditRequestRepository;
import rs.edu.raf.BankService.service.CreditService;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService {
    private final CreditRepository creditRepository;
    private final CreditRequestRepository creditRequestRepository;
    private final CreditMapper creditMapper;
    private final AccountRepository accountRepository;

    @Transactional
    @Override
    public CreditDto createCredit(Credit credit) {
        Credit credit1 = creditRepository.findCreditByCreditNumber(credit.getCreditNumber());
        if (credit1 != null) {
            throw new RuntimeException("Credit already exists");
        }
        CashAccount cashAccount = accountRepository.findByAccountNumber(credit.getAccountNumber());
        if (cashAccount == null) {
            throw new RuntimeException("Account not found");
        }
        if (!cashAccount.getCurrencyCode().equals(credit.getCurrencyCode())) {
            throw new RuntimeException("Currency is not the same as account currency");
        }

        List<CashAccount> bankCashAccounts = accountRepository.findAllByEmail("bankAccount@bank.rs");
        CashAccount bankAccountSender = bankCashAccounts.stream().filter(account -> account.getCurrencyCode().equals(cashAccount.getCurrencyCode())).findFirst().orElse(null);
        if (bankAccountSender == null) {
            throw new RuntimeException("Bank account not found");
        }
        if (bankAccountSender.getAvailableBalance() < credit.getCreditAmount()) {
            throw new RuntimeException("Not enough funds");
        }
        bankAccountSender.setAvailableBalance((long) (bankAccountSender.getAvailableBalance() - credit.getCreditAmount()));

        cashAccount.setAvailableBalance((long) (cashAccount.getAvailableBalance() + credit.getCreditAmount()));
        accountRepository.saveAll(List.of(cashAccount, bankAccountSender));
        credit = creditRepository.save(credit);
        return creditMapper.creditToCreditDto(credit);
    }

    @Override
    public List<CreditDto> getCreditsByAccountNumber(String accountNumber) {
        return creditRepository.findAllByAccountNumber(accountNumber).stream().map(creditMapper::creditToCreditDto).toList();
    }

    @Override
    public CreditDto getCreditByCreditNumber(Long creditNumber) {
        return creditMapper.creditToCreditDto(creditRepository.findCreditByCreditNumber(creditNumber));
    }

    @Override
    public CreditRequestDto createCreditRequest(CreditRequestDto creditRequestDto) {
        CashAccount cashAccount = accountRepository.findByAccountNumber(creditRequestDto.getAccountNumber());
        if (cashAccount == null) {
            throw new RuntimeException("Account not found");
        }
        creditRequestDto.setCurrency(cashAccount.getCurrencyCode());
        CreditRequest creditRequest = creditMapper.creditRequestDtoToCreditRequest(creditRequestDto);
        creditRequest = creditRequestRepository.save(creditRequest);
        return creditMapper.creditRequestToCreditRequestDto(creditRequest);
    }

    @Override
    public List<CreditRequestDto> getAllCreditRequests() {
        return creditRequestRepository.findAllByStatusIs(CreditRequestStatus.PENDING).stream().map(creditMapper::creditRequestToCreditRequestDto).toList();
    }

    @Override
    public CreditRequestDto getCreditRequestById(Long id) {
        return creditMapper.creditRequestToCreditRequestDto(creditRequestRepository.findCreditRequestById(id));
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public CreditDto approveCreditRequest(Long creditRequestId) {
        CreditRequest creditRequest = creditRequestRepository.findById(creditRequestId).orElseThrow();
        if (creditRequest.getStatus() != CreditRequestStatus.PENDING) {
            throw new RuntimeException("Credit request is not pending");
        }
        CashAccount cashAccount = accountRepository.findByAccountNumber(creditRequest.getAccountNumber());
        if (cashAccount == null) {
            throw new RuntimeException("Account not found");
        }

        creditRequest.setStatus(CreditRequestStatus.APPROVED);
        creditRequest = creditRequestRepository.save(creditRequest);
        CreditDto creditDto = new CreditDto();
        // Create credit ovo je hardkodovano otprilike, jer je specifikacija losa, i ne treba inace ovako da se radi ali tako pise u spec otp
        //ako se spec promeni (a damjanovic je rekao da nece jer je gr4 vec uradila) onda menjati ovaj deo
        creditDto.setCurrencyCode(creditRequest.getCurrency());
        creditDto.setAccountNumber(creditRequest.getAccountNumber());
        creditDto.setCreditCreationDate(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        creditDto.setCreditName(String.valueOf(creditRequest.getCreditType()));
        creditDto.setPaymentPeriodMonths(creditRequest.getPaymentPeriodMonths());
        creditDto.setNominalInterestRate(new Random().nextDouble(5, 10));
        creditDto.setEffectiveInterestRate(new Random().nextDouble(creditDto.getNominalInterestRate(), creditDto.getNominalInterestRate() + 2));
        creditDto.setCreditAmount(creditRequest.getCreditAmount());
        creditDto.setRemainingAmount(creditDto.getCreditAmount() * (1 + (creditDto.getEffectiveInterestRate() / 100)));
        creditDto.setInstallmentAmount(creditDto.getRemainingAmount() / creditDto.getPaymentPeriodMonths());
        creditDto.setNextInstallmentDate(LocalDateTime.now().plusMonths(1).toEpochSecond(ZoneOffset.UTC));
        creditDto.setCreditExpirationDate(LocalDateTime.now().plusMonths(creditDto.getPaymentPeriodMonths()).toEpochSecond(ZoneOffset.UTC));


        return createCredit(creditMapper.creditDtoToCredit(creditDto));
    }

    @Override
    public Boolean denyCreditRequest(Long creditRequestId) {
        CreditRequest creditRequest = creditRequestRepository.findById(creditRequestId).orElseThrow();
        if (creditRequest.getStatus() != CreditRequestStatus.PENDING) {
            return false;
        }
        creditRequest.setStatus(CreditRequestStatus.REJECTED);
        creditRequestRepository.save(creditRequest);
        return true;
    }


}
