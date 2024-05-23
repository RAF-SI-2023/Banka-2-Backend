package rs.edu.raf.IAMService.exceptions;

public class EmailNotFoundException extends RuntimeException {
    public EmailNotFoundException(String email) {
        super(String.format("User with email '%s' not found", email));
    }
}
