package rs.edu.raf.BankService.exception;

public class UserAccountLinkingWithUserProfileInProcessException extends RuntimeException {

    public UserAccountLinkingWithUserProfileInProcessException(String accountNumber) {
        super("User account user profile link with account number " + accountNumber + " is in process");
    }
}
