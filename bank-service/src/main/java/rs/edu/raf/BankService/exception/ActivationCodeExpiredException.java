package rs.edu.raf.BankService.exception;

public class ActivationCodeExpiredException extends RuntimeException {

        public ActivationCodeExpiredException() {
            super("Activation code expired");
        }
}
