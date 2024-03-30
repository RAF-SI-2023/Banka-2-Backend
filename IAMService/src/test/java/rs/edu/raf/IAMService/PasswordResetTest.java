package rs.edu.raf.IAMService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import rs.edu.raf.IAMService.controllers.UserController;
import rs.edu.raf.IAMService.data.dto.LoginDto;
import rs.edu.raf.IAMService.data.dto.PasswordChangeTokenDto;
import rs.edu.raf.IAMService.data.dto.PasswordChangeTokenWithPasswordDto;
import rs.edu.raf.IAMService.data.entites.PasswordChangeToken;
import rs.edu.raf.IAMService.data.entites.Role;
import rs.edu.raf.IAMService.data.entites.User;
import rs.edu.raf.IAMService.data.enums.RoleType;
import rs.edu.raf.IAMService.jwtUtils.JwtUtil;
import rs.edu.raf.IAMService.repositories.UserRepository;
import rs.edu.raf.IAMService.services.PasswordChangeTokenService;
import rs.edu.raf.IAMService.services.UserService;
import rs.edu.raf.IAMService.services.impl.PasswordChangeTokenServiceImpl;
import rs.edu.raf.IAMService.utils.ChangedPasswordTokenUtil;
import rs.edu.raf.IAMService.utils.SubmitLimiter;
import rs.edu.raf.IAMService.validator.PasswordValidator;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;



public class PasswordResetTest {

    @Mock
    private UserService userService;

    @Mock
    private ChangedPasswordTokenUtil changedPasswordTokenUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SubmitLimiter submitLimiter;


    @Mock
    private PasswordValidator passwordValidator;

    @Mock
    private HttpServletRequest request;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordChangeTokenServiceImpl passwordChangeTokenService;


    @Mock(answer = Answers.RETURNS_MOCKS)
    private User user;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }


    // NAPISATI TESTOVE ZA PROMENU LOZINKE ZA KONTROLER



}
