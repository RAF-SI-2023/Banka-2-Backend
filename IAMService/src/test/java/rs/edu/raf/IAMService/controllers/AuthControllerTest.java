package rs.edu.raf.IAMService.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import rs.edu.raf.IAMService.data.dto.*;
import rs.edu.raf.IAMService.data.entites.User;
import rs.edu.raf.IAMService.jwtUtils.JwtUtil;
import rs.edu.raf.IAMService.mapper.UserMapper;
import rs.edu.raf.IAMService.services.UserService;

import javax.naming.AuthenticationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin() throws Exception {
        // Setup
        LoginDto loginRequest = new LoginDto("user@example.com", "password");
        User user = new User(); // Create a user object with the required fields.
        String expectedToken = "token";
        when(userService.findByEmail(loginRequest.getEmail())).thenReturn(userMapper.userToUserDto(user));
        when(jwtUtil.generateToken(any(UserDto.class))).thenReturn(expectedToken);

        // Act
        ResponseEntity<?> response = authController.login(loginRequest);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        TokenDto responseBody = (TokenDto) response.getBody();
        assertNotNull(responseBody);
        assertEquals(expectedToken, responseBody.getToken());

        // Verify interactions
        verify(authenticationProvider).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService).findByEmail(loginRequest.getEmail());
        verify(jwtUtil).generateToken(any(UserDto.class));
    }

    @Test
    void testUnsuccessfulLogin() {
        // Setup
        LoginDto loginRequest = new LoginDto("wrong@example.com", "wrongpassword");
        when(authenticationProvider.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Bad credentials") {});

        // Act
        ResponseEntity<?> response = authController.login(loginRequest);

        // Assert
        assertEquals(401, response.getStatusCodeValue());

        // Verify interactions
        verify(authenticationProvider).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService, never()).findByEmail(anyString());
        verify(jwtUtil, never()).generateToken(any(UserDto.class));
    }


}