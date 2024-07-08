package rs.edu.raf.BankService.springSecurityUtil;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import rs.edu.raf.BankService.filters.principal.CustomUserPrincipal;

public class SpringSecurityUtil {

    public static boolean hasRole(String role) {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(role));
    }

    static public String getPrincipalEmail() {
        return ((CustomUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail();
    }

    public static Long getPrincipalId() {
        return ((CustomUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
    }

    public static boolean isAgent() {
        return hasRole("ROLE_AGENT");
    }

    public static boolean isUser() {
        return hasRole("ROLE_USER");
    }

    public static boolean isSupervisor() {
        return hasRole("ROLE_SUPERVISOR");
    }

    public static boolean isAdmin() {
        return hasRole("ROLE_ADMIN");
    }

    static public boolean hasPermission(String permission) {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(permission));
    }

    public static String getJwtToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String tokenHeader = request.getHeader("Authorization");
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            return tokenHeader.substring(7);
        }
        return null;
    }

    public static String getAuthorizationHeader() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getHeader("Authorization");
    }

    public static String getUserRole() {
        if (isAdmin()) {
            return "ROLE_ADMIN";
        }
        if (isAgent()) {
            return "ROLE_AGENT";
        }
        if (isSupervisor()) {
            return "ROLE_SUPERVISOR";
        }
        return "ROLE_USER";
    }


}
