package rs.edu.raf.IAMService.controllers;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.IAMService.data.dto.*;
import rs.edu.raf.IAMService.data.entites.Permission;
import rs.edu.raf.IAMService.data.enums.RoleType;
import rs.edu.raf.IAMService.exceptions.EmailTakenException;
import rs.edu.raf.IAMService.exceptions.MissingRoleException;
import rs.edu.raf.IAMService.jwtUtils.JwtUtil;
import rs.edu.raf.IAMService.services.UserService;

import java.math.BigDecimal;
import java.util.List;

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
    private final JwtUtil jwtUtil;

    @PostMapping("/create/employee")
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

    @PostMapping("/create/agent")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPERVISOR')")
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


    @PostMapping(path = "/password-change", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<?> initiatesChangePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        return ResponseEntity.ok().body(userService.setPassword(changePasswordDto.getEmail(), changePasswordDto.getPassword()));
    }


    @PostMapping("/public/create/private-client")
    public PrivateClientDto createPrivateClient(@RequestBody PrivateClientDto clientDto) {
        return userService.createPrivateClient(clientDto);
    }

    @PostMapping("/public/create/corporate-client")
    public CorporateClientDto createCorporateClient(@RequestBody CorporateClientDto clientDto) {
        return userService.createCorporateClient(clientDto);
    }

    @PostMapping("/public/create/company-employee")
    public ResponseEntity<?> createCompanyEmployee(@RequestBody CompanyEmployeeDto clientDto) {
        return ResponseEntity.ok(userService.createCompanyEmployee(clientDto));
    }

    @PostMapping("/public/password-activation/{email}")
    public Long activateClient(@PathVariable String email,
                               @RequestBody PasswordActivationDto dto) {
        return userService.passwordActivation(email, dto.getPassword());
    }

    @GetMapping(path = "/email/{email}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<?> findByEmail(@PathVariable String email) {
        UserDto userDto = userService.findByEmail(email);
        if (userDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(userDto);
    }

    @GetMapping(path = "/{id}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<?> findById(@PathVariable Long id) {
        UserDto userDto = userService.findById(id);
        if (userDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(userDto);
    }

    @GetMapping(path = "/agent-limit/{id}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<BigDecimal> getAgentsLeftLimit(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getAgentsLeftLimit(id));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPERVISOR')")
    @PatchMapping(path = "/agent-limit/reset/{id}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Void> resetAgentsLeftLimit(@PathVariable Long id) {
        userService.resetAgentsLeftLimit(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/decrease-limit/{id}/{amount}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Void> decreaseAgentLimit(@PathVariable Long id, @PathVariable Double amount) {
        userService.decreaseAgentLimit(id, amount);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/delete/{email}", consumes = MediaType.ALL_VALUE)
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_USER')")
    @Transactional
    public ResponseEntity<?> deleteUserByEmail(@PathVariable String email) {
        try {
            return ResponseEntity.ok(userService.deleteUserByEmail(email));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

    }

    @PutMapping(path = "/update/employee", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    public ResponseEntity<?> updateEmployee(@RequestBody EmployeeDto employeeDto) {
        return updateUser(employeeDto);
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE','ROLE_USER')")
    @PutMapping(path = "/update/corporate-client", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCorporateClient(@RequestBody CorporateClientDto corporateClientDto) {
        return updateUser(corporateClientDto);
    }

    @PutMapping(path = "/update/private-client", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE','ROLE_USER')")
    public ResponseEntity<?> updatePrivateClient(@RequestBody PrivateClientDto privateClientDto) {
        return updateUser(privateClientDto);
    }

    @GetMapping(path = "/all", consumes = MediaType.ALL_VALUE)
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE','ROLE_SUPERVISOR')")
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


    @PutMapping(path = "/employee-activate/{id}", consumes = MediaType.ALL_VALUE)
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public ResponseEntity<Boolean> activateEmployee(@PathVariable int id) {

        try {
            userService.employeeActivation(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Boolean.FALSE);
        }

        return ResponseEntity.status(HttpStatus.OK).body(Boolean.TRUE);
    }

    @PutMapping(path = "/employee-deactivate/{id}", consumes = MediaType.ALL_VALUE)
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public ResponseEntity<Boolean> deactivateEmployee(@PathVariable int id) {
        try {
            userService.employeeDeactivation(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Boolean.FALSE);
        }

        return ResponseEntity.status(HttpStatus.OK).body(Boolean.TRUE);
    }

    @PutMapping(path = "/reduce-daily-limit", consumes = MediaType.ALL_VALUE)
    @CrossOrigin(origins = "http://localhost:8001")
    @PreAuthorize(value = "hasRole('ROLE_AGENT')")
    public ResponseEntity<Boolean> reduceTheAgentLimit(@PathParam("agentId") int id, @PathParam("amount") int amount) {
        return null;
    }



    //TODO videti da li cemo uopste raditi sa permisijama
    // za sad ne radimo, ovo ne radi kako treba i ne vidim poentu da ispravljam
    // ispravicu ako krenemo da radimo sa permisijama.

    /**  ne radi, ispraviti da se ne radi sa Permissions, nego PermissionDTO
     */
  /*  @GetMapping(path = "/getUserPermissions/{id}", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<?> getUserPermissions(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.getUserPermissions(id));
    }

    /**
     * ne radi, ispraviti da se ne radi sa Permissions, nego PermissionDTO
     */
   /* @PostMapping(path = "/addUserPermission/{id}")
    public ResponseEntity<?> addUserPermission(@PathVariable Long id, @RequestBody Permission permission) {
        userService.addUserPermission(id, permission);
        return ResponseEntity.ok().build();
    }

    /**
     * ne radi, ispraviti da se ne radi sa Permissions, nego PermissionDTO
     */
    /*@PostMapping(path = "/removeUserPermission/{id}")
    public ResponseEntity<?> removeUserPermission(@PathVariable Long id, @RequestBody Permission permission) {
        userService.removeUserPermission(id, permission);
        return ResponseEntity.ok().build();
    }

    /**
     * ne radi, ispraviti da se ne radi sa Permissions, nego PermissionDTO
     */
    /*@PatchMapping(path = "/deleteAndSetUserPermissions/{id}")
    public ResponseEntity<?> deleteAndSetUserPermissions(@PathVariable Long id, @RequestBody List<Permission> permissionList) {
        userService.deleteAndSetUserPermissions(id, permissionList);
        return ResponseEntity.ok().build();
    }*/


}


