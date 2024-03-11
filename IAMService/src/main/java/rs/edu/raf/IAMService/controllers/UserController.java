package rs.edu.raf.IAMService.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.IAMService.data.dto.PasswordChangeTokenDto;
import rs.edu.raf.IAMService.data.entites.User;
import rs.edu.raf.IAMService.services.UserService;
import rs.edu.raf.IAMService.utils.ChangedPasswordTokenUtil;
import rs.edu.raf.IAMService.utils.SubmitLimiter;
import rs.edu.raf.IAMService.validator.PasswordValidator;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    @Autowired
    private HttpServletRequest request;

    private final UserService userService;

    private PasswordEncoder passwordEncoder;

    private final SubmitLimiter submitLimiter;
    private final ChangedPasswordTokenUtil changedPasswordTokenUtil;

    public UserController(UserService userService, ChangedPasswordTokenUtil changedPasswordTokenUtil, PasswordEncoder passwordEncoder, SubmitLimiter submitLimiter) {
        this.userService = userService;
        this.changedPasswordTokenUtil = changedPasswordTokenUtil;
        this.passwordEncoder = passwordEncoder;
        this.submitLimiter = submitLimiter;
    }


    @GetMapping(path = "/changePassword/{email}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<PasswordChangeTokenDto> InitiatesChangePassword(@PathVariable String email) {
        if (!submitLimiter.allowRequest(email))
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();


        int port = this.request.getServerPort();

        String baseURL = "http://localhost:" + port + "/api/users/changePasswordSubmit/";

        PasswordChangeTokenDto passwordChangeTokenDto = changedPasswordTokenUtil.generateToken(userService.findByEmail(email), baseURL);

        userService.sendToQueue(email, passwordChangeTokenDto.getUrlLink());


        return ResponseEntity.ok().body(passwordChangeTokenDto);
    }

    @PostMapping(path = "/changePasswordSubmit/{token} ", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<?> changePasswordSubmit(String newPassword, PasswordChangeTokenDto passwordChangeTokenDto) {

        Optional<User> userOptional = userService.findUserByEmail(passwordChangeTokenDto.getEmail());
        if (userOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Korisnik sa emailom: " + passwordChangeTokenDto.getEmail() + " ne postoji ili nije pronadjen");

        User user = userOptional.get();

        if (passwordEncoder.matches(newPassword, user.getPassword()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Korisnik vec koristi tu sifru");

        if (!PasswordValidator.isValid(newPassword))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Pogresan format lozinke");


        if (changedPasswordTokenUtil.isTokenValid(passwordChangeTokenDto)) {

            user.setPassword(passwordEncoder.encode(newPassword));
            userService.updateEntity(user);

            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(401).body("Token za mail: " + passwordChangeTokenDto.getEmail() + " nije vise validan");
    }


}
