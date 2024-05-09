package rs.edu.raf.IAMService.data.enums;

import lombok.Getter;

@Getter
public enum RoleType {
    ADMIN("ADMIN"),
    EMPLOYEE("EMPLOYEE"),
    SUPERVISOR("SUPERVISOR"),
    AGENT("AGENT"),
    USER("USER");


    private final String role;

    RoleType(String role) {
        this.role = role;
    }

}
