package rs.edu.raf.IAMService.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.IAMService.configuration.PasswordEncoderConfig;
import rs.edu.raf.IAMService.data.dto.*;
import rs.edu.raf.IAMService.data.entites.Role;
import rs.edu.raf.IAMService.data.enums.RoleType;
import rs.edu.raf.IAMService.exceptions.EmailNotFoundException;
import rs.edu.raf.IAMService.jwtUtils.JwtUtil;
import rs.edu.raf.IAMService.services.UserService;


@RestController
@CrossOrigin
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationProvider authenticationProvider;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/create-admin")
    public ResponseEntity<?> jwt() {
        UserDto adminUser = new UserDto();
        adminUser.setEmail("admin@raf.rs");
        adminUser.setUsername("admin");
        Role adminRole = new Role();
        adminRole.setRoleType(RoleType.ADMIN);
        adminUser.setRole(RoleType.ADMIN);

        String encodedPassword = passwordEncoder.encode("admin");
        adminUser = userService.createAdmin(adminUser, encodedPassword);

        return ResponseEntity.ok(new TokenDto(jwtUtil.generateToken(adminUser)));
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

    @PutMapping("/activate-employee")
    public ResponseEntity<?> activateEmployee(@RequestBody PasswordDto passwordDto) {
        try {
            EmployeeDto employeeDto = userService.activateEmployee(passwordDto.getEmail(), passwordDto.getPassword());
            return ResponseEntity.ok(employeeDto);
        } catch (EmailNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
