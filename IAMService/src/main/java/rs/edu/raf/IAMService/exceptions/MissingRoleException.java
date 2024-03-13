package rs.edu.raf.IAMService.exceptions;

import rs.edu.raf.IAMService.data.enums.RoleType;

public class MissingRoleException extends RuntimeException {

    public MissingRoleException(RoleType roleType) {
        super(String.format("Role of type '%s' not found!", roleType.toString()));
    }
}
