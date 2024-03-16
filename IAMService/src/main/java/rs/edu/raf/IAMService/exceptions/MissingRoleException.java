package rs.edu.raf.IAMService.exceptions;

import rs.edu.raf.IAMService.data.enums.RoleType;

public class MissingRoleException extends RuntimeException {

    public MissingRoleException(String role) {
        super(String.format("Role of type '%s' not found!", role));
    }
}
