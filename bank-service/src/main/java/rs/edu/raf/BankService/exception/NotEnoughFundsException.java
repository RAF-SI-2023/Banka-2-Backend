package rs.edu.raf.BankService.exception;

public class NotEnoughFundsException extends RuntimeException {

    public NotEnoughFundsException() {
        super("Not enough funds in account");
    }
}