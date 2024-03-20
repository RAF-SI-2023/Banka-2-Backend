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
import rs.edu.raf.IAMService.data.dto.UserDto;
import rs.edu.raf.IAMService.data.entites.Role;
import rs.edu.raf.IAMService.data.entites.User;
import rs.edu.raf.IAMService.data.enums.RoleType;
import rs.edu.raf.IAMService.jwtUtils.JwtUtil;
import rs.edu.raf.IAMService.repositories.UserRepository;
import rs.edu.raf.IAMService.services.UserService;
import rs.edu.raf.IAMService.utils.ChangedPasswordTokenUtil;
import rs.edu.raf.IAMService.utils.SubmitLimiter;
import rs.edu.raf.IAMService.validator.PasswordValidator;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class PasswordChangeTest {

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


    @Mock(answer = Answers.RETURNS_MOCKS)
    private User user;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testInitiatesChangePassword() {
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

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail(email);
        loginDto.setPassword("password");
        reset(submitLimiter);

        when(submitLimiter.allowRequest(anyString())).thenReturn(true);

        when(userService.findUserByEmail(email)).thenReturn(Optional.of(user1));

        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        PasswordChangeTokenDto passwordChangeTokenDto = new PasswordChangeTokenDto();
        passwordChangeTokenDto.setEmail(email);
        passwordChangeTokenDto.setUrlLink("http://localhost:8000/api/users/changePasswordSubmit/");
        passwordChangeTokenDto.setToken("token");
        passwordChangeTokenDto.setExpireTime(1000L);



        when(changedPasswordTokenUtil.generateToken(any(), anyString())).thenReturn(passwordChangeTokenDto);
        // Test
        ResponseEntity<PasswordChangeTokenDto> responseEntity = userController.initiatesChangePassword(loginDto);


        // Verification
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(userService).findByEmail(email);
        verify(userService).sendToQueue(eq(email), anyString());
    }

    @Test
    void testChangePasswordSubmit() {
        // Mocking
        String newPassword = "Paaricefdsfghehe12";
        String email = "lol";
        PasswordChangeTokenDto passwordChangeTokenDto = new PasswordChangeTokenDto();
        passwordChangeTokenDto.setEmail(email);
        passwordChangeTokenDto.setToken(anyString());

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


        String token = "exampleToken";
        Claims claims = new DefaultClaims();
        claims.put("email", email);


        when(userService.findUserByEmail(email)).thenReturn(Optional.of(user1));
        when(passwordEncoder.matches(newPassword, user1.getPassword())).thenReturn(false);
        when(passwordValidator.isValid(newPassword)).thenReturn(true);
        when(changedPasswordTokenUtil.isTokenValid(passwordChangeTokenDto)).thenReturn(true);
        when(jwtUtil.extractAllClaims(anyString())).thenReturn(null);


        when(request.getHeader("authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.extractAllClaims(token)).thenReturn(claims);

        // Invoking the method
        PasswordChangeTokenWithPasswordDto passwordChangeTokenWithPasswordDto = new PasswordChangeTokenWithPasswordDto();
        passwordChangeTokenWithPasswordDto.setNewPassword(newPassword);
        passwordChangeTokenWithPasswordDto.setPasswordChangeTokenDto(passwordChangeTokenDto);


        ResponseEntity<?> responseEntity = userController.changePasswordSubmit(passwordChangeTokenDto.getToken(), passwordChangeTokenWithPasswordDto);

        // Verification
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(userService).updateEntity(user1);
    }

    @Test
    public void testTokenMismatch() {
        // Arrange

        String validToken = "validToken";
        String invalidToken = "invalidToken";
        PasswordChangeTokenWithPasswordDto passwordChangeTokenWithPasswordDto = new PasswordChangeTokenWithPasswordDto();
        PasswordChangeTokenDto passwordChangeTokenDto = new PasswordChangeTokenDto();
        passwordChangeTokenDto.setToken(validToken);
        passwordChangeTokenWithPasswordDto.setPasswordChangeTokenDto(passwordChangeTokenDto);

        // Act
        ResponseEntity<?> responseEntity = userController.changePasswordSubmit(invalidToken, passwordChangeTokenWithPasswordDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Tokeni se ne poklapaju", responseEntity.getBody());
    }

    @Test
    public void testUserNotFound() {
        // Arrange
        String email = "lol";
        String validToken = "validToken";
        PasswordChangeTokenWithPasswordDto passwordChangeTokenWithPasswordDto = new PasswordChangeTokenWithPasswordDto();
        PasswordChangeTokenDto passwordChangeTokenDto = new PasswordChangeTokenDto();
        passwordChangeTokenDto.setToken(validToken);
        passwordChangeTokenDto.setEmail("example@example.com");
        passwordChangeTokenWithPasswordDto.setPasswordChangeTokenDto(passwordChangeTokenDto);
        Claims claims = new DefaultClaims();
        claims.put("email", email);

        String token = "exampleToken";
        when(request.getHeader("authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.extractAllClaims(token)).thenReturn(claims);

        when(userService.findUserByEmail(anyString())).thenReturn(Optional.empty());


        // Act
        ResponseEntity<?> responseEntity = userController.changePasswordSubmit(validToken, passwordChangeTokenWithPasswordDto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

    }

    @Test
    public void testUnauthorizedEmailChange() {
        String newPassword = "Paaricefdsfghehe12";
        String email = "lol";
        PasswordChangeTokenDto passwordChangeTokenDto = new PasswordChangeTokenDto();
        passwordChangeTokenDto.setEmail("nestodrugo");
        passwordChangeTokenDto.setToken(anyString());


        Role role = new Role(RoleType.USER);
        User user1 = new User();
        user1.setUsername("lol");
        user1.setPassword(passwordEncoder.encode("lol"));
        user1.setEmail("lol");
        user1.setAddress("lol");
        user1.setRole(new Role(RoleType.ADMIN));
        user1.setPhone("lol");
        user1.setPermissions(null);
        user1.setDateOfBirth(null);
        user1.setRole(role);
        user1.setId(1L);


        String token = "exampleToken";
        Claims claims = new DefaultClaims();
        claims.put("email", email);




        when(userService.findUserByEmail(anyString())).thenReturn(Optional.of(user1));
        when(passwordEncoder.matches(newPassword, user1.getPassword())).thenReturn(false);
        when(passwordValidator.isValid(newPassword)).thenReturn(true);
        when(changedPasswordTokenUtil.isTokenValid(passwordChangeTokenDto)).thenReturn(true);



        when(request.getHeader("authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.extractAllClaims(token)).thenReturn(claims);

        // Invoking the method
        PasswordChangeTokenWithPasswordDto passwordChangeTokenWithPasswordDto = new PasswordChangeTokenWithPasswordDto();
        passwordChangeTokenWithPasswordDto.setNewPassword(newPassword);
        passwordChangeTokenWithPasswordDto.setPasswordChangeTokenDto(passwordChangeTokenDto);


        ResponseEntity<?> responseEntity = userController.changePasswordSubmit(passwordChangeTokenDto.getToken(), passwordChangeTokenWithPasswordDto);

        // Verification
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());

    }
    @Test
    public void testSamePassword() {
        String newPassword = "lol";
        String email = "lol";
        PasswordChangeTokenDto passwordChangeTokenDto = new PasswordChangeTokenDto();
        passwordChangeTokenDto.setEmail("lol");
        passwordChangeTokenDto.setToken(anyString());


        Role role = new Role(RoleType.USER);
        User user1 = new User();
        user1.setUsername("lol");
        user1.setPassword(passwordEncoder.encode("lol"));
        user1.setEmail("lol");
        user1.setAddress("lol");
        user1.setRole(new Role(RoleType.ADMIN));
        user1.setPhone("lol");
        user1.setPermissions(null);
        user1.setDateOfBirth(null);
        user1.setRole(role);
        user1.setId(1L);


        String token = "exampleToken";
        Claims claims = new DefaultClaims();
        claims.put("email", email);



        when(userService.findUserByEmail(anyString())).thenReturn(Optional.of(user1));

        when(passwordEncoder.matches(newPassword, user1.getPassword())).thenReturn(true);
        when(passwordValidator.isValid(newPassword)).thenReturn(true);
        when(changedPasswordTokenUtil.isTokenValid(passwordChangeTokenDto)).thenReturn(true);



        when(request.getHeader("authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.extractAllClaims(token)).thenReturn(claims);

        // Invoking the method
        PasswordChangeTokenWithPasswordDto passwordChangeTokenWithPasswordDto = new PasswordChangeTokenWithPasswordDto();
        passwordChangeTokenWithPasswordDto.setNewPassword(newPassword);
        passwordChangeTokenWithPasswordDto.setPasswordChangeTokenDto(passwordChangeTokenDto);


        ResponseEntity<?> responseEntity = userController.changePasswordSubmit(passwordChangeTokenDto.getToken(), passwordChangeTokenWithPasswordDto);

        // Verification
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }

    @Test
    public void testSamePasswordValidator() {
        String newPassword = "Paaricefdsfghehe12";
        String email = "lol";
        PasswordChangeTokenDto passwordChangeTokenDto = new PasswordChangeTokenDto();
        passwordChangeTokenDto.setEmail("lol");
        passwordChangeTokenDto.setToken(anyString());


        Role role = new Role(RoleType.USER);
        User user1 = new User();
        user1.setUsername("lol");
        user1.setPassword(passwordEncoder.encode("lol"));
        user1.setEmail("lol");
        user1.setAddress("lol");
        user1.setRole(new Role(RoleType.ADMIN));
        user1.setPhone("lol");
        user1.setPermissions(null);
        user1.setDateOfBirth(null);
        user1.setRole(role);
        user1.setId(1L);


        String token = "exampleToken";
        Claims claims = new DefaultClaims();
        claims.put("email", email);



        when(userService.findUserByEmail(anyString())).thenReturn(Optional.of(user1));

        when(passwordEncoder.matches(newPassword, user1.getPassword())).thenReturn(true);
        when(passwordValidator.isValid(newPassword)).thenReturn(false);
        when(changedPasswordTokenUtil.isTokenValid(passwordChangeTokenDto)).thenReturn(false);



        when(request.getHeader("authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.extractAllClaims(token)).thenReturn(claims);

        // Invoking the method
        PasswordChangeTokenWithPasswordDto passwordChangeTokenWithPasswordDto = new PasswordChangeTokenWithPasswordDto();
        passwordChangeTokenWithPasswordDto.setNewPassword(newPassword);
        passwordChangeTokenWithPasswordDto.setPasswordChangeTokenDto(passwordChangeTokenDto);


        ResponseEntity<?> responseEntity = userController.changePasswordSubmit(passwordChangeTokenDto.getToken(), passwordChangeTokenWithPasswordDto);

        // Verification
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }

    @Test
    public void testSamePasswordTokenValidation() {
        String newPassword = "Paaricefdsfghehe12";
        String email = "lol";
        PasswordChangeTokenDto passwordChangeTokenDto = new PasswordChangeTokenDto();
        passwordChangeTokenDto.setEmail("lol");
        passwordChangeTokenDto.setToken(anyString());


        Role role = new Role(RoleType.USER);
        User user1 = new User();
        user1.setUsername("lol");
        user1.setPassword(passwordEncoder.encode("lol"));
        user1.setEmail("lol");
        user1.setAddress("lol");
        user1.setRole(new Role(RoleType.ADMIN));
        user1.setPhone("lol");
        user1.setPermissions(null);
        user1.setDateOfBirth(null);
        user1.setRole(role);
        user1.setId(1L);


        String token = "exampleToken";
        Claims claims = new DefaultClaims();
        claims.put("email", email);




        when(userService.findUserByEmail(anyString())).thenReturn(Optional.of(user1));

        when(passwordEncoder.matches(newPassword, user1.getPassword())).thenReturn(false);
        when(passwordValidator.isValid(newPassword)).thenReturn(true);
        when(changedPasswordTokenUtil.isTokenValid(passwordChangeTokenDto)).thenReturn(false);



        when(request.getHeader("authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.extractAllClaims(token)).thenReturn(claims);

        // Invoking the method
        PasswordChangeTokenWithPasswordDto passwordChangeTokenWithPasswordDto = new PasswordChangeTokenWithPasswordDto();
        passwordChangeTokenWithPasswordDto.setNewPassword(newPassword);
        passwordChangeTokenWithPasswordDto.setPasswordChangeTokenDto(passwordChangeTokenDto);


        ResponseEntity<?> responseEntity = userController.changePasswordSubmit(passwordChangeTokenDto.getToken(), passwordChangeTokenWithPasswordDto);

        // Verification
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());

    }
}
