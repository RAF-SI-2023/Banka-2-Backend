package rs.edu.raf.IAMService.exceptions;

public class MissingRoleException extends RuntimeException {

    public MissingRoleException(String role) {
        super(String.format("Role of type '%s' not found!", role));
    }
}
