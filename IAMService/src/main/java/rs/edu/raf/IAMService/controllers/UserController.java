package rs.edu.raf.IAMService.controllers;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.IAMService.data.dto.PasswordChangeTokenDto;
import rs.edu.raf.IAMService.data.entites.Employee;
import rs.edu.raf.IAMService.data.entites.User;
import rs.edu.raf.IAMService.data.enums.RoleType;
import rs.edu.raf.IAMService.jwtUtils.JwtUtil;
import rs.edu.raf.IAMService.services.UserService;
import rs.edu.raf.IAMService.utils.ChangedPasswordTokenUtil;
import rs.edu.raf.IAMService.utils.SubmitLimiter;
import rs.edu.raf.IAMService.validator.PasswordValidator;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "userApi")
public class UserController {

    private final HttpServletRequest request;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final SubmitLimiter submitLimiter;
    private final ChangedPasswordTokenUtil changedPasswordTokenUtil;

    private final PasswordValidator passwordValidator;


    public UserController(UserService userService, ChangedPasswordTokenUtil changedPasswordTokenUtil,
                          PasswordEncoder passwordEncoder, SubmitLimiter submitLimiter,
                          PasswordValidator passwordValidator, HttpServletRequest request, JwtUtil jwtUtil) {
        this.userService = userService;
        this.changedPasswordTokenUtil = changedPasswordTokenUtil;
        this.passwordEncoder = passwordEncoder;
        this.submitLimiter = submitLimiter;
        this.passwordValidator = passwordValidator;
        this.request = request;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping(path = "/changePassword/{email}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<PasswordChangeTokenDto> InitiatesChangePassword(@PathVariable String email) {

        if (!submitLimiter.allowRequest(email))
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();

        int port = this.request.getServerPort();

        String baseURL = "http://localhost:" + port + "/api/users/changePasswordSubmit/";

        PasswordChangeTokenDto passwordChangeTokenDto = changedPasswordTokenUtil.generateToken(userService.findByEmail(email.toLowerCase()), baseURL);

        userService.sendToQueue(email, passwordChangeTokenDto.getUrlLink());


        return ResponseEntity.ok().body(passwordChangeTokenDto);
    }


    @PostMapping(path = "/changePasswordSubmit/{token} ", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<?> changePasswordSubmit(String newPassword, PasswordChangeTokenDto passwordChangeTokenDto) {
        String tokenWithoutBearer = request.getHeader("authorization").replace("Bearer ", "");
        Claims extractedToken = jwtUtil.extractAllClaims(tokenWithoutBearer);

        Optional<User> userOptional = userService.findUserByEmail(passwordChangeTokenDto.getEmail());

        if (userOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Korisnik sa emailom: " + passwordChangeTokenDto.getEmail() + " ne postoji ili nije pronadjen");

        User user = userOptional.get();


        if (!extractedToken.get("email").toString().equalsIgnoreCase(passwordChangeTokenDto.getEmail())) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Nemate autorizaciju da promenite mail: " + passwordChangeTokenDto.getEmail());
        }


        if (passwordEncoder.matches(newPassword, user.getPassword()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Korisnik vec koristi tu sifru");

        if (!passwordValidator.isValid(newPassword))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Pogresan format lozinke");


        if (changedPasswordTokenUtil.isTokenValid(passwordChangeTokenDto)) {

            user.setPassword(passwordEncoder.encode(newPassword));
            userService.updateEntity(user);

            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(401).body("Token za mail: " + passwordChangeTokenDto.getEmail() + " nije vise validan");
    }

    @PutMapping(path = "/changeActiveStatusOfEmployee/{email} ", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<?> changeActiveStatusOfEmployee(@PathVariable String email, String employeeEmail) {
        Optional<User> employeeOpt = userService.findUserByEmail(employeeEmail);
        Optional<User> adminOpt = userService.findUserByEmail(email);
        User admin = adminOpt.get();
        Employee employee;
        if(employeeOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee with email: " + employeeEmail + " not found.");

        if (employeeOpt.get() instanceof Employee) {
            employee = (Employee) employeeOpt.get();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Cannot change active status of non-employee user.");
        }
        if (admin.getRole().getRoleType().equals(RoleType.ADMIN)) {
            employee.setActive(!employee.isActive());
            userService.updateEntity(employee);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to change active status of employee.");

    }

}
