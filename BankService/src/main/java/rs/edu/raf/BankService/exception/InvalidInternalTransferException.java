package rs.edu.raf.BankService.exception;

public class InvalidInternalTransferException extends RuntimeException {

    public InvalidInternalTransferException(String message) {
        super(message);
    }
}
