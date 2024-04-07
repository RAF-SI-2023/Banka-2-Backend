package rs.edu.raf.OTCService.filters.principal;

import lombok.Getter;

import java.security.Principal;

@Getter
public class CustomUserPrincipal implements Principal {
    // Možete dodati dodatne metode da vratite userId i permissions
    private Long userId;

    public CustomUserPrincipal(Long userId) {
        this.userId = userId;
    }

    // Standard getters and setters

    @Override
    public String getName() {
        return null;
    }
}
