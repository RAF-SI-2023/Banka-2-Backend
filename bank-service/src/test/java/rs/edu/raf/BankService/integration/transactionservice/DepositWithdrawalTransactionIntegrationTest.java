package rs.edu.raf.BankService.integration.transactionservice;

import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import rs.edu.raf.BankService.data.dto.GenericTransactionDto;
import rs.edu.raf.BankService.data.dto.InternalTransferTransactionDto;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.data.enums.AccountType;
import rs.edu.raf.BankService.data.enums.UserAccountUserProfileLinkState;
import rs.edu.raf.BankService.repository.CashAccountRepository;
import rs.edu.raf.BankService.service.TransactionService;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DepositWithdrawalTransactionIntegrationTest extends TransactionServiceIntegrationTestConfig {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CashAccountRepository cashAccountRepository;

    private CashAccount testCashAccount;
    private InternalTransferTransactionDto depositResult;

    @Transactional
    @After
    public void finish() {
        if (testCashAccount != null && testCashAccount.getId() != null) {
            cashAccountRepository.delete(testCashAccount);
        }
    }

    /*
        Scenario: Deposit funds to account
        Given an account with number "1122334455667788" with a balance of 15000 "RSD"
        When deposits 5000 "RSD" to the account
        Then the account's new balance should be 20000 after transfer
     */

    @Given("an account with number {string} with a balance of {long} {string}")
    public void anAccountWithNumberWithABalanceOf(String accountNumber, long accountBalance, String currency) {
        testCashAccount = createTestAccount(accountNumber, "test@example.com", currency, accountBalance);
        cashAccountRepository.save(testCashAccount);
    }

    @When("deposits {long} {string} to the account")
    public void depositsToTheAccount(long depositAmount, String currency) {
        InternalTransferTransactionDto depositTransaction = new InternalTransferTransactionDto();
        depositTransaction.setAmount(depositAmount);
        depositTransaction.setSenderAccountNumber(testCashAccount.getAccountNumber());
        depositResult = transactionService.depositWithdrawalTransaction(depositTransaction);
    }

    @Then("the account's new balance should be {long} after transfer")
    public void theAccountSNewBalanceShouldBeAfterTransfer(long newBalance) {
        CashAccount cashAccount = cashAccountRepository.findByAccountNumber(testCashAccount.getAccountNumber());
        if (cashAccount == null) {
            fail("Receiver account not found");
        }
        assertEquals(newBalance, cashAccount.getAvailableBalance());
    }

    @When("withdraws {long} {string} from the account")
    public void withdrawsFromTheAccount(long withdrawAmount, String currency) {
        InternalTransferTransactionDto withdrawTransaction = new InternalTransferTransactionDto();
        withdrawTransaction.setAmount(withdrawAmount);
        withdrawTransaction.setSenderAccountNumber(testCashAccount.getAccountNumber());
        depositResult = transactionService.depositWithdrawalTransaction(withdrawTransaction);
    }

    private CashAccount createTestAccount(String accountNumber,
                                          String email,
                                          String currencyCode,
                                          Long balance) {
        CashAccount cashAccount = new CashAccount();
        cashAccount.setAccountNumber(accountNumber);
        cashAccount.setEmail(email);
        cashAccount.setAvailableBalance(balance);
        cashAccount.setAccountType(AccountType.DOMESTIC_CURRENCY_ACCOUNT);
        cashAccount.setCurrencyCode(currencyCode);
        cashAccount.setMaintenanceFee(0.0);
        cashAccount.setAvailableBalance(balance);
        cashAccount.setLinkState(UserAccountUserProfileLinkState.NOT_ASSOCIATED);
        cashAccount.setEmployeeId(1L);
        return cashAccount;
    }
}
