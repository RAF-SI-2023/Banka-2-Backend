package rs.edu.raf.IAMService.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.IAMService.data.dto.LoginDto;
import rs.edu.raf.IAMService.data.dto.TokenDto;
import rs.edu.raf.IAMService.jwtUtils.JwtUtil;
import rs.edu.raf.IAMService.services.UserService;


@RestController
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationProvider authenticationProvider;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationProvider authenticationProvider, UserService userService, JwtUtil jwtUtil) {
        this.authenticationProvider = authenticationProvider;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginRequest) {
        try {
            authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(new TokenDto(jwtUtil.generateToken(userService.findByEmail(loginRequest.getEmail()))));
    }
}
