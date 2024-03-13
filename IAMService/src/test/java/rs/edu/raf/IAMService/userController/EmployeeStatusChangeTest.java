package rs.edu.raf.IAMService.userController;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import rs.edu.raf.IAMService.controllers.UserController;
import rs.edu.raf.IAMService.data.entites.Employee;
import rs.edu.raf.IAMService.data.entites.Role;
import rs.edu.raf.IAMService.data.entites.User;
import rs.edu.raf.IAMService.data.enums.RoleType;
import rs.edu.raf.IAMService.services.UserService;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

public class EmployeeStatusChangeTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testChangeActiveStatusOfEmployee_AdminSuccess() {
        // Arrange
        String adminEmail = "lol";
        String employeeEmail = "employeeEmail";

        User admin = new User();


        Role roleAdmin = new Role(RoleType.ADMIN);



        admin.setUsername("lol");
        admin.setPassword(passwordEncoder.encode("lol"));
        admin.setEmail("lol");
        admin.setAddress("lol");
        admin.setPhone("lol");
        admin.setPermissions(null);
        admin.setDateOfBirth(null);
        admin.setRole(roleAdmin);
        admin.setId(Long.valueOf(1));

        Employee employee = new Employee();
        employee.setEmail(employeeEmail);
        when(userService.findUserByEmail(adminEmail)).thenReturn(Optional.of(admin));
        when(userService.findUserByEmail(employeeEmail)).thenReturn(Optional.of(employee));

        // Act
        ResponseEntity<?> response = userController.changeActiveStatusOfEmployee(adminEmail, employeeEmail);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(employee.isActive());
        verify(userService, times(1)).updateEntity(employee);
    }

    @Test
    void testChangeActiveStatusOfEmployee_UserIsNotAdmin() {
        // Arrange
        String adminEmail = "lol";
        String employeeEmail = "employeeEmail";

        User admin = new User();


        Role roleAdmin = new Role(RoleType.USER);



        admin.setUsername("lol");
        admin.setPassword(passwordEncoder.encode("lol"));
        admin.setEmail("lol");
        admin.setAddress("lol");
        admin.setPhone("lol");
        admin.setPermissions(null);
        admin.setDateOfBirth(null);
        admin.setRole(roleAdmin);
        admin.setId(Long.valueOf(1));

        Employee employee = new Employee();
        employee.setEmail(employeeEmail);
        when(userService.findUserByEmail(adminEmail)).thenReturn(Optional.of(admin));
        when(userService.findUserByEmail(employeeEmail)).thenReturn(Optional.of(employee));

        // Act
        ResponseEntity<?> response = userController.changeActiveStatusOfEmployee(adminEmail, employeeEmail);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertFalse(employee.isActive());
        verify(userService, times(0)).updateEntity(employee);
    }

    @Test
    void testChangeActiveStatusOfEmployee_UserIsNotEmployee() {
        // Arrange
        String adminEmail = "lol";
        String employeeEmail = "employeeEmail";

        User admin = new User();


        Role roleAdmin = new Role(RoleType.ADMIN);
        Role roleEmployee= new Role(RoleType.USER);


        admin.setUsername("lol");
        admin.setPassword(passwordEncoder.encode("lol"));
        admin.setEmail("lol");
        admin.setAddress("lol");
        admin.setPhone("lol");
        admin.setPermissions(null);
        admin.setDateOfBirth(null);
        admin.setRole(roleAdmin);
        admin.setId(Long.valueOf(1));

        User employee = new User();
        employee.setUsername("employee");
        employee.setPassword(passwordEncoder.encode("employee"));
        employee.setEmail("employeeEmail");
        employee.setAddress("employee");
        employee.setPhone("employee");
        employee.setPermissions(null);
        employee.setDateOfBirth(null);
        employee.setRole(roleEmployee);
        employee.setId(Long.valueOf(4));


        when(userService.findUserByEmail(adminEmail)).thenReturn(Optional.of(admin));
        when(userService.findUserByEmail(employeeEmail)).thenReturn(Optional.of(employee));

        // Act
        ResponseEntity<?> response = userController.changeActiveStatusOfEmployee(adminEmail, employeeEmail);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        verify(userService, times(0)).updateEntity(employee);
    }

    @Test
    void testChangeActiveStatusOfEmployee_UserEmployeeIsNotFound() {
        // Arrange
        String adminEmail = "lol";
        String employeeEmail = "employeeEmail";

        User admin = new User();


        Role roleAdmin = new Role(RoleType.ADMIN);
        Role roleEmployee= new Role(RoleType.USER);


        admin.setUsername("lol");
        admin.setPassword(passwordEncoder.encode("lol"));
        admin.setEmail("lol");
        admin.setAddress("lol");
        admin.setPhone("lol");
        admin.setPermissions(null);
        admin.setDateOfBirth(null);
        admin.setRole(roleAdmin);
        admin.setId(Long.valueOf(1));

        User employee = new User();
        employee.setUsername("employee");
        employee.setPassword(passwordEncoder.encode("employee"));
        employee.setEmail("employeeEmail");
        employee.setAddress("employee");
        employee.setPhone("employee");
        employee.setPermissions(null);
        employee.setDateOfBirth(null);
        employee.setRole(roleEmployee);
        employee.setId(Long.valueOf(4));


        when(userService.findUserByEmail(adminEmail)).thenReturn(Optional.of(admin));
        when(userService.findUserByEmail(employeeEmail)).thenReturn(null);

        // Act
        ResponseEntity<?> response = userController.changeActiveStatusOfEmployee(adminEmail, null);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(userService, times(0)).updateEntity(employee);
    }



}
