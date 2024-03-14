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

    private static final Logger logger = LoggerFactory.getLogger(BootstrapData.class);
    private final AccountRepository accountRepository;

    @Override
    public void run(String... args) throws Exception {
        logger.info("BankService: DATA LOADING IN PROGRESS...");

        DomesticCurrencyAccount domesticCurrencyAccount1 = new DomesticCurrencyAccount();
        domesticCurrencyAccount1.setAccountNumber("123456789");
        domesticCurrencyAccount1.setEmail(myEmail1);
        domesticCurrencyAccount1.setAccountType(AccountType.DOMESTIC_CURRENCY_ACCOUNT);
        domesticCurrencyAccount1.setEmployeeId(2L);
        domesticCurrencyAccount1.setMaintenanceFee(220.00);
        domesticCurrencyAccount1.setCurrencyCode("RSD");
        domesticCurrencyAccount1.setDomesticCurrencyAccountType(DomesticCurrencyAccountType.RETIREMENT);
        domesticCurrencyAccount1.setInterestRate(2.5);
        accountRepository.save((Account) domesticCurrencyAccount1);

        ForeignCurrencyAccount foreignCurrencyAccount1 = new ForeignCurrencyAccount();
        foreignCurrencyAccount1.setAccountNumber("987654321");
        foreignCurrencyAccount1.setEmail(myEmail2);
        foreignCurrencyAccount1.setAccountType(AccountType.FOREIGN_CURRENCY_ACCOUNT);
        foreignCurrencyAccount1.setEmployeeId(2L);
        foreignCurrencyAccount1.setMaintenanceFee(220.00);
        foreignCurrencyAccount1.setCurrencyCode("USD");
        accountRepository.saveAndFlush(foreignCurrencyAccount1);


        logger.info("BankService: DATA LOADING IN PROGRESS...");
    }
}
