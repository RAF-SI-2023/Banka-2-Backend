package rs.edu.raf.BankService.bootstrap;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import rs.edu.raf.BankService.bootstrap.exchangeRatesUtils.ExchangeRateApiResponse;
import rs.edu.raf.BankService.bootstrap.exchangeRatesUtils.ExchangeRateBootstrapUtil;
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
import rs.edu.raf.BankService.data.enums.*;
import rs.edu.raf.BankService.repository.*;
import rs.edu.raf.BankService.repository.credit.CreditRepository;
import rs.edu.raf.BankService.repository.credit.CreditRequestRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Profile("dev")
public class BootstrapDevData implements CommandLineRunner {

    @Value("${MY_EMAIL_1:lukapavlovic032@gmail.com}")
    private String myEmail1;

    @Value("${MY_EMAIL_2:lpavlovic11521rn@raf.rs}")
    private String myEmail2;

    @Value("${MY_EMAIL_3:lukapa369@gmail.com}")
    private String myEmail3;


    private static final Logger logger = LoggerFactory.getLogger(BootstrapDevData.class);
    private final CashAccountRepository cashAccountRepository;
    private final CreditRepository creditRepository;
    private final CreditRequestRepository creditRequestRepository;
    private final CardRepository cardRepository;
    private final ExchangeRateRepository exchangeRateRepository;
    private final ResourceLoader resourceLoader;
    private final SecuritiesOwnershipRepository securitiesOwnershipRepository;
    private final CashTransactionRepository cashTransactionRepository;
    private final OrderRepository orderRepository;

    @Override
    public void run(String... args) throws Exception {
        logger.info("BankService: DEV DATA LOADING IN PROGRESS...");
        loadExchangeRates();
        loadBankOwnedCashAccounts();

        loadOtherCashAccounts();

        loadCredits();

        loadCreditRequests();

        loadExchangeRates();

        loadSecurityOwnerships();

        loadTransactions();

        loadOrders();

        logger.info("BankService: DEV DATA LOADING FINISHED...");
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
            LocalDateTime threeDaysFromNow = now.plus(3, ChronoUnit.DAYS);
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
            LocalDateTime threeDaysFromNow1 = now.plus(3, ChronoUnit.DAYS);
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
            CashAccount sender3 = cashAccountRepository.findAllByEmail(myEmail2).get(0);

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
                //TODO OVDE STAVITI DA BUDU BUSINESS
                DomesticCurrencyCashAccount domesticBankAccount = new DomesticCurrencyCashAccount();
                domesticBankAccount.setAccountNumber("000000000000000" + i);
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
        dca2.setEmail("myEmail1@gmail.com");
        dca2.setAccountType(AccountType.DOMESTIC_CURRENCY_ACCOUNT);
        dca2.setEmployeeId(2L);
        dca2.setMaintenanceFee(220.00);
        dca2.setCurrencyCode("RSD");
        dca2.setAvailableBalance(100000L);
        dca2.setPIB("123456789");
        dca2.setIdentificationNumber("123456789");
        dca2.setPrimaryTradingAccount(true);
        addAccountIfCashAccountNumberIsNotPresent(dca2);

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
    }

    private void loadSecurityOwnerships() {
        if (securitiesOwnershipRepository.count() == 0) {

            String[] symbols1 = {"AAPL", "GOOGL", "Z", "AGXLW"};
            String[] symbols2 = {"NTFL", "TSLA", "MSFT", "FB"};
            String[] symbols3 = {"K", "TT", "CC", "I"};


            for (int i = 0; i < 4; i++) {
                SecuritiesOwnership so1 = new SecuritiesOwnership();
                so1.setEmail(myEmail1);
                so1.setAccountNumber("3334444999999999");
                so1.setOwnedByBank(false);
                so1.setSecuritiesSymbol(symbols1[i]);
                int quantity = new Random().nextInt(100);
                so1.setQuantity(quantity + 50);
                so1.setQuantityOfPubliclyAvailable(quantity);
                so1.setReservedQuantity(0);
                securitiesOwnershipRepository.save(so1);

                SecuritiesOwnership so2 = new SecuritiesOwnership();
                so2.setEmail(myEmail2);
                so2.setAccountNumber("3334444111111111");
                so2.setOwnedByBank(false);
                so2.setSecuritiesSymbol(symbols2[i]);
                int quantity1 = new Random().nextInt(150);
                so2.setQuantity(30 + quantity1);
                so2.setQuantityOfPubliclyAvailable(quantity1);
                so2.setReservedQuantity(25);
                securitiesOwnershipRepository.save(so2);


                SecuritiesOwnership so3 = new SecuritiesOwnership();
                so3.setEmail("myEmail1@gmail.com");
                so3.setAccountNumber("1112222333333333");
                so3.setOwnedByBank(false);
                so3.setSecuritiesSymbol(symbols3[i]);
                int quantity2 = new Random().nextInt(250);
                so3.setQuantity(quantity2 + 100);
                so3.setQuantityOfPubliclyAvailable(quantity2);
                so3.setReservedQuantity(5);
                securitiesOwnershipRepository.save(so3);

            }


        }

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
