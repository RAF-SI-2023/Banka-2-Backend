package rs.edu.raf.BankService.exception;

public class UserAccountAlreadyAssociatedWithUserProfileException extends RuntimeException {

        public UserAccountAlreadyAssociatedWithUserProfileException(String accountNumber){
            super("Account " + accountNumber + " already associated with the profile. Try to login  ");
        }
}
