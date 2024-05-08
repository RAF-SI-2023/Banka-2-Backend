package rs.edu.raf.BankService.bootstrap;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.data.entities.accounts.DomesticCurrencyCashAccount;
import rs.edu.raf.BankService.data.entities.accounts.ForeignCurrencyCashAccount;
import rs.edu.raf.BankService.data.entities.card.Card;
import rs.edu.raf.BankService.data.enums.*;
import rs.edu.raf.BankService.repository.CashAccountRepository;
import rs.edu.raf.BankService.repository.CardRepository;
import rs.edu.raf.BankService.repository.ExchangeRateRepository;
import rs.edu.raf.BankService.repository.credit.CreditRepository;
import rs.edu.raf.BankService.repository.credit.CreditRequestRepository;

/**
 *     Ova klasa ce da se runnuje prilikom pokretanja maven test komande
 *     ----------------------------------------------------------------
 *     PODACI ISPOD SU TESTNI PODACI
 *     PODACI ISPOD SU TESTNI PODACI
 *     PODACI ISPOD SU TESTNI PODACI
 *     PODACI ISPOD SU TESTNI PODACI
 *     ----------------------------------------------------------------
 *     NIJE DOZVOLJENO MENJANJE POSTOJEĆIH PODATAKA !!!!!!!!!!!!!!!
 *     NIJE DOZVOLJENO MENJANJE POSTOJEĆIH PODATAKA !!!!!!!!!!!!!!!
 *     NIJE DOZVOLJENO MENJANJE POSTOJEĆIH PODATAKA !!!!!!!!!!!!!!!
 *     NIJE DOZVOLJENO MENJANJE POSTOJEĆIH PODATAKA !!!!!!!!!!!!!!!
 *     ----------------------------------------------------------------
 *     DOZVOLJENO JE DODAVANJE NOVIH PODATAKA !!!!!!!!!!!!!!!
 *     DOZVOLJENO JE DODAVANJE NOVIH PODATAKA !!!!!!!!!!!!!!!
 *     DOZVOLJENO JE DODAVANJE NOVIH PODATAKA !!!!!!!!!!!!!!!
 *     DOZVOLJENO JE DODAVANJE NOVIH PODATAKA !!!!!!!!!!!!!!!
 */

@Component
@RequiredArgsConstructor
@Profile("test")
public class BootstrapTestData implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(BootstrapTestData.class);
    private final CashAccountRepository cashAccountRepository;
    private final CreditRepository creditRepository;
    private final CreditRequestRepository creditRequestRepository;
    private final CardRepository cardRepository;
    private final ExchangeRateRepository exchangeRateRepository;

    private static Boolean alreadySetup = false;
    private static final Object lock = new Object();

    @Override
    public void run(String... args) throws Exception {

        synchronized (lock){
            if (alreadySetup) {
                return;
            }
            alreadySetup = true;
        }


        logger.info("BankService: TEST DATA LOADING IN PROGRESS...");


        DomesticCurrencyCashAccount testDomesticCashAccount1 = new DomesticCurrencyCashAccount();
        testDomesticCashAccount1.setAccountNumber("0004444999999999");
        testDomesticCashAccount1.setEmail("owndByUser1@gmail.com");
        testDomesticCashAccount1.setAccountType(AccountType.DOMESTIC_CURRENCY_ACCOUNT);
        testDomesticCashAccount1.setEmployeeId(2L);
        testDomesticCashAccount1.setMaintenanceFee(220.00);
        testDomesticCashAccount1.setCurrencyCode("RSD");
        testDomesticCashAccount1.setAvailableBalance(100000L);
        testDomesticCashAccount1.setDomesticCurrencyAccountType(DomesticCurrencyAccountType.RETIREMENT);
        testDomesticCashAccount1.setInterestRate(2.5);

        DomesticCurrencyCashAccount testDomesticCashAccount2 = new DomesticCurrencyCashAccount();
        testDomesticCashAccount2.setAccountNumber("0932345666666666");
        testDomesticCashAccount2.setEmail("owndByUser2@gmail.com");
        testDomesticCashAccount2.setAccountType(AccountType.DOMESTIC_CURRENCY_ACCOUNT);
        testDomesticCashAccount2.setEmployeeId(2L);
        testDomesticCashAccount2.setMaintenanceFee(220.00);
        testDomesticCashAccount2.setCurrencyCode("RSD");
        testDomesticCashAccount2.setAvailableBalance(100000L);
        testDomesticCashAccount2.setDomesticCurrencyAccountType(DomesticCurrencyAccountType.RETIREMENT);
        testDomesticCashAccount2.setInterestRate(2.5);

        DomesticCurrencyCashAccount testDomesticCashAccount3 = new DomesticCurrencyCashAccount();
        testDomesticCashAccount3.setAccountNumber("0932345111111111");
        testDomesticCashAccount3.setEmail("owndByUser3@gmail.com");
        testDomesticCashAccount3.setAccountType(AccountType.DOMESTIC_CURRENCY_ACCOUNT);
        testDomesticCashAccount3.setEmployeeId(2L);
        testDomesticCashAccount3.setMaintenanceFee(220.00);
        testDomesticCashAccount3.setCurrencyCode("RSD");
        testDomesticCashAccount3.setAvailableBalance(100000L);
        testDomesticCashAccount3.setDomesticCurrencyAccountType(DomesticCurrencyAccountType.RETIREMENT);
        testDomesticCashAccount3.setInterestRate(2.5);

        ForeignCurrencyCashAccount testForeignCurrencyCashAccount = new ForeignCurrencyCashAccount();
        testForeignCurrencyCashAccount.setAccountNumber("7772345666556666");
        testForeignCurrencyCashAccount.setEmail("owndByUser2@gmail.com");
        testForeignCurrencyCashAccount.setAccountType(AccountType.FOREIGN_CURRENCY_ACCOUNT);
        testForeignCurrencyCashAccount.setEmployeeId(2L);
        testForeignCurrencyCashAccount.setMaintenanceFee(220.00);
        testForeignCurrencyCashAccount.setCurrencyCode("USD");
        testForeignCurrencyCashAccount.setAvailableBalance(10000L);

        addAccountIfCashAccountNumberIsNotPresent(testDomesticCashAccount1);
        addAccountIfCashAccountNumberIsNotPresent(testDomesticCashAccount2);
        addAccountIfCashAccountNumberIsNotPresent(testDomesticCashAccount3);
        addAccountIfCashAccountNumberIsNotPresent(testForeignCurrencyCashAccount);

        Card card1 = new Card();
        card1.setAccountNumber("0004444999999999");
        card1.setCvvCode("133");
        card1.setIdentificationCardNumber(1000000000000000L);
        card1.setCardType(CardType.CREDIT);
        card1.setCreationDate(1712602820L);
        card1.setExpirationDate(1775682066000L);
        card1.setLimitCard(1111000L);
        card1.setStatus(true);
        card1.setNameOfCard("TEST");

        addCardIfIdentificationCardNumberIsNotPresent(card1);

        logger.info("BankService: TEST DATA LOADING FINISHED...");

    }

    private void addCardIfIdentificationCardNumberIsNotPresent(Card card){
        if(cardRepository.findByIdentificationCardNumber(card.getIdentificationCardNumber()).isEmpty()){
            cardRepository.save(card);
        }
    }

    private void addAccountIfCashAccountNumberIsNotPresent(CashAccount account){
        if(cashAccountRepository.findByAccountNumber(account.getAccountNumber()) == null){
            if(account instanceof DomesticCurrencyCashAccount){
                cashAccountRepository.save((DomesticCurrencyCashAccount)account);
            }
            else if (account instanceof ForeignCurrencyCashAccount){
                cashAccountRepository.save((ForeignCurrencyCashAccount)account);
            }
            else {
                cashAccountRepository.save(account);
            }
        }
    }
}
