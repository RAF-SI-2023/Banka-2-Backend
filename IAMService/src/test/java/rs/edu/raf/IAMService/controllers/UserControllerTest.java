package rs.edu.raf.IAMService.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rs.edu.raf.IAMService.data.dto.EmployeeDto;
import rs.edu.raf.IAMService.data.enums.RoleType;
import rs.edu.raf.IAMService.exceptions.EmailTakenException;
import rs.edu.raf.IAMService.exceptions.MissingRoleException;
import rs.edu.raf.IAMService.services.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateEmployee_Success() {
        // Setup
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setEmail("test@user.com");
        employeeDto.setUsername("test@user.com");

        // Mock result
        EmployeeDto resultEmployeeDto = new EmployeeDto();
        resultEmployeeDto.setId(1L);
        resultEmployeeDto.setEmail("test@user.com");
        resultEmployeeDto.setUsername("test@user.com");

        when(userService.createEmployee(employeeDto)).thenReturn(resultEmployeeDto);

        // Execution
        ResponseEntity<?> response = userController.createEmployee(employeeDto);

        // Assertion
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testCreateEmployee_EmailTaken() {
        // Setup
        EmployeeDto employeeDto = new EmployeeDto();
        when(userService.createEmployee(employeeDto)).thenThrow(new EmailTakenException("test@user.com"));

        // Execution
        ResponseEntity<?> response = userController.createEmployee(employeeDto);

        // Assertion
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(
                "User with email 'test@user.com' already exists",
                response.getBody()
        );
    }

    @Test
    public void testCreateEmployee_MissingRole() {
        // Setup
        EmployeeDto employeeDto = new EmployeeDto();
        when(userService.createEmployee(employeeDto)).thenThrow(new MissingRoleException("INVALID_ROLE"));

        // Execution
        ResponseEntity<?> response = userController.createEmployee(employeeDto);

        // Assertion
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(
                "Role of type 'INVALID_ROLE' not found!",
                response.getBody()
        );
    }
}