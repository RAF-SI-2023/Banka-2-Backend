package rs.edu.raf.BankService.exception;

public class AccountNumberAlreadyExistException extends RuntimeException {

    public AccountNumberAlreadyExistException(String accountNumber) {
        super("Account with account number " + accountNumber + " already exist");
    }
}
