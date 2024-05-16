package rs.edu.raf.OTCService.filters.principal;

import lombok.Getter;

import java.security.Principal;

@Getter
public class CustomUserPrincipal implements Principal {
    // Možete dodati dodatne metode da vratite userId i permissions
    private Long userId;
    private String email;

    public CustomUserPrincipal(Long userId, String email) {
        this.userId = userId;
        this.email = email;
    }

    // Standard getters and setters

    @Override
    public String getName() {
        return null;
    }
}
