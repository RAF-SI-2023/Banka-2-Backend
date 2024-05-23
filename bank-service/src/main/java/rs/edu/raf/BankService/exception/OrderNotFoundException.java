package rs.edu.raf.BankService.exception;

public class OrderNotFoundException extends RuntimeException{

    public OrderNotFoundException() {
        super("Order not found");
    }
}
