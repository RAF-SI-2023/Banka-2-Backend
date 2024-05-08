package rs.edu.raf.BankService.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import rs.edu.raf.BankService.filters.principal.CustomUserPrincipal;
import rs.edu.raf.BankService.jwtUtils.JwtUtil;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        String jwt = null;
        String role = null;
        String email = null;
        Long userId = null;
        List<String> permissions = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            role = jwtUtil.extractUserRole(jwt);
            email = jwtUtil.extractEmail(jwt);
            userId = jwtUtil.extractUserId(jwt);
            permissions = jwtUtil.extractPermissions(jwt);
        }
        if (permissions != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            CustomUserPrincipal customPrincipal = new CustomUserPrincipal(userId, email);

            permissions.add("ROLE_" + role);

            if (jwtUtil.validateToken(jwt)) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                customPrincipal,
                                null,
                                permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
                        );

                authenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }

}













