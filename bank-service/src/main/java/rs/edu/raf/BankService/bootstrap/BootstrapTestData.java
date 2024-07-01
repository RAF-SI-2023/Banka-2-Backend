package rs.edu.raf.BankService.bootstrap;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import rs.edu.raf.BankService.bootstrap.exchangeRatesUtils.ExchangeRateApiResponse;
import rs.edu.raf.BankService.bootstrap.exchangeRatesUtils.ExchangeRateBootstrapUtil;
import rs.edu.raf.BankService.data.entities.MarginsAccount;
import rs.edu.raf.BankService.data.entities.MarginsTransaction;
import rs.edu.raf.BankService.data.entities.Order;
import rs.edu.raf.BankService.data.entities.SecuritiesOwnership;
import rs.edu.raf.BankService.data.entities.accounts.BusinessCashAccount;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.data.entities.accounts.DomesticCurrencyCashAccount;
import rs.edu.raf.BankService.data.entities.accounts.ForeignCurrencyCashAccount;
import rs.edu.raf.BankService.data.entities.card.Card;
import rs.edu.raf.BankService.data.entities.credit.Credit;
import rs.edu.raf.BankService.data.entities.credit.CreditRequest;
import rs.edu.raf.BankService.data.entities.exchangeCurrency.ExchangeRates;
import rs.edu.raf.BankService.data.entities.transactions.ExternalTransferTransaction;
import rs.edu.raf.BankService.data.entities.transactions.InternalTransferTransaction;
import rs.edu.raf.BankService.data.entities.transactions.SecuritiesTransaction;
import rs.edu.raf.BankService.data.enums.AccountType;
import rs.edu.raf.BankService.data.enums.CardType;
import rs.edu.raf.BankService.data.enums.CreditRequestStatus;
import rs.edu.raf.BankService.data.enums.CreditType;
import rs.edu.raf.BankService.data.enums.DomesticCurrencyAccountType;
import rs.edu.raf.BankService.data.enums.ListingType;
import rs.edu.raf.BankService.data.enums.OrderActionType;
import rs.edu.raf.BankService.data.enums.OrderStatus;
import rs.edu.raf.BankService.data.enums.TransactionDirection;
import rs.edu.raf.BankService.data.enums.TransactionStatus;
import rs.edu.raf.BankService.data.enums.UserAccountUserProfileLinkState;
import rs.edu.raf.BankService.repository.CardRepository;
import rs.edu.raf.BankService.repository.CashAccountRepository;
import rs.edu.raf.BankService.repository.CashTransactionRepository;
import rs.edu.raf.BankService.repository.ExchangeRateRepository;
import rs.edu.raf.BankService.repository.MarginsAccountRepository;
import rs.edu.raf.BankService.repository.MarginsTransactionRepository;
import rs.edu.raf.BankService.repository.OrderRepository;
import rs.edu.raf.BankService.repository.SecuritiesOwnershipRepository;
import rs.edu.raf.BankService.repository.credit.CreditRepository;
import rs.edu.raf.BankService.repository.credit.CreditRequestRepository;

/**
 * Ova klasa ce da se runnuje prilikom pokretanja maven test komande
 * ----------------------------------------------------------------
 * PODACI ISPOD SU TESTNI PODACI
 * PODACI ISPOD SU TESTNI PODACI
 * PODACI ISPOD SU TESTNI PODACI
 * PODACI ISPOD SU TESTNI PODACI
 * ----------------------------------------------------------------
 * NIJE DOZVOLJENO MENJANJE POSTOJEĆIH PODATAKA !!!!!!!!!!!!!!!
 * NIJE DOZVOLJENO MENJANJE POSTOJEĆIH PODATAKA !!!!!!!!!!!!!!!
 * NIJE DOZVOLJENO MENJANJE POSTOJEĆIH PODATAKA !!!!!!!!!!!!!!!
 * NIJE DOZVOLJENO MENJANJE POSTOJEĆIH PODATAKA !!!!!!!!!!!!!!!
 * ----------------------------------------------------------------
 * DOZVOLJENO JE DODAVANJE NOVIH PODATAKA !!!!!!!!!!!!!!!
 * DOZVOLJENO JE DODAVANJE NOVIH PODATAKA !!!!!!!!!!!!!!!
 * DOZVOLJENO JE DODAVANJE NOVIH PODATAKA !!!!!!!!!!!!!!!
 * DOZVOLJENO JE DODAVANJE NOVIH PODATAKA !!!!!!!!!!!!!!!
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
    private final ResourceLoader resourceLoader;
    private final SecuritiesOwnershipRepository securitiesOwnershipRepository;
    private final CashTransactionRepository cashTransactionRepository;
    private final OrderRepository orderRepository;
    private final MarginsAccountRepository marginAccountRepository;
    private final MarginsTransactionRepository marginsTransactionRepository;
    @Value("${MY_EMAIL_1:lukapavlovic032@gmail.com}")
    private String myEmail1;
    @Value("${MY_EMAIL_2:lpavlovic11521rn@raf.rs}")
    private String myEmail2;
    @Value("${MY_EMAIL_3:lukapa369@gmail.com}")
    private String myEmail3;
    private String tempPrimaryBankAccount;

    @Override
    public void run(String... args) throws Exception {
        try {
            logger.info("BankService: TEST DATA LOADING IN PROGRESS...");
            loadExchangeRates();
            loadBankOwnedCashAccounts();

            loadOtherCashAccounts();

            loadCredits();

            loadCreditRequests();

            loadExchangeRates();

            loadSecurityOwnerships();

            loadTransactions();

            loadOrders();

            loadMarginAccounts();

            loadMarginsTransactions();

            addCardIfIdentificationCardNumberIsNotPresent(new Card(7767588514263210L, CardType.DEBIT, "Visa",
                    "3330000000000000", "444", 11110L, true, false));

            loadBank3FakeAccount();

            logger.info("BankService: TEST DATA LOADING FINISHED...");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadMarginsTransactions() {
        if (marginsTransactionRepository.count() == 0) {
            MarginsTransaction mt1 = new MarginsTransaction();
            mt1.setMarginsAccount(marginAccountRepository.findAllByAccountNumber("3334444999999999").get(0));
            mt1.setUserId(1L);
            mt1.setCurrencyCode("RSD");
            mt1.setDescription("Kupovina akcija");
            mt1.setInvestmentAmount(1000.0);
            mt1.setLoanValue(0.0);
            mt1.setInterest(0.0);
            mt1.setOrderPrice(1000.0);
            mt1.setType(TransactionDirection.DEPOSIT);
            mt1.setCreatedAt(System.currentTimeMillis());
            marginsTransactionRepository.save(mt1);

            MarginsTransaction mt2 = new MarginsTransaction();
            mt2.setMarginsAccount(marginAccountRepository.findAllByAccountNumber("3334444999999999").get(0));
            mt2.setUserId(1L);
            mt2.setCurrencyCode("RSD");
            mt2.setDescription("Akcija 1");
            mt2.setInvestmentAmount(100.0);
            mt2.setLoanValue(0.0);
            mt2.setInterest(0.0);
            mt2.setOrderPrice(100.0);
            mt2.setType(TransactionDirection.WITHDRAW);
            mt2.setCreatedAt(System.currentTimeMillis());

            marginsTransactionRepository.save(mt2);

        }
    }

    private void loadMarginAccounts() {
        if (marginAccountRepository.count() == 0) {

            MarginsAccount marginsAccount1 = new MarginsAccount();
            marginsAccount1.setAccountNumber("3334444999999999");
            marginsAccount1.setEmail(myEmail1);
            marginsAccount1.setCurrencyCode("RSD");
            marginsAccount1.setListingType(ListingType.STOCK);
            marginsAccount1.setBalance(1000000.0);
            marginsAccount1.setLoanValue(0.0);
            marginsAccount1.setMaintenanceMargin(0.0);
            marginsAccount1.setMarginCall(false);
            marginsAccount1.setUserId(1L);
            marginAccountRepository.save(marginsAccount1);

            MarginsAccount marginsAccount2 = new MarginsAccount();
            marginsAccount2.setAccountNumber("3334444999999999");
            marginsAccount2.setEmail(myEmail1);
            marginsAccount2.setCurrencyCode("RSD");
            marginsAccount2.setListingType(ListingType.FOREX);
            marginsAccount2.setBalance(10000.0);
            marginsAccount2.setLoanValue(0.0);
            marginsAccount2.setMaintenanceMargin(0.0);
            marginsAccount2.setMarginCall(false);
            marginsAccount2.setUserId(1L);

            marginAccountRepository.save(marginsAccount2);

        }
    }

    private void loadOrders() {

        if (orderRepository.count() == 0) {
            Order order1 = new Order();

            order1.setListingId(2499L);
            order1.setListingSymbol("Z");
            order1.setOrderActionType(OrderActionType.BUY);
            order1.setListingType(ListingType.STOCK);
            order1.setQuantity(10);
            order1.setRealizedQuantity(0);
            order1.setOrderStatus(OrderStatus.APPROVED);
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime threeDaysFromNow = now.plusDays(3);
            long nowEpochMilli = now.toInstant(ZoneOffset.UTC).toEpochMilli();
            long threeDaysFromNowEpochMilli = threeDaysFromNow.toInstant(ZoneOffset.UTC).toEpochMilli();
            order1.setSettlementDate(threeDaysFromNowEpochMilli);
            order1.setDone(false);
            order1.setTimeOfLastModification(nowEpochMilli);
            order1.setLimitPrice(0.0);
            order1.setStopPrice(0.0);
            order1.setAllOrNone(false);
            order1.setMargin(false);
            order1.setOwnedByBank(false);
            order1.setInitiatedByUserId(1L);
            order1.setApprovedBySupervisorId(2L);
            order1.setUserId(1L);

            orderRepository.save(order1);

            Order order2 = new Order();

            order2.setListingId(2499L);
            order2.setListingSymbol("Z");
            order2.setOrderActionType(OrderActionType.BUY);
            order2.setListingType(ListingType.STOCK);
            order2.setQuantity(11);
            order2.setRealizedQuantity(0);
            order2.setOrderStatus(OrderStatus.WAITING_FOR_APPROVAL);
            LocalDateTime now1 = LocalDateTime.now();
            LocalDateTime threeDaysFromNow1 = now.plusDays(3);
            long nowEpochMilli1 = now1.toInstant(ZoneOffset.UTC).toEpochMilli();
            long threeDaysFromNowEpochMilli1 = threeDaysFromNow1.toInstant(ZoneOffset.UTC).toEpochMilli();
            order2.setSettlementDate(threeDaysFromNowEpochMilli1);
            order2.setDone(false);
            order2.setTimeOfLastModification(nowEpochMilli1);
            order2.setLimitPrice(0.0);
            order2.setStopPrice(0.0);
            order2.setAllOrNone(false);
            order2.setMargin(false);
            order2.setOwnedByBank(false);
            order2.setInitiatedByUserId(1L);
            order2.setApprovedBySupervisorId(2L);
            order2.setUserId(2L);

            orderRepository.save(order2);
        }

    }

    private void loadTransactions() {

        if (cashTransactionRepository.count() == 0) {

            CashAccount sender1 = cashAccountRepository.findAllByEmail(myEmail1).get(0);
            CashAccount sender2 = cashAccountRepository.findAllByEmail(myEmail2).get(0);
            CashAccount sender3 = cashAccountRepository.findAllByEmail(myEmail3).get(0);

            InternalTransferTransaction itt = new InternalTransferTransaction();
            itt.setAmount(1000L);
            itt.setSenderCashAccount(sender1);
            itt.setReceiverCashAccount(sender2);
            itt.setStatus(TransactionStatus.CONFIRMED);
            itt.setCreatedAt(LocalDateTime.now());
            cashTransactionRepository.save(itt);

            ExternalTransferTransaction ett = new ExternalTransferTransaction();
            ett.setAmount(500L);
            ett.setSenderCashAccount(sender2);
            ett.setReceiverCashAccount(sender3);
            ett.setStatus(TransactionStatus.CONFIRMED);
            ett.setCreatedAt(LocalDateTime.now());
            ett.setReferenceNumber("123456789");
            ett.setVerificationToken("123456");
            ett.setTransactionPurpose("Kupovina");
            cashTransactionRepository.save(ett);

            SecuritiesTransaction st = new SecuritiesTransaction();
            st.setAmount(612.0);
            st.setSenderCashAccount(sender3);
            st.setReceiverCashAccount(sender1);
            st.setStatus(TransactionStatus.CONFIRMED);
            st.setCreatedAt(LocalDateTime.now());
            st.setSecuritiesSymbol("AAPL");
            st.setQuantityToTransfer(10);
            cashTransactionRepository.save(st);

        }

    }

    private void loadBankOwnedCashAccounts() {
        // Create bank accounts for all allowed currencies
        int i = 0;
        for (String currency : ExchangeRateBootstrapUtil.allowedCurrencies) {
            if (currency.equals("RSD")) {
                // TODO OVDE STAVITI DA BUDU BUSINESS
                DomesticCurrencyCashAccount domesticBankAccount = new DomesticCurrencyCashAccount();
                domesticBankAccount.setAccountNumber("000000000000000" + i);
                tempPrimaryBankAccount = domesticBankAccount.getAccountNumber();
                domesticBankAccount.setEmail("bankAccount@bank.rs");
                domesticBankAccount.setAccountType(AccountType.BANK_ACCOUNT);
                domesticBankAccount.setEmployeeId(2L);
                domesticBankAccount.setMaintenanceFee(0.00);
                domesticBankAccount.setLinkState(UserAccountUserProfileLinkState.ASSOCIATED);
                domesticBankAccount.setInterestRate(0.0);
                domesticBankAccount.setCurrencyCode(currency);
                domesticBankAccount.setAvailableBalance(999999999L);
                domesticBankAccount.setDomesticCurrencyAccountType(DomesticCurrencyAccountType.PERSONAL);
                domesticBankAccount.setOwnedByBank(true);
                domesticBankAccount.setPrimaryTradingAccount(true);

                addAccountIfCashAccountNumberIsNotPresent(domesticBankAccount);
                i++;
                continue;
            }

            ForeignCurrencyCashAccount foreignCurrencyAccount = new ForeignCurrencyCashAccount();
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
            foreignCurrencyAccount.setOwnedByBank(true);

            addAccountIfCashAccountNumberIsNotPresent(foreignCurrencyAccount);
            i++;
        }
    }

    private void loadOtherCashAccounts() {
        DomesticCurrencyCashAccount domesticCurrencyAccount1 = new DomesticCurrencyCashAccount();
        domesticCurrencyAccount1.setAccountNumber("3334444999999999");
        domesticCurrencyAccount1.setEmail(myEmail1);
        domesticCurrencyAccount1.setAccountType(AccountType.DOMESTIC_CURRENCY_ACCOUNT);
        domesticCurrencyAccount1.setEmployeeId(2L);
        domesticCurrencyAccount1.setMaintenanceFee(220.00);
        domesticCurrencyAccount1.setCurrencyCode("RSD");
        domesticCurrencyAccount1.setAvailableBalance(1000000000L);
        domesticCurrencyAccount1.setDomesticCurrencyAccountType(DomesticCurrencyAccountType.RETIREMENT);
        domesticCurrencyAccount1.setInterestRate(2.5);
        domesticCurrencyAccount1.setPrimaryTradingAccount(true);
        addAccountIfCashAccountNumberIsNotPresent(domesticCurrencyAccount1);

        ForeignCurrencyCashAccount foreignCurrencyAccount1 = new ForeignCurrencyCashAccount();
        foreignCurrencyAccount1.setAccountNumber("3334444777777777");
        foreignCurrencyAccount1.setEmail(myEmail1);
        foreignCurrencyAccount1.setAccountType(AccountType.FOREIGN_CURRENCY_ACCOUNT);
        foreignCurrencyAccount1.setEmployeeId(2L);
        foreignCurrencyAccount1.setMaintenanceFee(220.00);
        foreignCurrencyAccount1.setCurrencyCode("USD");
        foreignCurrencyAccount1.setAvailableBalance(500L);
        addAccountIfCashAccountNumberIsNotPresent(foreignCurrencyAccount1);

        ForeignCurrencyCashAccount foreignCurrencyAccount2 = new ForeignCurrencyCashAccount();
        foreignCurrencyAccount2.setAccountNumber("3334444888888888");
        foreignCurrencyAccount2.setEmail(myEmail1);
        foreignCurrencyAccount2.setAccountType(AccountType.FOREIGN_CURRENCY_ACCOUNT);
        foreignCurrencyAccount2.setEmployeeId(2L);
        foreignCurrencyAccount2.setMaintenanceFee(22.00);
        foreignCurrencyAccount2.setCurrencyCode("EUR");
        foreignCurrencyAccount2.setAvailableBalance(450L);
        addAccountIfCashAccountNumberIsNotPresent(foreignCurrencyAccount2);

        ForeignCurrencyCashAccount fac4 = new ForeignCurrencyCashAccount();
        fac4.setAccountNumber("3333444401010101");
        fac4.setEmail(myEmail1);
        fac4.setAccountType(AccountType.FOREIGN_CURRENCY_ACCOUNT);
        fac4.setEmployeeId(2L);
        fac4.setMaintenanceFee(22.00);
        fac4.setCurrencyCode("EUR");
        fac4.setAvailableBalance(450L);
        addAccountIfCashAccountNumberIsNotPresent(fac4);

        DomesticCurrencyCashAccount domesticCurrencyAccount2 = new DomesticCurrencyCashAccount();
        domesticCurrencyAccount2.setAccountNumber("3334444111111111");
        domesticCurrencyAccount2.setEmail(myEmail2);
        domesticCurrencyAccount2.setAccountType(AccountType.DOMESTIC_CURRENCY_ACCOUNT);
        domesticCurrencyAccount2.setEmployeeId(2L);
        domesticCurrencyAccount2.setMaintenanceFee(220.00);
        domesticCurrencyAccount2.setAvailableBalance(100000L);
        domesticCurrencyAccount2.setCurrencyCode("RSD");
        domesticCurrencyAccount2.setDomesticCurrencyAccountType(DomesticCurrencyAccountType.RETIREMENT);
        domesticCurrencyAccount2.setInterestRate(2.5);
        domesticCurrencyAccount2.setPrimaryTradingAccount(true);
        addAccountIfCashAccountNumberIsNotPresent(domesticCurrencyAccount2);

        ForeignCurrencyCashAccount foreignCurrencyAccount3 = new ForeignCurrencyCashAccount();
        foreignCurrencyAccount3.setAccountNumber("3330000000000000");
        foreignCurrencyAccount3.setEmail(myEmail2);
        foreignCurrencyAccount3.setAccountType(AccountType.FOREIGN_CURRENCY_ACCOUNT);
        foreignCurrencyAccount3.setEmployeeId(2L);
        foreignCurrencyAccount3.setMaintenanceFee(20.00);
        foreignCurrencyAccount3.setCurrencyCode("EUR");
        foreignCurrencyAccount3.setAvailableBalance(550L);
        addAccountIfCashAccountNumberIsNotPresent(foreignCurrencyAccount3);

        ForeignCurrencyCashAccount facmyEmail2 = new ForeignCurrencyCashAccount();
        facmyEmail2.setAccountNumber("1112222888888888");
        facmyEmail2.setEmail(myEmail2);
        facmyEmail2.setAccountType(AccountType.FOREIGN_CURRENCY_ACCOUNT);
        facmyEmail2.setEmployeeId(2L);
        facmyEmail2.setMaintenanceFee(20.00);
        facmyEmail2.setCurrencyCode("USD");
        facmyEmail2.setAvailableBalance(550L);
        addAccountIfCashAccountNumberIsNotPresent(facmyEmail2);

        BusinessCashAccount dca2 = new BusinessCashAccount();
        dca2.setAccountNumber("1112222333333333");
        dca2.setEmail("company_employee_1@company.com");
        dca2.setAccountType(AccountType.DOMESTIC_CURRENCY_ACCOUNT);
        dca2.setEmployeeId(2L);
        dca2.setMaintenanceFee(220.00);
        dca2.setCurrencyCode("RSD");
        dca2.setAvailableBalance(100000L);
        dca2.setPIB("123456789");
        dca2.setIdentificationNumber("123456789");
        dca2.setPrimaryTradingAccount(true);
        addAccountIfCashAccountNumberIsNotPresent(dca2);

        DomesticCurrencyCashAccount domesticCurrencyAccount3 = new DomesticCurrencyCashAccount();
        domesticCurrencyAccount3.setAccountNumber("1112222666666666");
        domesticCurrencyAccount3.setEmail(myEmail3);
        domesticCurrencyAccount3.setAccountType(AccountType.DOMESTIC_CURRENCY_ACCOUNT);
        domesticCurrencyAccount3.setEmployeeId(2L);
        domesticCurrencyAccount3.setMaintenanceFee(220.00);
        domesticCurrencyAccount3.setAvailableBalance(1000000L);
        domesticCurrencyAccount3.setCurrencyCode("RSD");
        domesticCurrencyAccount3.setDomesticCurrencyAccountType(DomesticCurrencyAccountType.PERSONAL);
        domesticCurrencyAccount3.setInterestRate(2.5);
        domesticCurrencyAccount3.setPrimaryTradingAccount(true);
        addAccountIfCashAccountNumberIsNotPresent(domesticCurrencyAccount3);

        ForeignCurrencyCashAccount foreignCurrencyAccount12 = new ForeignCurrencyCashAccount();
        foreignCurrencyAccount12.setAccountNumber("1112222444444444");
        foreignCurrencyAccount12.setEmail(myEmail3);
        foreignCurrencyAccount12.setAccountType(AccountType.FOREIGN_CURRENCY_ACCOUNT);
        foreignCurrencyAccount12.setEmployeeId(2L);
        foreignCurrencyAccount12.setMaintenanceFee(220.00);
        foreignCurrencyAccount12.setCurrencyCode("USD");
        foreignCurrencyAccount12.setAvailableBalance(500L);
        addAccountIfCashAccountNumberIsNotPresent(foreignCurrencyAccount12);

        ForeignCurrencyCashAccount foreignCurrencyAccount4 = new ForeignCurrencyCashAccount();
        foreignCurrencyAccount4.setAccountNumber("1112222555555555");
        foreignCurrencyAccount4.setEmail(myEmail3);
        foreignCurrencyAccount4.setAccountType(AccountType.FOREIGN_CURRENCY_ACCOUNT);
        foreignCurrencyAccount4.setEmployeeId(2L);
        foreignCurrencyAccount4.setMaintenanceFee(220.00);
        foreignCurrencyAccount4.setCurrencyCode("EUR");
        foreignCurrencyAccount4.setAvailableBalance(350L);
        addAccountIfCashAccountNumberIsNotPresent(foreignCurrencyAccount4);
    }

    private void loadCredits() {
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
    }

    private void loadCreditRequests() {
        if (creditRequestRepository.count() == 0) {
            String[] purposes = { "STAMBENI", "AUTO", "POTROŠAČKI", "REFINANSIRANJE", "EDUKACIJA" };
            String[] notes = { "pls daj kredit", "kupujem auto", "kupujem televizor", "refinansiram dugove",
                    "studiram" };
            String[] accNumbs = { "3334444999999999", "3334444111111111", "3334444888888888" };
            String[] educations = { "OSNOVNA", "SREDNJA", "VIŠA", "TRECI", "MASTER", "DOKTOR" };
            String[] maritalStatuses = { "NEOZENJEN", "OŽENJEN", "RAZVEDEN", "UDOVAC" };
            for (int i = 0; i < 10; i++) {
                CreditRequest crd = new CreditRequest();
                crd.setAccountNumber(accNumbs[new Random().nextInt(3)]);
                crd.setCreditAmount(new Random().nextDouble(1000L, 10000L));
                crd.setCreditPurpose(purposes[new Random().nextInt(5)]);
                if (crd.getAccountNumber().equals("3334444999999999")
                        || crd.getAccountNumber().equals("3334444111111111"))
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
    }

    private void loadExchangeRates() throws IOException {
        if (exchangeRateRepository.count() != 0) {
            if (exchangeRateRepository.findAll().get(0).getTimeNextUpdate() < System.currentTimeMillis()) {
                exchangeRateRepository.deleteAll();
            }
        }
        if (exchangeRateRepository.count() == 0) {
            ExchangeRateBootstrapUtil.setResource(resourceLoader, "dev");
            List<ExchangeRateApiResponse> responseList = ExchangeRateBootstrapUtil.getData();
            for (ExchangeRateApiResponse response : responseList) {
                Map<String, Double> conversionRates = response.getConversion_rates();
                conversionRates.forEach((k, v) -> {
                    if (k.equals(response.getBase_code()))
                        return;
                    if (response.getBase_code().equals("EUR")) {
                        v = v * 0.98; // satro provizija hehe
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
    }

    private void loadSecurityOwnerships() {
        if (securitiesOwnershipRepository.count() == 0) {

            String[] symbols1 = { "AAPL", "GOOGL", "Z", "NEXOY" };
            String[] symbols2 = { "NTFL", "TSLA", "MSFT", "FB" };
            String[] symbols3 = { "K", "TT", "CC", "I" };

            for (int i = 0; i < 4; i++) {
                SecuritiesOwnership so1 = new SecuritiesOwnership();
                so1.setEmail(myEmail1);
                so1.setAccountNumber("3334444999999999");
                so1.setOwnedByBank(false);
                so1.setListingType(ListingType.STOCK);
                so1.setSecuritiesSymbol(symbols1[i]);
                int quantity = new Random().nextInt(100);
                so1.setQuantity(quantity + 50);
                so1.setQuantityOfPubliclyAvailable(quantity);
                so1.setReservedQuantity(0);
                so1.setAverageBuyingPrice(so1.getQuantity() * new Random().nextDouble(100, 1600));// ne postoji bolji
                                                                                                  // nacin???
                securitiesOwnershipRepository.save(so1);

                SecuritiesOwnership so2 = new SecuritiesOwnership();
                so2.setEmail(myEmail2);
                so2.setAccountNumber("3334444111111111");
                so2.setOwnedByBank(false);
                so1.setListingType(ListingType.STOCK);
                so2.setSecuritiesSymbol(symbols2[i]);
                int quantity1 = new Random().nextInt(150);
                so2.setQuantity(30 + quantity1);
                so2.setQuantityOfPubliclyAvailable(quantity1);
                so2.setReservedQuantity(25);
                so2.setAverageBuyingPrice(so2.getQuantity() * new Random().nextDouble(100, 1600));// ne postoji bolji
                                                                                                  // nacin???
                securitiesOwnershipRepository.save(so2);

                SecuritiesOwnership so3 = new SecuritiesOwnership();
                so3.setEmail(myEmail3);
                so1.setListingType(ListingType.STOCK);
                so3.setAccountNumber("1112222666666666");
                so3.setOwnedByBank(false);
                so3.setSecuritiesSymbol(symbols3[i]);
                int quantity2 = new Random().nextInt(250);
                so3.setQuantity(quantity2 + 100);
                so3.setQuantityOfPubliclyAvailable(quantity2);
                so3.setReservedQuantity(5);
                so3.setAverageBuyingPrice(so3.getQuantity() * new Random().nextDouble(100, 1600));// ne postoji bolji
                                                                                                  // nacin???
                securitiesOwnershipRepository.save(so3);

                SecuritiesOwnership so4 = new SecuritiesOwnership();
                so4.setEmail("bankAccount@bank.rs");
                so4.setListingType(ListingType.STOCK);
                so4.setAccountNumber(tempPrimaryBankAccount != null ? tempPrimaryBankAccount : "0000000000000005");
                so4.setOwnedByBank(true);
                so4.setSecuritiesSymbol(symbols3[i]);
                so4.setQuantity(quantity2 + 100);
                so4.setQuantityOfPubliclyAvailable(50);
                so4.setAverageBuyingPrice(so4.getQuantity() * new Random().nextDouble(100, 1600));// ne postoji bolji
                                                                                                  // nacin???
                securitiesOwnershipRepository.save(so4);
            }

        }

    }

    private void loadBank3FakeAccount() {
        DomesticCurrencyCashAccount domesticBank3Account = new DomesticCurrencyCashAccount();
        domesticBank3Account.setAccountNumber("3333333333333333");
        domesticBank3Account.setEmail("bank3Account@bank.rs");
        domesticBank3Account.setAccountType(AccountType.DOMESTIC_CURRENCY_ACCOUNT);
        domesticBank3Account.setEmployeeId(2L);
        domesticBank3Account.setMaintenanceFee(0.00);
        domesticBank3Account.setInterestRate(0.0);
        domesticBank3Account.setCurrencyCode("RSD");
        domesticBank3Account.setAvailableBalance(0.00);
        domesticBank3Account.setDomesticCurrencyAccountType(DomesticCurrencyAccountType.PERSONAL);
        domesticBank3Account.setOwnedByBank(false);
        domesticBank3Account.setPrimaryTradingAccount(true);

        addAccountIfCashAccountNumberIsNotPresent(domesticBank3Account);
    }

    private void addCardIfIdentificationCardNumberIsNotPresent(Card card) {
        if (cardRepository.findByIdentificationCardNumber(card.getIdentificationCardNumber()).isEmpty()) {
            cardRepository.save(card);
        }
    }

    private void addAccountIfCashAccountNumberIsNotPresent(CashAccount account) {
        if (cashAccountRepository.findByAccountNumber(account.getAccountNumber()) == null) {
            if (account instanceof DomesticCurrencyCashAccount) {
                cashAccountRepository.save((DomesticCurrencyCashAccount) account);
            } else if (account instanceof ForeignCurrencyCashAccount) {
                cashAccountRepository.save((ForeignCurrencyCashAccount) account);
            } else {
                cashAccountRepository.save(account);
            }
        }
    }
}
