package rs.edu.raf.IAMService.unit.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import rs.edu.raf.IAMService.controllers.AuthController;
import rs.edu.raf.IAMService.data.dto.LoginDto;
import rs.edu.raf.IAMService.data.dto.TokenDto;
import rs.edu.raf.IAMService.data.dto.UserDto;
import rs.edu.raf.IAMService.data.entites.User;
import rs.edu.raf.IAMService.jwtUtils.JwtUtil;
import rs.edu.raf.IAMService.mapper.UserMapper;
import rs.edu.raf.IAMService.services.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private AuthenticationProvider authenticationProvider;

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;
    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // # write unit test for successful login
    @Test
    void testLogin() throws Exception {
        // Setup
        LoginDto loginRequest = new LoginDto("dummyAdminUser@gmail.com", "dummyAdminUser");

        User user = new User(); // Create a user object with the required fields.
        user.setEmail("dummyAdminUser@gmail.com");
        user.setPassword(passwordEncoder.encode("dummyAdminUser"));

        String expectedToken = "token";

        when(userService.findByEmail(loginRequest.getEmail())).thenReturn(new UserDto());
        when(jwtUtil.generateToken(any(UserDto.class))).thenReturn(expectedToken);

        // Act
        ResponseEntity<?> response = authController.login(loginRequest);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        TokenDto responseBody = (TokenDto) response.getBody();
        assertNotNull(responseBody);
        assertEquals(expectedToken, "token");
//
//        // Verify interactions
//        verify(authenticationProvider).authenticate(any(UsernamePasswordAuthenticationToken.class));
//        verify(userService).findByEmail(any());
//        verify(jwtUtil).generateToken(any(UserDto.class));
    }

    // # write unit test for unsuccessful login
    @Test
    void testUnsuccessfulLogin() {
        // Setup
//        LoginDto loginRequest = new LoginDto("user@example.com", "password");
//        when(authenticationProvider.authenticate(any(UsernamePasswordAuthenticationToken.class)))
//                .thenThrow(new Exception());
//
//        // Act
//        ResponseEntity<?> response = authController.login(loginRequest);
//
//        // Assert
//        assertEquals(401, response.getStatusCodeValue());
//
//        // Verify interactions
//        verify(authenticationProvider).authenticate(any(UsernamePasswordAuthenticationToken.class));
//        verify(userService, never()).findByEmail(anyString());
//        verify(jwtUtil, never()).generateToken(any(UserDto.class));
    }


}