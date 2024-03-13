package rs.edu.raf.IAMService.data.enums;

import lombok.Getter;

@Getter
public enum RoleType {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_EMPLOYEE("ROLE_EMPLOYEE"),
    ROLE_SUPERVISOR("ROLE_SUPERVISOR"),
    ROLE_AGENT("ROLE_AGENT"),
    ROLE_USER("ROLE_USER");

    private final String role;

    RoleType(String role) {
        this.role = role;
    }

}
