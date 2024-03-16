package rs.edu.raf.IAMService;

import io.cucumber.java.Before;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import org.apache.catalina.filters.RateLimitFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import rs.edu.raf.IAMService.controllers.UserController;
import rs.edu.raf.IAMService.data.dto.PasswordChangeDto;
import rs.edu.raf.IAMService.data.dto.PasswordChangeTokenDto;
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

import jakarta.servlet.http.HttpServletRequest;

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
        String email = "test@example.com";
        int port = 8000;

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getServerPort()).thenReturn(port);


        Role role = new Role(RoleType.USER);

        UserDto userDto = new UserDto();
        userDto.setUsername("lol");
        userDto.setEmail("lol");
        userDto.setAddress("lol");
        userDto.setRole(null);
        userDto.setPhone("lol");
        userDto.setPermissions(null);
        userDto.setDateOfBirth(null);
        userDto.setRole(role.getRoleType());
        userDto.setId(Long.valueOf(1));

        reset(submitLimiter);

        when(submitLimiter.allowRequest(anyString())).thenReturn(true);

        when(userService.findByEmail(email)).thenReturn(userDto);

        PasswordChangeTokenDto passwordChangeTokenDto = new PasswordChangeTokenDto();
        passwordChangeTokenDto.setEmail(email);
        passwordChangeTokenDto.setUrlLink("http://localhost:8000/api/users/changePasswordSubmit/");
        passwordChangeTokenDto.setToken("token");
        passwordChangeTokenDto.setExpireTime(1000L);


        when(changedPasswordTokenUtil.generateToken(any(), anyString())).thenReturn(passwordChangeTokenDto);
        // Test
        ResponseEntity<PasswordChangeTokenDto> responseEntity = userController.InitiatesChangePassword(email);


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


        //    ResponseEntity<?> responseEntity = userController.changePasswordSubmit(newPassword, passwordChangeTokenDto);

        // Verification
        //  assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(userService).updateEntity(user1);
    }

}
