package rs.edu.raf.IAMService.jwtUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import rs.edu.raf.IAMService.data.dto.UserDto;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class JwtUtil {
    private static final String SECRET_KEY = "jwt-token-secret-key";

    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public String generateToken(UserDto userDto) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userDto.getId());
        claims.put("email", userDto.getEmail());
        claims.put("role", userDto.getRole());
        claims.put("permissions", userDto.getPermissions());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDto.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
    }

    public boolean validateToken(String token, UserDetails user) {
        return (user.getUsername().equals(extractEmail(token)) && !isTokenExpired(token));
    }



}
