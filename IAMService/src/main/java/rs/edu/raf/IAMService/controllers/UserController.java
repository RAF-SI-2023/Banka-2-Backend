package rs.edu.raf.IAMService.controllers;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.IAMService.data.dto.CorporateClientDto;
import rs.edu.raf.IAMService.data.dto.PasswordChangeTokenDto;
import rs.edu.raf.IAMService.data.dto.PrivateClientDto;
import rs.edu.raf.IAMService.data.dto.UserDto;
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
    private HttpServletRequest request;
    private final UserService userService;
    private PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final SubmitLimiter submitLimiter;
    private final ChangedPasswordTokenUtil changedPasswordTokenUtil;
    private final PasswordValidator passwordValidator;


    public UserController(UserService userService, ChangedPasswordTokenUtil changedPasswordTokenUtil, PasswordEncoder passwordEncoder, SubmitLimiter submitLimiter, PasswordValidator passwordValidator, HttpServletRequest request, JwtUtil jwtUtil) {
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

        if (!submitLimiter.allowRequest(email)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        int port = this.request.getServerPort();
        String baseURL = "http://localhost:" + port + "/api/users/changePasswordSubmit/";
        PasswordChangeTokenDto passwordChangeTokenDto =
                changedPasswordTokenUtil.generateToken(userService.findByEmail(email.toLowerCase()), baseURL);
        userService.sendToQueue(email, passwordChangeTokenDto.getUrlLink());
        return ResponseEntity.ok().body(passwordChangeTokenDto);
    }


    @PostMapping(path = "/changePasswordSubmit/{token} ", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<?> changePasswordSubmit(String newPassword, PasswordChangeTokenDto passwordChangeTokenDto) {
        String tokenWithoutBearer = request.getHeader("authorization").replace("Bearer ", "");
        Claims extractedToken = jwtUtil.extractAllClaims(tokenWithoutBearer);
        Optional<User> userOptional = userService.findUserByEmail(passwordChangeTokenDto.getEmail());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Korisnik sa emailom: " + passwordChangeTokenDto.getEmail() + " ne postoji ili nije pronadjen");
        }

        User user = userOptional.get();
        if (!extractedToken.get("email").toString().equalsIgnoreCase(passwordChangeTokenDto.getEmail())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Nemate autorizaciju da promenite mail: " + passwordChangeTokenDto.getEmail());
        }

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Korisnik vec koristi tu sifru");
        }

        if (!passwordValidator.isValid(newPassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Pogresan format lozinke");
        }

        if (changedPasswordTokenUtil.isTokenValid(passwordChangeTokenDto)) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userService.updateEntity(user);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(401).body("Token za mail: " + passwordChangeTokenDto.getEmail() + " nije vise validan");

    }


    @GetMapping(path = "/findByEmail/{email}", consumes = MediaType.ALL_VALUE)
    //  @PreAuthorize("hasAnyAuthority('ADMIN', 'EMPLOYEE', 'USER')")
    public ResponseEntity<?> findByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @GetMapping(path = "/findById/{id}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }


    @DeleteMapping(path = "/delete/{email}", consumes = MediaType.ALL_VALUE)
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_USER')")
    @Transactional
    public ResponseEntity<?> deleteUserByEmail(@PathVariable String email) {
        Claims claims = getClaims(request);
        if (claims == null) {
            return ResponseEntity.status(401).build();
        }

        RoleType roleType = RoleType.valueOf((String) claims.get("role"));

        if (roleType.equals(RoleType.ROLE_USER)) {
            if (!email.equals(claims.get("email"))) {
                return ResponseEntity.status(401).build();
            }
        }
        UserDto userDto = userService.findByEmail(email);
        if (roleType.equals(RoleType.ROLE_EMPLOYEE)) {
            if (!userDto.getRole().equals(RoleType.ROLE_USER) && !email.equals(claims.get("email"))) {
                return ResponseEntity.status(401).build();
            }
        }
        return ResponseEntity.ok(userService.deleteUserByEmail(email));
    }


    @PutMapping("/updateUser")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EMPLOYEE', 'USER')")
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto) {
        Claims claims = getClaims(request);
        if (claims == null) {
            return ResponseEntity.status(401).build();
        }
        RoleType roleType = RoleType.valueOf((String) claims.get("role"));
        UserDto userDtoFromDB = userService.findByEmail(userDto.getEmail());
        if (roleType.equals(RoleType.ROLE_USER)) {
            if (userDto.getEmail().equals(claims.get("email"))) {
                if (validationCheck(userDto, userDtoFromDB)) {
                    return ResponseEntity.ok(userService.updateUser(userDto));
                }
                return ResponseEntity.status(401).build();

            }
        }
        if (roleType.equals(RoleType.ROLE_EMPLOYEE)) {
            if ((userDto.getRole().equals(RoleType.ROLE_USER) || userDto.getEmail().equals(claims.get("email"))) && validationCheck(userDto, userDtoFromDB)) {
                return ResponseEntity.ok(userService.updateUser(userDto));
            } else return ResponseEntity.status(401).build();
        }

        if (roleType.equals(RoleType.ROLE_ADMIN)) {
            if (validationCheck(userDto, userDtoFromDB)) return ResponseEntity.ok(userService.updateUser(userDto));
            else return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(userService.updateUser(userDto));
    }


    private Claims getClaims(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || authHeader.isEmpty()) {
            authHeader = request.getHeader("authorization");
            if (authHeader == null || authHeader.isEmpty()) {
                return null;
            }
        }
        if (!authHeader.startsWith("Bearer ")) {
            return null;
        }
        String token = authHeader.substring(7);
        return jwtUtil.extractAllClaims(token);
    }

    private boolean validationCheck(UserDto userDto, UserDto userDtoFromDB) {
        if (userDto.getEmail().equalsIgnoreCase(userDtoFromDB.getEmail()) && userDto.getRole().equals(userDtoFromDB.getRole()) && userDto.getPermissions().equals(userDtoFromDB.getPermissions()) && userDto.getId().equals(userDtoFromDB.getId()) && userDto.getUsername().equals(userDtoFromDB.getUsername())) {
            if (userDto instanceof CorporateClientDto && userDtoFromDB instanceof CorporateClientDto) {
                if (((CorporateClientDto) userDto).getPrimaryAccountNumber().equals(((CorporateClientDto) userDtoFromDB).getPrimaryAccountNumber())) {
                    return true;
                }
            }
            if (userDto instanceof PrivateClientDto && userDtoFromDB instanceof PrivateClientDto) {
                if (((PrivateClientDto) userDto).getPrimaryAccountNumber().equals(((PrivateClientDto) userDtoFromDB).getPrimaryAccountNumber())) {
                    return true;
                }
            }
            return true;
        }
        return false;

    }

}


