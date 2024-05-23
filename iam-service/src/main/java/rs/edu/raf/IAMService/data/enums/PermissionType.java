package rs.edu.raf.IAMService.data.enums;

import org.springframework.security.core.GrantedAuthority;

public enum PermissionType implements GrantedAuthority {

    PERMISSION_1("PERMISSION_1"),
    PERMISSION_2("PERMISSION_2"),
    PERMISSION_3("PERMISSION_3"),
    PERMISSION_4("PERMISSION_4");

    private final String permission;

    PermissionType(String permission) {
        this.permission = permission;
    }

    @Override
    public String getAuthority() {
        return permission;
    }
}
