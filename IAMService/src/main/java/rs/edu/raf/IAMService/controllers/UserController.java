package rs.edu.raf.IAMService.controllers;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.IAMService.data.dto.*;
import rs.edu.raf.IAMService.data.entites.PasswordChangeToken;
import rs.edu.raf.IAMService.data.entites.Permission;
import rs.edu.raf.IAMService.data.entites.User;
import rs.edu.raf.IAMService.data.enums.RoleType;
import rs.edu.raf.IAMService.exceptions.EmailTakenException;
import rs.edu.raf.IAMService.exceptions.MissingRoleException;
import rs.edu.raf.IAMService.jwtUtils.JwtUtil;
import rs.edu.raf.IAMService.services.UserService;
import rs.edu.raf.IAMService.services.impl.PasswordChangeTokenServiceImpl;
import rs.edu.raf.IAMService.utils.ChangedPasswordTokenUtil;
import rs.edu.raf.IAMService.utils.SubmitLimiter;
import rs.edu.raf.IAMService.validator.PasswordValidator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(
        value = "/api/users",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
)
public class UserController {

    private final HttpServletRequest request;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final SubmitLimiter submitLimiter;
    private final ChangedPasswordTokenUtil changedPasswordTokenUtil;
    private final PasswordValidator passwordValidator;
    private final PasswordChangeTokenServiceImpl passwordChangeTokenService;


    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createEmployee(@RequestBody EmployeeDto employeeDto) {
        try {
            EmployeeDto newEmployeeDto = userService.createEmployee(employeeDto);
            return ResponseEntity.ok(newEmployeeDto.getId());
        } catch (EmailTakenException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (MissingRoleException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/public/agent")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createAgent(@RequestBody AgentDto agentDto) {
        try {
            AgentDto newAgentDto = userService.createAgent(agentDto);
            return ResponseEntity.ok(newAgentDto.getId());
        } catch (EmailTakenException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (MissingRoleException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


    @PostMapping(path = "/password-change-initialization", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<PasswordChangeTokenDto> initiatesChangePassword(@RequestBody LoginDto loginDto) {
        String email = loginDto.getEmail();
        String password = loginDto.getPassword();
        Optional<User> userOptional = userService.findUserByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (!passwordEncoder.matches(password, userOptional.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!submitLimiter.allowRequest(email)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        String baseURL = "uzeti od front-a rutu za promenu sifre";
        PasswordChangeTokenDto passwordChangeTokenDto =
                changedPasswordTokenUtil.generateToken(userService.findByEmail(email.toLowerCase()), baseURL);
        userService.sendToQueue(email, passwordChangeTokenDto.getUrlLink());
        return ResponseEntity.ok().body(passwordChangeTokenDto);
    }

    @PostMapping(path = "/password-change-confirmation/{token}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changePasswordSubmit(@PathVariable String token, @RequestBody PasswordChangeTokenWithPasswordDto passwordChangeTokenWithPasswordDto) {
        String newPassword = passwordChangeTokenWithPasswordDto.getNewPassword();
        PasswordChangeTokenDto passwordChangeTokenDto = passwordChangeTokenWithPasswordDto.getPasswordChangeTokenDto();
        if (!token.equals(passwordChangeTokenDto.getToken())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tokeni se ne poklapaju");
        }
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

    @PostMapping(path = "/public/password-reset-initialization/{email}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<PasswordChangeTokenDto> initiatesResetPassword(@PathVariable String email) {
        Optional<User> userOptional = userService.findUserByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        String baseURL = "uzeti od front-a rutu za resetovanje sifre ";
        PasswordChangeToken passwordChangeToken =
                changedPasswordTokenUtil.generateTokenInDB(email, baseURL);
        if (passwordChangeTokenService.findByEmail(email) != null) {
            passwordChangeTokenService.updateEntity(passwordChangeToken);
        } else {
            passwordChangeTokenService.createEntity(passwordChangeToken);
        }
        userService.PasswordResetsendToQueue(email, passwordChangeToken.getUrlLink());

        PasswordChangeTokenDto passwordChangeTokenDto = new PasswordChangeTokenDto(passwordChangeToken.getToken(), passwordChangeToken.getExpireTime(), passwordChangeToken.getEmail(), passwordChangeToken.getUrlLink());
        return ResponseEntity.ok().body(passwordChangeTokenDto);
    }

    @PostMapping(path = "/public/password-reset-confirmation/{token}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> resetPasswordSubmit(@PathVariable String token, @RequestBody PasswordChangeTokenWithPasswordDto passwordChangeTokenWithPasswordDto) {
        String newPassword = passwordChangeTokenWithPasswordDto.getNewPassword();
        PasswordChangeTokenDto passwordChangeTokenDto = passwordChangeTokenWithPasswordDto.getPasswordChangeTokenDto();
        PasswordChangeToken passwordChangeToken = passwordChangeTokenService.findByEmail(passwordChangeTokenDto.getEmail());
        if (!token.equals(passwordChangeToken.getToken())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tokeni se ne poklapaju");
        }
        Optional<User> userOptional = userService.findUserByEmail(passwordChangeTokenDto.getEmail());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Korisnik sa emailom: " + passwordChangeTokenDto.getEmail() + " ne postoji ili nije pronadjen");
        }
        User user = userOptional.get();
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


    @GetMapping(path = "/getUserPermissions/{id}")
    public ResponseEntity<?> getUserPermissions(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.getUserPermissions(id));
    }

    @PostMapping(path = "/addUserPermission/{id}")
    public ResponseEntity<?> addUserPermission(@PathVariable Long id, @RequestBody Permission permission) {
        userService.addUserPermission(id, permission);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/removeUserPermission/{id}")
    public ResponseEntity<?> removeUserPermission(@PathVariable Long id, @RequestBody Permission permission) {
        userService.removeUserPermission(id, permission);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(path = "/deleteAndSetUserPermissions/{id}")
    public ResponseEntity<?> deleteAndSetUserPermissions(@PathVariable Long id, @RequestBody List<Permission> permissionList) {
        userService.deleteAndSetUserPermissions(id, permissionList);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/public/private-client")
    public PrivateClientDto createPrivateClient(@RequestBody PrivateClientDto clientDto) {
        return userService.createPrivateClient(clientDto);
    }

    @PostMapping("/public/corporate-client")
    public CorporateClientDto createCorporateClient(@RequestBody CorporateClientDto clientDto) {
        return userService.createCorporateClient(clientDto);
    }

    @PostMapping("/public/{email}/password-activation")
    public Long activateClient(@PathVariable String email,
                               @RequestBody PasswordActivationDto dto) {
        return userService.passwordActivation(email, dto.getPassword());
    }

    @GetMapping(path = "/findByEmail/{email}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<?> findByEmail(@PathVariable String email) {
        UserDto userDto = userService.findByEmail(email);
        if (userDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(userDto);
    }

    @GetMapping(path = "/findById/{id}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<?> findById(@PathVariable Long id) {
        UserDto userDto = userService.findById(id);
        if (userDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping(path = "/delete/{email}", consumes = MediaType.ALL_VALUE)
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_USER')")
    @Transactional
    public ResponseEntity<?> deleteUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.deleteUserByEmail(email));
    }

    @PutMapping(path = "/updateEmployee", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    public ResponseEntity<?> updateEmployee(@RequestBody EmployeeDto employeeDto) {
        return updateUser(employeeDto);
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE','ROLE_USER')")
    @PutMapping(path = "/updateCorporateClient", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCorporateClient(@RequestBody CorporateClientDto corporateClientDto) {
        return updateUser(corporateClientDto);
    }

    @PutMapping(path = "/updatePrivateClient", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE','ROLE_USER')")
    public ResponseEntity<?> updatePrivateClient(@RequestBody PrivateClientDto privateClientDto) {
        return updateUser(privateClientDto);
    }

    @GetMapping(path = "/findAll", consumes = MediaType.ALL_VALUE)
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    public ResponseEntity<?> updateUser(UserDto userDto) {
        Claims claims = getClaims(request);
        if (claims == null) {
            return ResponseEntity.status(401).build();
        }
        RoleType roleType = RoleType.valueOf((String) claims.get("role"));
        UserDto userDtoFromDB = userService.findByEmail(userDto.getEmail());
        if (roleType.equals(RoleType.USER)) {
            if (userDto.getEmail().equals(claims.get("email"))) {
                if (validationCheck(userDto, userDtoFromDB)) {
                    return ResponseEntity.ok(userService.updateUser(userDto));
                }
                return ResponseEntity.status(401).build();

            } else return ResponseEntity.status(403).build();
        }
        if (roleType.equals(RoleType.EMPLOYEE)) {
            if ((userDto.getRole().equals(RoleType.USER) || userDto.getEmail().equals(claims.get("email"))) && validationCheck(userDto, userDtoFromDB)) {
                return ResponseEntity.ok(userService.updateUser(userDto));
            } else return ResponseEntity.status(403).build();
        }

        if (roleType.equals(RoleType.ADMIN)) {
            if (validationCheck(userDto, userDtoFromDB)) {
                UserDto ud = userService.updateUser(userDto);
                return ResponseEntity.ok(ud);
            } else {
                return ResponseEntity.status(403).build();
            }
        }
        return ResponseEntity.ok(userService.updateUser(userDto));
    }

    public Claims getClaims(HttpServletRequest request) {
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

    public boolean validationCheck(UserDto userDto, UserDto userDtoFromDB) {
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


    @PutMapping(path = "/activateEmployee/{id}")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public ResponseEntity<Boolean> activateEmployee(@PathVariable int id) {

        try {
            userService.employeeActivation(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Boolean.FALSE);
        }

        return ResponseEntity.status(HttpStatus.OK).body(Boolean.TRUE);
    }

    @PutMapping(path = "/deactivateEmployee/{id}")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public ResponseEntity<Boolean> deactivateEmployee(@PathVariable int id) {
        try {
            userService.employeeDeactivation(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Boolean.FALSE);
        }

        return ResponseEntity.status(HttpStatus.OK).body(Boolean.TRUE);
    }

    @PutMapping(path = "/setAgentLimit/{id}")
    @PreAuthorize(value = "hasRole('ROLE_SUPERVISOR')")
    public ResponseEntity<Boolean> setAgentLimit(@PathVariable int id, @RequestBody BigDecimal limit) {
        try {
            userService.setAgentLimit(id, limit);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Boolean.FALSE);
        }

        return ResponseEntity.status(HttpStatus.OK).body(Boolean.TRUE);
    }

}


