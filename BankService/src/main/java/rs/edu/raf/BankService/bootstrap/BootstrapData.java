package rs.edu.raf.BankService.bootstrap;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rs.edu.raf.BankService.data.entities.Account;
import rs.edu.raf.BankService.data.entities.DomesticCurrencyAccount;
import rs.edu.raf.BankService.data.entities.ForeignCurrencyAccount;
import rs.edu.raf.BankService.data.enums.AccountType;
import rs.edu.raf.BankService.data.enums.DomesticCurrencyAccountType;
import rs.edu.raf.BankService.repository.AccountRepository;

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

    @Override
    public void run(String... args) throws Exception {
        logger.info("BankService: DATA LOADING IN PROGRESS...");

        DomesticCurrencyAccount domesticCurrencyAccount1 = new DomesticCurrencyAccount();
        domesticCurrencyAccount1.setAccountNumber("3334444999999999");
        domesticCurrencyAccount1.setEmail(myEmail1);
        domesticCurrencyAccount1.setAccountType(AccountType.DOMESTIC_CURRENCY_ACCOUNT);
        domesticCurrencyAccount1.setEmployeeId(2L);
        domesticCurrencyAccount1.setMaintenanceFee(220.00);
        domesticCurrencyAccount1.setCurrencyCode("RSD");
        domesticCurrencyAccount1.setDomesticCurrencyAccountType(DomesticCurrencyAccountType.RETIREMENT);
        domesticCurrencyAccount1.setInterestRate(2.5);
        accountRepository.save((Account) domesticCurrencyAccount1);

        DomesticCurrencyAccount domesticCurrencyAccount2 = new DomesticCurrencyAccount();
        domesticCurrencyAccount2.setAccountNumber("3334444111111111");
        domesticCurrencyAccount2.setEmail(myEmail2);
        domesticCurrencyAccount2.setAccountType(AccountType.DOMESTIC_CURRENCY_ACCOUNT);
        domesticCurrencyAccount2.setEmployeeId(2L);
        domesticCurrencyAccount2.setMaintenanceFee(220.00);
        domesticCurrencyAccount2.setCurrencyCode("RSD");
        domesticCurrencyAccount2.setDomesticCurrencyAccountType(DomesticCurrencyAccountType.RETIREMENT);
        domesticCurrencyAccount2.setInterestRate(2.5);

        ForeignCurrencyAccount foreignCurrencyAccount1 = new ForeignCurrencyAccount();
        foreignCurrencyAccount1.setAccountNumber("3334444777777777");
        foreignCurrencyAccount1.setEmail(myEmail3);
        foreignCurrencyAccount1.setAccountType(AccountType.FOREIGN_CURRENCY_ACCOUNT);
        foreignCurrencyAccount1.setEmployeeId(2L);
        foreignCurrencyAccount1.setMaintenanceFee(220.00);
        foreignCurrencyAccount1.setCurrencyCode("USD");
        accountRepository.saveAndFlush(foreignCurrencyAccount1);


        logger.info("BankService: DATA LOADING IN PROGRESS...");
    }
}
