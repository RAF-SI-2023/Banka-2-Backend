package rs.edu.raf.BankService.integration.transactionservice;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import rs.edu.raf.BankService.data.dto.ExternalTransferTransactionDto;
import rs.edu.raf.BankService.data.entities.accounts.CashAccount;
import rs.edu.raf.BankService.data.enums.AccountType;
import rs.edu.raf.BankService.data.enums.TransactionStatus;
import rs.edu.raf.BankService.data.enums.UserAccountUserProfileLinkState;
import rs.edu.raf.BankService.repository.CashAccountRepository;
import rs.edu.raf.BankService.repository.CashTransactionRepository;
import rs.edu.raf.BankService.service.TransactionService;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateExternalTransactionIntegrationTest extends TransactionServiceIntegrationTestConfig {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CashAccountRepository cashAccountRepository;

    @Autowired
    private CashTransactionRepository cashTransactionRepository;

    private CashAccount testSenderCashAccount;
    private CashAccount testReceiverCashAccount;
    private TransactionStatus status;
    private Long transactionId;

    @Transactional
    @After
    public void finish() {
        if (transactionId != null) {
            cashTransactionRepository.deleteById(transactionId);
        }
        if (testSenderCashAccount != null && testSenderCashAccount.getId() != null) {
            cashAccountRepository.delete(testSenderCashAccount);
        }
        if (testReceiverCashAccount != null && testReceiverCashAccount.getId() != null) {
            cashAccountRepository.delete(testReceiverCashAccount);
        }
    }

    @Given("a sender account with number {string} with a balance of {long} for external transaction - happyFlow")
    public void an_account_with_number_with_a_balance_of(String accountNumber, Long balance) {
        testSenderCashAccount = createTestAccount(accountNumber,
                "sender@example.com",
                AccountType.DOMESTIC_CURRENCY_ACCOUNT,
                "USD",
                0.0 ,
                balance);

        cashAccountRepository.save(testSenderCashAccount);
    }

    @Given("a receiver account with number {string} with a balance of {long} for external transaction - happyFlow")
    public void a_receiver_account_with_number_with_a_balance_of(String accountNumber, Long balance) {
        testReceiverCashAccount = createTestAccount(accountNumber,
                "sender@example.com",
                AccountType.DOMESTIC_CURRENCY_ACCOUNT,
                "USD",
                0.0 ,
                balance);

        cashAccountRepository.save(testReceiverCashAccount);
    }

    @When("I request an external transfer of {long} from {string} to {string} - happyFlow")
    public void i_request_an_internal_transfer_of_from_to(Long amount, String senderAccountNumber, String receiverAccountNumber) {
        ExternalTransferTransactionDto transactionDto = new ExternalTransferTransactionDto();
        transactionDto.setAmount(amount);
        transactionDto.setSenderAccountNumber(senderAccountNumber);
        transactionDto.setReceiverAccountNumber(receiverAccountNumber);
        transactionDto.setTransactionPurpose("Asdf");
        transactionDto.setTransactionCode("Code");
        transactionDto.setReferenceNumber("1234");
        ExternalTransferTransactionDto result = transactionService.createExternalTransferTransaction(transactionDto);
        transactionId = Long.parseLong(result.getId());
        status = result.getStatus();
    }

    @Then("the external transfer should be pending")
    public void the_transfer_should_be_successful() {
        assertEquals(TransactionStatus.PENDING, status);
    }

    @Then("the sender's new balance should be {long} after external transaction - happyFlow")
    public void the_sender_s_new_balance_should_be(double expectedBalance) {
        CashAccount senderCashAccount = cashAccountRepository.findByAccountNumber(testSenderCashAccount.getAccountNumber());
        assertEquals(expectedBalance, senderCashAccount.getAvailableBalance());
    }

    @Then("the receiver's new balance should be {long} after external transaction - happyFlow")
    public void the_receiver_s_new_balance_should_be(double expectedBalance) {
        CashAccount receiverCashAccount = cashAccountRepository.findByAccountNumber(testReceiverCashAccount.getAccountNumber());
        assertEquals(expectedBalance, receiverCashAccount.getAvailableBalance());
    }

    @Given("a sender account with number {string} with a balance of {long} for external transaction - insufficientFunds")
    public void a_sender_account_with_number_with_a_balance_of_insufficientFunds(String accountNumber, Long balance) {
        testSenderCashAccount = createTestAccount(accountNumber, "sender@example.com", AccountType.DOMESTIC_CURRENCY_ACCOUNT, "USD", 0.0, balance);
        cashAccountRepository.save(testSenderCashAccount);
    }

    @Given("a receiver account with number {string} with a balance of {long} for external transaction - insufficientFunds")
    public void a_receiver_account_with_number_with_a_balance_of_insufficientFunds(String accountNumber, Long balance) {
        testReceiverCashAccount = createTestAccount(accountNumber, "receiver@example.com", AccountType.DOMESTIC_CURRENCY_ACCOUNT, "USD", 0.0, balance);
        cashAccountRepository.save(testReceiverCashAccount);
    }

    @When("I request an external transfer of {long} from {string} to {string} - insufficientFunds")
    public void i_request_an_internal_transfer_of_from_to_insufficientFunds(Long amount, String senderAccountNumber, String receiverAccountNumber) {
        ExternalTransferTransactionDto transactionDto = new ExternalTransferTransactionDto();
        transactionDto.setAmount(amount);
        transactionDto.setSenderAccountNumber(senderAccountNumber);
        transactionDto.setReceiverAccountNumber(receiverAccountNumber);
        transactionDto.setTransactionPurpose("Asdf");
        transactionDto.setTransactionCode("Code");
        transactionDto.setReferenceNumber("1234");
        ExternalTransferTransactionDto result = transactionService.createExternalTransferTransaction(transactionDto);
        transactionId = Long.valueOf(result.getId());
        status = result.getStatus();
    }

    @Then("the external transfer should be declined")
    public void the_transfer_should_be_declined() {
        assertEquals(TransactionStatus.DECLINED, status);
    }

    @Then("the sender's new balance should be {long} after external transaction - insufficientFunds")
    public void the_sender_s_new_balance_should_be_insufficientFunds(double expectedBalance) {
        CashAccount senderCashAccount = cashAccountRepository.findByAccountNumber(testSenderCashAccount.getAccountNumber());
        assertEquals(expectedBalance, senderCashAccount.getAvailableBalance());
    }

    @Then("the receiver's new balance should be {long} after external transaction - insufficientFunds")
    public void the_receiver_s_new_balance_should_be_insufficientFunds(double expectedBalance) {
        CashAccount receiverCashAccount = cashAccountRepository.findByAccountNumber(testReceiverCashAccount.getAccountNumber());
        assertEquals(expectedBalance, receiverCashAccount.getAvailableBalance());
    }

    private CashAccount createTestAccount(String accountNumber,
                                          String email,
                                          AccountType accountType,
                                          String currencyCode,
                                          Double maintenanceFee,
                                          Long balance) {
        CashAccount cashAccount = new CashAccount();
        cashAccount.setAccountNumber(accountNumber);
        cashAccount.setEmail(email);
        cashAccount.setAvailableBalance(balance);
        cashAccount.setAccountType(accountType);
        cashAccount.setCurrencyCode(currencyCode);
        cashAccount.setMaintenanceFee(maintenanceFee);
        cashAccount.setAvailableBalance(balance);
        cashAccount.setLinkState(UserAccountUserProfileLinkState.NOT_ASSOCIATED);
        cashAccount.setEmployeeId(1L);
        return cashAccount;
    }
}
