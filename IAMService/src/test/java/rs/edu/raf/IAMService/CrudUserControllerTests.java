package rs.edu.raf.IAMService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rs.edu.raf.IAMService.controllers.UserController;
import rs.edu.raf.IAMService.data.dto.UserDto;
import rs.edu.raf.IAMService.data.enums.RoleType;
import rs.edu.raf.IAMService.services.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CrudUserControllerTests {

    @Mock
    HttpServletRequest request;
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController controller;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindByEmail_Success() {
        // Arrange
        String email = "test@example.com";
        UserDto expectedUser = new UserDto();
        expectedUser.setUsername("lol");
        expectedUser.setEmail("lol");
        expectedUser.setAddress("lol");
        expectedUser.setPhone("lol");
        expectedUser.setPermissions(null);
        expectedUser.setDateOfBirth(null);
        expectedUser.setRole(RoleType.ROLE_ADMIN);


        when(userService.findByEmail(email)).thenReturn(expectedUser);

        // Act
        ResponseEntity<?> response = controller.findByEmail(email);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedUser, response.getBody());
    }

    @Test
    public void testFindByEmail_NotFound() {
        // Arrange
        String email = "nonexistent@example.com";
        when(userService.findByEmail(email)).thenReturn(null);

        // Act
        ResponseEntity<?> response = controller.findByEmail(email);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testFindById_Success() {
        // Arrange
        String email = "test@example.com";
        UserDto expectedUser = new UserDto();
        expectedUser.setId(1L);
        expectedUser.setUsername("lol");
        expectedUser.setEmail("lol");
        expectedUser.setAddress("lol");
        expectedUser.setPhone("lol");
        expectedUser.setPermissions(null);
        expectedUser.setDateOfBirth(null);
        expectedUser.setRole(RoleType.ROLE_ADMIN);


        when(userService.findById(1L)).thenReturn(expectedUser);

        // Act
        ResponseEntity<?> response = controller.findById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedUser, response.getBody());
    }

    @Test
    public void testFindById_NotFound() {
        // Arrange
        Long id = 5L;
        when(userService.findById(id)).thenReturn(null);

        // Act
        ResponseEntity<?> response = controller.findById(id);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteUserByEmail_AdminRole_Success() {
        // Arrange
        String email = "admin@example.com";
        UserDto user = new UserDto();
        user.setEmail(email);
        Claims claims = mock(Claims.class);
        when(claims.get("role")).thenReturn(RoleType.ROLE_ADMIN.name());
        UserController controller = Mockito.spy(this.controller);
        //     when(controller.getClaims(request)).Return(claims);
        doReturn(claims).when(controller).getClaims(request);
        when(userService.deleteUserByEmail(email)).thenReturn(user);

        // Act
        ResponseEntity<?> response = controller.deleteUserByEmail(email);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user.getEmail(), ((UserDto) response.getBody()).getEmail());
    }

    @Test
    public void testDeleteUserByEmail_EmployeeRole_DeleteOwnAccount_Success() {
        // Arrange
        String email = "employee@example.com";
        UserDto user = new UserDto();
        user.setRole(RoleType.ROLE_EMPLOYEE);
        user.setEmail(email);
        Claims claims = mock(Claims.class);
        when(claims.get("role")).thenReturn(RoleType.ROLE_EMPLOYEE.name());
        when(claims.get("email")).thenReturn(email);
        UserController controller = Mockito.spy(this.controller);
        doReturn(claims).when(controller).getClaims(request);
        when(userService.deleteUserByEmail(email)).thenReturn(user);
        when(userService.findByEmail(email)).thenReturn(user);
        // Act
        ResponseEntity<?> response = controller.deleteUserByEmail(email);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user.getEmail(), ((UserDto) response.getBody()).getEmail());
    }

    @Test
    public void testDeleteUserByEmail_Unauthorized_NotAuthenticated() {
        // Arrange
        String email = "unauthorized@example.com";
        UserController controller = Mockito.spy(this.controller);
        when(controller.getClaims(request)).thenReturn(null); // Simulate unauthorized request
        // Act
        ResponseEntity<?> response = controller.deleteUserByEmail(email);
        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testDeleteUserByEmail_EmployeeRole_DeleteAnotherUserAccount_SUCCESS() {
        // Arrange
        String email = "test@example.com";
        UserDto user = new UserDto();
        user.setRole(RoleType.ROLE_USER);
        user.setEmail(email);
        Claims claims = mock(Claims.class);
        when(claims.get("role")).thenReturn(RoleType.ROLE_EMPLOYEE.name());
        when(claims.get("email")).thenReturn("employee@example.com");
        UserController controller = Mockito.spy(this.controller);
        doReturn(claims).when(controller).getClaims(request);
        when(userService.findByEmail(email)).thenReturn(user);
        when(userService.deleteUserByEmail(email)).thenReturn(user);

        // Act
        ResponseEntity<?> response = controller.deleteUserByEmail(email);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());


    }

    @Test
    public void testDeleteUserByEmail_EmployeeRole_DeleteAdminAccount_FORBIDDEN() {
        // Arrange
        String email = "admin@example.com";
        UserDto user = new UserDto();
        user.setRole(RoleType.ROLE_ADMIN);
        user.setEmail(email);
        Claims claims = mock(Claims.class);
        when(claims.get("role")).thenReturn(RoleType.ROLE_EMPLOYEE.name());
        when(claims.get("email")).thenReturn("employee@example.com");
        UserController controller = Mockito.spy(this.controller);
        doReturn(claims).when(controller).getClaims(request);
        when(userService.findByEmail(email)).thenReturn(user);
        when(userService.deleteUserByEmail(email)).thenThrow(new RuntimeException("User with role ADMIN cannot be deleted by EMPLOYEE"));

        // Act
        ResponseEntity<?> response = controller.deleteUserByEmail(email);
        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void testDeleteUserByEmail_UserRole_DeleteAnotherUser_FORBIDDEN() {
        // Arrange
        String email = "user@example.com";
        UserDto user = new UserDto();
        user.setRole(RoleType.ROLE_USER);
        user.setEmail(email);
        Claims claims = mock(Claims.class);
        when(claims.get("role")).thenReturn(RoleType.ROLE_USER.name());
        when(claims.get("email")).thenReturn("user2@example.com");
        UserController controller = Mockito.spy(this.controller);
        doReturn(claims).when(controller).getClaims(request);
        when(userService.findByEmail(email)).thenReturn(user);
        when(userService.deleteUserByEmail(email)).thenThrow(new RuntimeException("User with role ADMIN cannot be deleted by EMPLOYEE"));

        // Act
        ResponseEntity<?> response = controller.deleteUserByEmail(email);
        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void testUpdateUser_Unauthorized_ReturnsUnauthorized() {
        // Arrange
        when(controller.getClaims(request)).thenReturn(null);

        // Act
        ResponseEntity<?> response = controller.updateUser(new UserDto());

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testUpdateUser_UserRole_UpdateOwnInformation_Success() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmail("user@example.com");
        userDto.setRole(RoleType.ROLE_USER);
        userDto.setId(1L);
        userDto.setUsername("user");
        Claims claims = mock(Claims.class);
        when(claims.get("role")).thenReturn(RoleType.ROLE_USER.name());
        when(claims.get("email")).thenReturn("user@example.com");
        UserController controller = Mockito.spy(this.controller);
        doReturn(claims).when(controller).getClaims(request);
        when(userService.findByEmail("user@example.com")).thenReturn(userDto);
        when(userService.updateUser(userDto)).thenReturn(userDto);

        // Act
        ResponseEntity<?> response = controller.updateUser(userDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdateUser_EmployeeRole_UpdateAnotherUserInformation_Success() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmail("anotheruser@example.com");
        userDto.setRole(RoleType.ROLE_USER);
        userDto.setId(1L);
        userDto.setUsername("user");
        UserController controller = Mockito.spy(this.controller);
        Claims claims = mock(Claims.class);
        when(claims.get("email")).thenReturn("employee@example.com");
        when(claims.get("role")).thenReturn(RoleType.ROLE_EMPLOYEE.name());
        when(controller.getClaims(request)).thenReturn(claims);
        when(userService.findByEmail("anotheruser@example.com")).thenReturn(userDto);
        when(userService.updateUser(userDto)).thenReturn(userDto);

        // Act
        ResponseEntity<?> response = controller.updateUser(userDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdateUser_AdminRole_UpdateUserInformation_Success() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmail("anotheruser@example.com");
        userDto.setRole(RoleType.ROLE_USER);
        userDto.setId(1L);
        userDto.setUsername("user");
        UserController controller = Mockito.spy(this.controller);
        Claims claims = mock(Claims.class);
        when(claims.get("role")).thenReturn(RoleType.ROLE_ADMIN.name());
        when(controller.getClaims(request)).thenReturn(claims);
        when(userService.findByEmail("anotheruser@example.com")).thenReturn(userDto);
        when(userService.updateUser(userDto)).thenReturn(userDto);

        // Act
        ResponseEntity<?> response = controller.updateUser(userDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdateUser_UserRole_UpdateAnotherUserInformation_Unauthorized() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmail("anotheruser@example.com");
        userDto.setRole(RoleType.ROLE_USER);
        userDto.setId(1L);
        userDto.setUsername("user");
        UserController controller = Mockito.spy(this.controller);
        Claims claims = mock(Claims.class);
        when(claims.get("role")).thenReturn(RoleType.ROLE_USER.name());
        when(claims.get("email")).thenReturn("user@example.com"); // Different user's email
        when(controller.getClaims(request)).thenReturn(claims);

        // Act
        ResponseEntity<?> response = controller.updateUser(userDto);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void testUpdateUser_EmployeeRole_UpdateUserInformation_NoPermissions() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmail("anotheruser@example.com");
        userDto.setRole(RoleType.ROLE_ADMIN);
        userDto.setId(1L);
        userDto.setUsername("user");
        UserController controller = Mockito.spy(this.controller);
        Claims claims = mock(Claims.class);
        when(claims.get("role")).thenReturn(RoleType.ROLE_EMPLOYEE.name());
        when(controller.getClaims(request)).thenReturn(claims);
        when(userService.findByEmail("user@example.com")).thenReturn(userDto);

        // Act
        ResponseEntity<?> response = controller.updateUser(userDto);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void testUpdateUser_AdminRole_ValidationChecksFail() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmail("anotheruser@example.com");
        userDto.setRole(RoleType.ROLE_USER);
        userDto.setId(1L);
        userDto.setUsername("user");

        UserController controller = Mockito.spy(this.controller);
        Claims claims = mock(Claims.class);
        when(claims.get("role")).thenReturn(RoleType.ROLE_ADMIN.name());
        when(controller.getClaims(request)).thenReturn(claims);
        when(userService.findByEmail("anotheruser@example.com")).thenReturn(userDto);
        //simulate validation check fail even tho its the same user
        when(controller.validationCheck(userDto, userDto)).thenReturn(false);

        // Act
        ResponseEntity<?> response = controller.updateUser(userDto);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }


}


