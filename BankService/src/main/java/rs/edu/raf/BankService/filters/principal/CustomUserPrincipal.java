package rs.edu.raf.BankService.filters.principal;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserPrincipal implements Principal {
    private Long userId;
    private String email;

    public CustomUserPrincipal(Long userId, String email) {
        this.userId = userId;
        this.email = email;
    }

    // Standard getters and setters

    // Možete dodati dodatne metode da vratite userId i permissions
    public Long getUserId() {
        return userId;
    }

    @Override
    public String getName() {
        return null;
    }
}
