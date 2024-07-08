package rs.edu.raf.BankService.integration.transactionservice;

import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.data.enums.AccountType;
import rs.edu.raf.BankService.data.enums.UserAccountUserProfileLinkState;
import rs.edu.raf.BankService.repository.CashAccountRepository;
import rs.edu.raf.BankService.service.TransactionService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.Assert.*;

public class TransferFundsIntegrationTest extends TransactionServiceIntegrationTestConfig {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CashAccountRepository cashAccountRepository;

    private CashAccount testSenderCashAccount;
    private CashAccount testReceiverCashAccount;

    private Boolean transferCompleted = false;

    @Transactional
    @After
    public void finish() {
        if (testSenderCashAccount != null && testSenderCashAccount.getId() != null) {
            cashAccountRepository.delete(testSenderCashAccount);
        }
        if (testReceiverCashAccount != null && testReceiverCashAccount.getId() != null) {
            cashAccountRepository.delete(testReceiverCashAccount);
        }
    }

    /*
        Scenario: Successfully transfer funds
        Given a sender account with number "1122334455667788" with a balance of 15000 "RSD", 5000 of that reserved for transfer
        And a receiver account with number "9988776655443322" with a balance of 10000 "RSD"
        When I request a transfer of 5000 from "1122334455667788" to "9988776655443322"
        Then the transfer should be successful
        And the sender's new balance should be 10000 after transfer
        And the receiver's new balance should be 15000 after transfer
    */

    @Given("a sender account with number {string} with a balance of {long} {string}, {long} of that reserved for transfer")
    public void aSenderAccountWithNumberWithABalanceOfOfThatReservedForTransfer(String senderAccountNumber, long senderBalance,
                                                                                String currency, long reservedAmount) {
        testSenderCashAccount = createTestAccount(senderAccountNumber, "sender@example.com", currency, senderBalance);
        testSenderCashAccount.setReservedFunds(reservedAmount);
        cashAccountRepository.save(testSenderCashAccount);
    }

    @And("a receiver account with number {string} with a balance of {long} {string}")
    public void aReceiverAccountWithNumberWithABalanceOf(String receiverAccountNumber, long receiverBalance, String currency) {
        testReceiverCashAccount = createTestAccount(receiverAccountNumber, "receiver@example.com", currency, receiverBalance);
        cashAccountRepository.save(testReceiverCashAccount);
    }

    @When("I request a transfer of {long} from {string} to {string}")
    public void iRequestATransferOfFromTo(long transferAmount, String senderAccountNumber, String receiverAccountNumber) {
        transferCompleted = transactionService.transferFunds(senderAccountNumber, receiverAccountNumber, transferAmount);
    }

    @Then("the transfer should be successful")
    public void theTransferShouldBeSuccessful() {
        assert (transferCompleted);
    }

    @And("the sender's new balance should be {long} after transfer")
    public void theSenderSNewBalanceShouldBeAfterTransfer(long expectedSenderBalance) {
        CashAccount senderCashAccount = cashAccountRepository.findByAccountNumber(testSenderCashAccount.getAccountNumber());
        if (senderCashAccount == null) {
            fail("Sender account not found");
        }
        assertEquals(expectedSenderBalance, senderCashAccount.getAvailableBalance());
    }

    @And("the receiver's new balance should be {long} after transfer")
    public void theReceiverSNewBalanceShouldBeAfterTransfer(long expectedReceiverBalance) {
        CashAccount receiverCashAccount = cashAccountRepository.findByAccountNumber(testReceiverCashAccount.getAccountNumber());
        if (receiverCashAccount == null) {
            fail("Receiver account not found");
        }
        assertEquals(expectedReceiverBalance, receiverCashAccount.getAvailableBalance());
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
