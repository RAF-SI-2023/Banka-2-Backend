package rs.edu.raf.BankService.exception;

public class ActivationCodeDoesNotMatchException extends RuntimeException {

        public ActivationCodeDoesNotMatchException() {
            super("Activation code does not match");
        }
}
