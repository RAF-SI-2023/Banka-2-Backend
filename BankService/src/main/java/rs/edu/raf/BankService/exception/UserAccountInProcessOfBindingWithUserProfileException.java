package rs.edu.raf.BankService.exception;

public class UserAccountInProcessOfBindingWithUserProfileException extends RuntimeException {

    public UserAccountInProcessOfBindingWithUserProfileException(String accountNumber) {
        super("User account user profile link with account number " + accountNumber + " is in process");
    }
}
