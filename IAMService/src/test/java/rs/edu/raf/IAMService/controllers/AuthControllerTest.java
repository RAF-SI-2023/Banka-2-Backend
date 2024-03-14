package rs.edu.raf.IAMService.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rs.edu.raf.IAMService.data.dto.EmployeeDto;
import rs.edu.raf.IAMService.data.dto.PasswordDto;
import rs.edu.raf.IAMService.exceptions.EmailNotFoundException;
import rs.edu.raf.IAMService.services.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testActivateEmployee_Success() {
        // Setup
        PasswordDto passwordDto = new PasswordDto("test@user.com", "password");
        EmployeeDto employeeDto = new EmployeeDto();
        when(userService.activateEmployee(passwordDto.getEmail(), passwordDto.getPassword())).thenReturn(employeeDto);

        // Execution
        ResponseEntity<?> response = authController.activateEmployee(passwordDto);

        // Assertion
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employeeDto, response.getBody());
    }

    @Test
    public void testActivateEmployee_EmailNotFound() {
        // Setup
        PasswordDto passwordDto = new PasswordDto("test@user.com", "password");
        when(userService.activateEmployee(passwordDto.getEmail(), passwordDto.getPassword())).thenThrow(new EmailNotFoundException("test@user.com"));

        // Execution
        ResponseEntity<?> response = authController.activateEmployee(passwordDto);

        // Assertion
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(
                "User with email 'test@user.com' not found",
                response.getBody()
        );
    }
}