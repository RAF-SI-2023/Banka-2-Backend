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


    @Test
    void testInitiatesResetPassword_UserFound() {
        // Arrange
        String email = "lol@lol.com";
        Role role = new Role(RoleType.USER);
        User user1 = new User();
        user1.setUsername("lol");
        user1.setPassword(passwordEncoder.encode("lol"));
        user1.setEmail("lol");
        user1.setAddress("lol");
        user1.setRole(null);
        user1.setPhone("lol");
        user1.setPermissions(null);
        user1.setDateOfBirth(null);
        user1.setRole(role);
        user1.setPassword("password");
        user1.setId(1L);
        Optional<User> optionalUser = Optional.of(user1);

        when(userService.findUserByEmail(email)).thenReturn(Optional.empty());
        //when(optionalUser.isEmpty()).thenReturn(true);

        // Act
        ResponseEntity<PasswordChangeTokenDto> response = userController.initiatesResetPassword(email);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        // Add more assertions as needed
    }

    @Test
    void testInitiatesResetPassword() {
        String email = "lol";
        int port = 8000;

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getServerPort()).thenReturn(port);
        Role role = new Role(RoleType.USER);
        User user1 = new User();
        user1.setUsername("lol");
        user1.setPassword(passwordEncoder.encode("lol"));
        user1.setEmail("lol");
        user1.setAddress("lol");
        user1.setRole(null);
        user1.setPhone("lol");
        user1.setPermissions(null);
        user1.setDateOfBirth(null);
        user1.setRole(role);
        user1.setPassword("password");
        user1.setId(1L);

        PasswordChangeToken passwordChangeToken = new PasswordChangeToken();
        passwordChangeToken.setEmail(email);
        passwordChangeToken.setUrlLink("http://localhost:8000/api/users/resetPasswordSubmit/");
        passwordChangeToken.setToken("token");
        passwordChangeToken.setExpireTime(1000L);

        PasswordChangeTokenDto passwordChangeTokenDto = new PasswordChangeTokenDto();
        passwordChangeTokenDto.setEmail(email);
        passwordChangeTokenDto.setUrlLink("http://localhost:8000/api/users/resetPasswordSubmit/");
        passwordChangeTokenDto.setToken("token");
        passwordChangeTokenDto.setExpireTime(1000L);

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail(email);
        loginDto.setPassword("password");


        when(userService.findUserByEmail(email)).thenReturn(Optional.of(user1));



        when(changedPasswordTokenUtil.generateTokenInDB(any(), anyString())).thenReturn(passwordChangeToken);
        // Test
        ResponseEntity<PasswordChangeTokenDto> responseEntity = userController.initiatesResetPassword(loginDto.getEmail());

        // Verification
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testResetPasswordSubmit() {
        // Mocking
        String newPassword = "Paaricefdsfghehe12";
        String email = "lol";
            String token = "token";
        PasswordChangeTokenDto passwordChangeTokenDto = new PasswordChangeTokenDto();
        passwordChangeTokenDto.setEmail(email);
        passwordChangeTokenDto.setExpireTime(1000L);
        passwordChangeTokenDto.setToken("token");

        PasswordChangeToken passwordChangeToken = new PasswordChangeToken();
        passwordChangeToken.setEmail(email);
        passwordChangeToken.setUrlLink("http://localhost:8000/api/users/resetPasswordSubmit/");
        passwordChangeToken.setToken(token);

        Role role = new Role(RoleType.USER);
        User user1 = new User();
        user1.setUsername("lol");
        user1.setPassword(passwordEncoder.encode("lol"));
        user1.setEmail("lol");
        user1.setAddress("lol");
        user1.setRole(null);
        user1.setPhone("lol");
        user1.setPermissions(null);
        user1.setDateOfBirth(null);
        user1.setRole(role);
        user1.setId(1L);


        when(passwordChangeTokenService.findByEmail(email)).thenReturn(passwordChangeToken);
        when(userService.findUserByEmail(email)).thenReturn(Optional.of(user1));
        when(passwordEncoder.matches(newPassword, user1.getPassword())).thenReturn(false);
        when(passwordValidator.isValid(newPassword)).thenReturn(true);
        when(changedPasswordTokenUtil.isTokenValid(passwordChangeTokenDto)).thenReturn(true);
        when(jwtUtil.extractAllClaims(anyString())).thenReturn(null);

        // Invoking the method
        PasswordChangeTokenWithPasswordDto passwordChangeTokenWithPasswordDto = new PasswordChangeTokenWithPasswordDto();
        passwordChangeTokenWithPasswordDto.setNewPassword(newPassword);
        passwordChangeTokenWithPasswordDto.setPasswordChangeTokenDto(passwordChangeTokenDto);


        ResponseEntity<?> responseEntity = userController.resetPasswordSubmit("token", passwordChangeTokenWithPasswordDto);

        // Verification
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(userService).updateEntity(user1);
    }


    @Test
    void testResetPasswordSubmit_TokenMatches() {
        // Arrange
        String token = "valid_token";
        PasswordChangeTokenDto passwordChangeTokenDto = new PasswordChangeTokenDto();
        passwordChangeTokenDto.setToken(token);
        String newPassword = "new_password";
        PasswordChangeToken passwordChangeToken = new PasswordChangeToken();
        when(passwordChangeTokenService.findByEmail(passwordChangeTokenDto.getEmail())).thenReturn(passwordChangeToken);
        when(passwordEncoder.matches(newPassword, "user_current_password")).thenReturn(false);
        when(changedPasswordTokenUtil.isTokenValid(passwordChangeTokenDto)).thenReturn(true);

        // Act
        ResponseEntity<?> response = userController.resetPasswordSubmit(token, new PasswordChangeTokenWithPasswordDto(newPassword, passwordChangeTokenDto));

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        // Add more assertions as needed
    }
    @Test
    void testResetPasswordSubmit_SamePassword() {
        // Arrange
        String token = "valid_token";
        PasswordChangeTokenDto passwordChangeTokenDto = new PasswordChangeTokenDto();
        passwordChangeTokenDto.setToken(token); // Set token to be different from 'token'
        passwordChangeTokenDto.setEmail("lol");
        PasswordChangeToken passwordChangeToken = new PasswordChangeToken();
        passwordChangeToken.setToken(token); // Set token to be different from 'token
        passwordChangeToken.setEmail("lol");
        String newPassword = "new_password";
        User user = new User();
        user.setEmail(passwordChangeTokenDto.getEmail());
        user.setPassword("new_password"); // Assuming the user's current password is "user_current_password"
        when(passwordChangeTokenService.findByEmail(anyString())).thenReturn(passwordChangeToken);
        when(userService.findUserByEmail(passwordChangeTokenDto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(newPassword, user.getPassword())).thenReturn(true);

        // Act
        ResponseEntity<?> response = userController.resetPasswordSubmit(token, new PasswordChangeTokenWithPasswordDto(newPassword, passwordChangeTokenDto));

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Korisnik vec koristi tu sifru", response.getBody());
    }

    @Test
    void testResetPasswordSubmit_InvalidPasswordFormat() {
        String token = "valid_token";
        PasswordChangeTokenDto passwordChangeTokenDto = new PasswordChangeTokenDto();
        passwordChangeTokenDto.setToken(token); // Set token to be different from 'token'
        passwordChangeTokenDto.setEmail("lol");
        PasswordChangeToken passwordChangeToken = new PasswordChangeToken();
        passwordChangeToken.setToken(token); // Set token to be different from 'token
        passwordChangeToken.setEmail("lol");
        String newPassword = "dasdas";
        User user = new User();
        user.setEmail(passwordChangeTokenDto.getEmail());
        user.setPassword("new_password"); // Assuming the user's current password is "user_current_password"
        when(passwordChangeTokenService.findByEmail(anyString())).thenReturn(passwordChangeToken);
        when(userService.findUserByEmail(passwordChangeTokenDto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(newPassword, user.getPassword())).thenReturn(false);
        when(passwordValidator.isValid(newPassword)).thenReturn(false);
        // Act
        ResponseEntity<?> response = userController.resetPasswordSubmit(token, new PasswordChangeTokenWithPasswordDto(newPassword, passwordChangeTokenDto));

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Pogresan format lozinke", response.getBody());
    }

    @Test
    void testResetPasswordSubmit_TokenNotValid() {
        String token = "valid_token";
        PasswordChangeTokenDto passwordChangeTokenDto = new PasswordChangeTokenDto();
        passwordChangeTokenDto.setToken(token); // Set token to be different from 'token'
        passwordChangeTokenDto.setEmail("lol");
        PasswordChangeToken passwordChangeToken = new PasswordChangeToken();
        passwordChangeToken.setToken(token); // Set token to be different from 'token
        passwordChangeToken.setEmail("lol");
        String newPassword = "dasdas";
        User user = new User();
        user.setEmail(passwordChangeTokenDto.getEmail());
        user.setPassword("new_password"); // Assuming the user's current password is "user_current_password"
        when(passwordChangeTokenService.findByEmail(anyString())).thenReturn(passwordChangeToken);
        when(userService.findUserByEmail(passwordChangeTokenDto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(newPassword, user.getPassword())).thenReturn(false);
        when(passwordValidator.isValid(newPassword)).thenReturn(true);
        when(changedPasswordTokenUtil.isTokenValid(passwordChangeTokenDto)).thenReturn(false);

        // Act
        ResponseEntity<?> response = userController.resetPasswordSubmit(token, new PasswordChangeTokenWithPasswordDto(newPassword, passwordChangeTokenDto));

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

    }
}
