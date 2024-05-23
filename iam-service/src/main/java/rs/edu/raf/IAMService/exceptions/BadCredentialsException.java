package rs.edu.raf.IAMService.exceptions;

public class BadCredentialsException extends Exception {

    public BadCredentialsException(String email) {
        super("User with email: " + email + " not found.");
    }
}
