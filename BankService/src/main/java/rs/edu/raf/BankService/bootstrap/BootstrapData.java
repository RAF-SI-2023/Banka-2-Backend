package rs.edu.raf.BankService.bootstrap;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rs.edu.raf.BankService.bootstrap.exchangeRatesUtils.ExchangeRateApiResponse;
import rs.edu.raf.BankService.bootstrap.exchangeRatesUtils.ExchangeRateBootstrapUtil;
import rs.edu.raf.BankService.data.entities.accounts.Account;
import rs.edu.raf.BankService.data.entities.accounts.DomesticCurrencyAccount;
import rs.edu.raf.BankService.data.entities.accounts.ForeignCurrencyAccount;
import rs.edu.raf.BankService.data.entities.card.Card;
import rs.edu.raf.BankService.data.entities.credit.Credit;
import rs.edu.raf.BankService.data.entities.credit.CreditRequest;
import rs.edu.raf.BankService.data.entities.exchangeCurrency.ExchangeRates;
import rs.edu.raf.BankService.data.enums.*;
import rs.edu.raf.BankService.repository.AccountRepository;
import rs.edu.raf.BankService.repository.CardRepository;
import rs.edu.raf.BankService.repository.ExchangeRateRepository;
import rs.edu.raf.BankService.repository.ForeignCurrencyHolderRepository;
import rs.edu.raf.BankService.repository.credit.CreditRepository;
import rs.edu.raf.BankService.repository.credit.CreditRequestRepository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    @Value("${MY_EMAIL_1}")
    private String myEmail1;

    @Value("${MY_EMAIL_2}")
    private String myEmail2;

    @Value("${MY_EMAIL_3}")
    private String myEmail3;

    @Value("${MY_EMAIL_4}")
    private String myEmail4;

    @Value("${MY_EMAIL_5}")
    private String myEmail5;

    private static final Logger logger = LoggerFactory.getLogger(BootstrapData.class);
    private final AccountRepository accountRepository;
    private final CreditRepository creditRepository;
    private final CreditRequestRepository creditRequestRepository;
    private final CardRepository cardRepository;
    private final ExchangeRateRepository exchangeRateRepository;

    @Override
    public void run(String... args) throws Exception {
        logger.info("BankService: DATA LOADING IN PROGRESS...");
        if (accountRepository.count() == 0) {
            DomesticCurrencyAccount domesticCurrencyAccount1 = new DomesticCurrencyAccount();
            domesticCurrencyAccount1.setAccountNumber("3334444999999999");
            domesticCurrencyAccount1.setEmail(myEmail1);
            domesticCurrencyAccount1.setAccountType(AccountType.DOMESTIC_CURRENCY_ACCOUNT);
            domesticCurrencyAccount1.setEmployeeId(2L);
            domesticCurrencyAccount1.setMaintenanceFee(220.00);
            domesticCurrencyAccount1.setCurrencyCode("RSD");
            domesticCurrencyAccount1.setAvailableBalance(100000L);
            domesticCurrencyAccount1.setDomesticCurrencyAccountType(DomesticCurrencyAccountType.RETIREMENT);
            domesticCurrencyAccount1.setInterestRate(2.5);
            accountRepository.save((Account) domesticCurrencyAccount1);

            DomesticCurrencyAccount domesticCurrencyAccount2 = new DomesticCurrencyAccount();
            domesticCurrencyAccount2.setAccountNumber("3334444111111111");
            domesticCurrencyAccount2.setEmail(myEmail2);
            domesticCurrencyAccount2.setAccountType(AccountType.DOMESTIC_CURRENCY_ACCOUNT);
            domesticCurrencyAccount2.setEmployeeId(2L);
            domesticCurrencyAccount2.setMaintenanceFee(220.00);
            domesticCurrencyAccount2.setAvailableBalance(100000L);
            domesticCurrencyAccount2.setCurrencyCode("RSD");
            domesticCurrencyAccount2.setDomesticCurrencyAccountType(DomesticCurrencyAccountType.RETIREMENT);
            domesticCurrencyAccount2.setInterestRate(2.5);
            accountRepository.save((Account) domesticCurrencyAccount2);

            ForeignCurrencyAccount foreignCurrencyAccount1 = new ForeignCurrencyAccount();
            foreignCurrencyAccount1.setAccountNumber("3334444777777777");
            foreignCurrencyAccount1.setEmail(myEmail1);
            foreignCurrencyAccount1.setAccountType(AccountType.FOREIGN_CURRENCY_ACCOUNT);
            foreignCurrencyAccount1.setEmployeeId(2L);
            foreignCurrencyAccount1.setMaintenanceFee(220.00);
            foreignCurrencyAccount1.setCurrencyCode("USD");
            foreignCurrencyAccount1.setAvailableBalance(500L);
            accountRepository.saveAndFlush(foreignCurrencyAccount1);

            ForeignCurrencyAccount foreignCurrencyAccount2 = new ForeignCurrencyAccount();
            foreignCurrencyAccount2.setAccountNumber("3334444888888888");
            foreignCurrencyAccount2.setEmail(myEmail1);
            foreignCurrencyAccount2.setAccountType(AccountType.FOREIGN_CURRENCY_ACCOUNT);
            foreignCurrencyAccount2.setEmployeeId(2L);
            foreignCurrencyAccount2.setMaintenanceFee(22.00);
            foreignCurrencyAccount2.setCurrencyCode("EUR");
            foreignCurrencyAccount2.setAvailableBalance(450L);
            accountRepository.saveAndFlush(foreignCurrencyAccount2);
            ForeignCurrencyAccount foreignCurrencyAccount3 = new ForeignCurrencyAccount();
            foreignCurrencyAccount3.setAccountNumber("3330000000000000");
            foreignCurrencyAccount3.setEmail(myEmail2);
            foreignCurrencyAccount3.setAccountType(AccountType.FOREIGN_CURRENCY_ACCOUNT);
            foreignCurrencyAccount3.setEmployeeId(2L);
            foreignCurrencyAccount3.setMaintenanceFee(20.00);
            foreignCurrencyAccount3.setCurrencyCode("EUR");
            foreignCurrencyAccount3.setAvailableBalance(550L);

            accountRepository.saveAndFlush(foreignCurrencyAccount3);
            // Create bank accounts for all allowed currencies

            int i = 0;
            for (String currency : ExchangeRateBootstrapUtil.allowedCurrencies) {
                if (currency.equals("RSD")) {
                    DomesticCurrencyAccount domesticBankAccount = new DomesticCurrencyAccount();
                    domesticBankAccount.setAccountNumber("000000000000000" + i);
                    domesticBankAccount.setEmail("bankAccount@bank.rs");
                    domesticBankAccount.setAccountType(AccountType.BANK_ACCOUNT);
                    domesticBankAccount.setEmployeeId(2L);
                    domesticBankAccount.setMaintenanceFee(0.00);
                    domesticBankAccount.setLinkState(UserAccountUserProfileLinkState.ASSOCIATED);
                    domesticBankAccount.setInterestRate(0.0);
                    domesticBankAccount.setCurrencyCode(currency);
                    domesticBankAccount.setAvailableBalance(999999999L);
                    accountRepository.saveAndFlush(domesticBankAccount);
                    i++;
                    continue;
                }
                ;
                ForeignCurrencyAccount foreignCurrencyAccount = new ForeignCurrencyAccount();
                foreignCurrencyAccount.setAccountNumber("000000000000000" + i);
                foreignCurrencyAccount.setEmail("bankAccount@bank.rs");
                foreignCurrencyAccount.setAccountType(AccountType.BANK_ACCOUNT);
                foreignCurrencyAccount.setEmployeeId(2L);
                foreignCurrencyAccount.setMaintenanceFee(0.00);
                foreignCurrencyAccount.setDefaultCurrencyCode(currency);
                foreignCurrencyAccount.setLinkState(UserAccountUserProfileLinkState.ASSOCIATED);
                foreignCurrencyAccount.setInterestRate(0.0);
                foreignCurrencyAccount.setCurrencyCode(currency);
                foreignCurrencyAccount.setAvailableBalance(999999999L);
                accountRepository.saveAndFlush(foreignCurrencyAccount);
                i++;
            }

        }
        if (creditRepository.count() == 0) {
            Credit c = new Credit();
            c.setAccountNumber("3334444111111111");
            c.setCreditNumber(1L);
            c.setCreditAmount(36000.0);
            c.setCreditCreationDate(new Date().getTime());
            c.setCreditExpirationDate(new Date().getTime() + 1000L * 60 * 60 * 24 * 365);
            c.setCreditName(String.valueOf(CreditType.STAMBENI));
            c.setInstallmentAmount(100.0);
            c.setCurrencyCode("EUR");
            c.setEffectiveInterestRate(3.5);
            c.setNominalInterestRate(3.0);
            c.setNextInstallmentDate(new Date().getTime() + 1000L * 60 * 60 * 24 * 30);
            c.setPaymentPeriodMonths(12L);
            c.setRemainingAmount(36000.0);
            creditRepository.save(c);
        }
        if (creditRequestRepository.count() == 0) {
            String[] purposes = {"STAMBENI", "AUTO", "POTROŠAČKI", "REFINANSIRANJE", "EDUKACIJA"};
            String[] notes = {"pls daj kredit", "kupujem auto", "kupujem televizor", "refinansiram dugove", "studiram"};
            String[] accNumbs = {"3334444999999999", "3334444111111111", "3334444888888888"};
            String[] educations = {"OSNOVNA", "SREDNJA", "VIŠA", "TRECI", "MASTER", "DOKTOR"};
            String[] maritalStatuses = {"NEOZENJEN", "OŽENJEN", "RAZVEDEN", "UDOVAC"};
            for (int i = 0; i < 10; i++) {
                CreditRequest crd = new CreditRequest();
                crd.setAccountNumber(accNumbs[new Random().nextInt(3)]);
                crd.setCreditAmount(new Random().nextDouble(1000L, 10000L));
                crd.setCreditPurpose(purposes[new Random().nextInt(5)]);
                if (crd.getAccountNumber().equals("3334444999999999") || crd.getAccountNumber().equals("3334444111111111"))
                    crd.setCurrency("RSD");
                else
                    crd.setCurrency("EUR");
                crd.setEducationLevel(educations[new Random().nextInt(6)]);
                crd.setEmploymentPeriod(5L);
                crd.setHousingStatus("IZNAJMLJEN");
                crd.setMaritalStatus(maritalStatuses[new Random().nextInt(4)]);
                crd.setMobileNumber("+381655555555");
                crd.setMonthlySalary(1000L);
                crd.setPermanentEmployment(true);
                crd.setOwnCar(false);
                crd.setBranch("Novi Sad");
                crd.setMaturity(12L);
                crd.setCreditType(CreditType.STAMBENI);
                crd.setStatus(CreditRequestStatus.PENDING);
                crd.setNote(notes[new Random().nextInt(5)]);
                crd.setPaymentPeriodMonths(new Random().nextLong(12, 36));
                creditRequestRepository.save(crd);
            }

        }


        Optional<Card> cardTest = cardRepository.findByIdentificationCardNumber(1000000000000000L);

        if (cardTest.isEmpty()) {
            Card card = new Card();
            card.setAccountNumber("3334444999999999");
            card.setCvvCode("133");
            card.setIdentificationCardNumber(1000000000000000L);
            card.setCardType(CardType.CREDIT);
            card.setCreationDate(1712602820L);
            card.setExpirationDate(1775682066000L);
            card.setLimitCard(1111000L);
            card.setStatus(true);
            card.setNameOfCard("TEST");
            cardRepository.save(card);
        } else {

            Card card = cardTest.get();
            card.setAccountNumber("3334444999999999");
            card.setCvvCode("133");
            card.setIdentificationCardNumber(1000000000000000L);
            card.setCardType(CardType.CREDIT);
            card.setCreationDate(1712602820L);
            card.setExpirationDate(1775682066000L);
            card.setLimitCard(1111000L);
            card.setStatus(true);
            card.setNameOfCard("TEST");

            cardRepository.save(card);

        }

        logger.info("BankService: DATA LOADING IN PROGRESS...");
        if (exchangeRateRepository.count() != 0) {
            if (exchangeRateRepository.findAll().get(0).getTimeNextUpdate() < System.currentTimeMillis()) {
                exchangeRateRepository.deleteAll();
            }
        }
        if (exchangeRateRepository.count() == 0) {
            List<ExchangeRateApiResponse> responseList = ExchangeRateBootstrapUtil.getDataFromApi();
            for (ExchangeRateApiResponse response : responseList) {
                Map<String, Double> conversionRates = response.getConversion_rates();
                conversionRates.forEach((k, v) -> {
                    if (k.equals(response.getBase_code())) return;
                    if (response.getBase_code().equals("EUR")) {
                        v = v * 0.98; //satro provizija hehe
                    } else {
                        v = v * 1.02;
                    }

                    exchangeRateRepository.save(new ExchangeRates(
                            null,
                            response.getTime_last_update_unix(),
                            response.getTime_next_update_unix(),
                            response.getBase_code(),
                            k,
                            v));

                });
            }
        }

        logger.info("BankService: DATA LOADING FINISHED...");
    }
}
