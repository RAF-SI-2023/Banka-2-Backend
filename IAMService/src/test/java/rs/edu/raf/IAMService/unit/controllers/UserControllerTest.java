package rs.edu.raf.IAMService.unit.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import rs.edu.raf.IAMService.controllers.UserController;
import rs.edu.raf.IAMService.data.dto.*;
import rs.edu.raf.IAMService.data.entites.Employee;
import rs.edu.raf.IAMService.data.entites.Permission;
import rs.edu.raf.IAMService.data.entites.User;
import rs.edu.raf.IAMService.data.enums.PermissionType;
import rs.edu.raf.IAMService.data.enums.RoleType;
import rs.edu.raf.IAMService.exceptions.EmailTakenException;
import rs.edu.raf.IAMService.exceptions.MissingRoleException;
import rs.edu.raf.IAMService.unit.TestSecurityConfig;
import rs.edu.raf.IAMService.exceptions.UserNotFoundException;
import rs.edu.raf.IAMService.filters.JwtFilter;
import rs.edu.raf.IAMService.jwtUtils.JwtUtil;
import rs.edu.raf.IAMService.services.UserService;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE, classes = {JwtFilter.class})
        })
@Import({TestSecurityConfig.class})
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserController userController;

    @MockBean
    private JwtUtil jwtUtil;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPrivateClient_happyFlow_returnsOk() throws Exception {
        PrivateClientDto clientDto = new PrivateClientDto();
        when(userService.createPrivateClient(any(PrivateClientDto.class)))
                .thenReturn(clientDto);

        mockMvc.perform(post("/api/users/public/create/private-client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void createCorporateClient_happyFlow_returnsOk() throws Exception {
        CorporateClientDto clientDto = new CorporateClientDto();
        when(userService.createCorporateClient(any(CorporateClientDto.class)))
                .thenReturn(clientDto);

        mockMvc.perform(post("/api/users/public/create/corporate-client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void activateClient_happyFlow_returnsOk() throws Exception {
        String email = "email";
        PasswordActivationDto activationDto = new PasswordActivationDto();
        activationDto.setPassword("newPassword");

        when(userService.passwordActivation(email, activationDto.getPassword()))
                .thenReturn(1L);

        mockMvc.perform(post("/api/users/public/password-activation/" + email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(activationDto)))
                .andExpect(status().isOk());
    }

    @Test
    void activateClient_userDoesNotExist_returnsNotFound() throws Exception {
        String email = "email";
        PasswordActivationDto activationDto = new PasswordActivationDto();
        activationDto.setPassword("newPassword");

        when(userService.passwordActivation(email, activationDto.getPassword()))
                .thenThrow(UserNotFoundException.class);

        mockMvc.perform(post("/api/users/public/password-activation/" + email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(activationDto)))
                .andExpect(status().isNotFound());
    }

//    @Test
//    void createAgent_happyFlow_returnsOk1() throws Exception {
//        AgentDto agentDto = new AgentDto();
//        when(userService.createAgent(any(AgentDto.class)))
//                .thenReturn(agentDto);
//
//        mockMvc.perform(post("/api/users/create/agent")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(agentDto)))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//    }

    @Test
    void createEmployee_happyFlow_returnsOk() {
        EmployeeDto employeeDto = new EmployeeDto();

        when(userService.createEmployee(Mockito.any(EmployeeDto.class))).thenReturn(employeeDto);

        ResponseEntity<?> responseEntity = userController.createEmployee(employeeDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(employeeDto.getId(), responseEntity.getBody());
        verify(userService, times(1)).createEmployee(employeeDto);
    }


    @Test
    void createEmployee_emailTaken_badRequest() {
        String email = "employee@gmail.com";
        EmployeeDto employeeDtoException = new EmployeeDto();
        employeeDtoException.setEmail(email);
        String message = String.format("User with email '%s' already exists", email);

        when(userService.createEmployee(employeeDtoException)).thenThrow(new EmailTakenException(email));

        ResponseEntity<?> responseEntity = userController.createEmployee(employeeDtoException);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(responseEntity.getBody(), message);
        verify(userService, times(1)).createEmployee(employeeDtoException);
    }

    @Test
    void createEmployee_missingRole_internalServer() {
        String role = RoleType.ADMIN.getRole();
        EmployeeDto employeeDtoException = new EmployeeDto();
        String message = String.format("Role of type '%s' not found!", role);

        when(userService.createEmployee(employeeDtoException)).thenThrow(new MissingRoleException(role));

        ResponseEntity<?> responseEntity = userController.createEmployee(employeeDtoException);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals(responseEntity.getBody(), message);
        verify(userService, times(1)).createEmployee(employeeDtoException);
    }


    @Test
    void createAgent_happyFlow_returnsOk() {
        AgentDto agentDto = new AgentDto();

        when(userService.createAgent(Mockito.any(AgentDto.class))).thenReturn(agentDto);

        ResponseEntity<?> responseEntity = userController.createAgent(agentDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(agentDto.getId(), responseEntity.getBody());
        verify(userService, times(1)).createAgent(agentDto);
    }

    @Test
    void createAgent_emailTaken_badRequest() {
        String email = "employee@gmail.com";
        AgentDto agentDto = new AgentDto();
        String message = String.format("User with email '%s' already exists", email);

        when(userService.createAgent(Mockito.any(AgentDto.class))).thenThrow(new EmailTakenException(email));

        ResponseEntity<?> responseEntity = userController.createAgent(agentDto);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(responseEntity.getBody(), message);
        verify(userService, times(1)).createAgent(agentDto);
    }

    @Test
    void createAgent_missingRole_internalServer() {
        String role = RoleType.ADMIN.getRole();
        AgentDto agentDtoException = new AgentDto();
        String message = String.format("Role of type '%s' not found!", role);

        when(userService.createAgent(agentDtoException)).thenThrow(new MissingRoleException(role));

        ResponseEntity<?> responseEntity = userController.createAgent(agentDtoException);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals(responseEntity.getBody(), message);
        verify(userService, times(1)).createAgent(agentDtoException);
    }

    @Test
    void initiatesChangePassword_happyFlow_returnsOk() {
        ChangePasswordDto changePasswordDto = new ChangePasswordDto();

        when(userService.setPassword(changePasswordDto.getEmail(), changePasswordDto.getPassword())).thenReturn(true);

        ResponseEntity<?> responseEntity = userController.initiatesChangePassword(changePasswordDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(true, responseEntity.getBody());
        verify(userService, times(1)).setPassword(changePasswordDto.getEmail(), changePasswordDto.getPassword());
    }


    @Test
    void findByEmail_happyFlow_returnsOk() {

        String email = "employee1@gmail.com";

        UserDto userDto = new UserDto();
        userDto.setEmail(email);

        when(userService.findByEmail(email)).thenReturn(userDto);

        ResponseEntity<?> responseEntity = userController.findByEmail(email);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertEquals(userDto, responseEntity.getBody());

        verify(userService, times(1)).findByEmail(email);

    }

    @Test
    void findById_happyFlow_returnsOk() {

        Long id = 1L;

        UserDto userDto = new UserDto();
        userDto.setId(id);

        when(userService.findById(id)).thenReturn(userDto);

        ResponseEntity<?> responseEntity = userController.findById(id);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userDto, responseEntity.getBody());

        verify(userService, times(1)).findById(id);
    }

    @Test
    public void testActivateEmployee_Success() {
        // Mock behavior
        int id = 123;
        Employee user = new Employee();
        user.setId(123L);
        user.setActive(true);
        when(userService.employeeActivation(id)).thenReturn(user);

        // Test
        ResponseEntity<Boolean> response = userController.activateEmployee(id);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Boolean.TRUE.equals(response.getBody()));
    }

    @Test
    public void testActivateEmployee_Failure() {
        // Mock behavior
        int id = 123;
        doThrow(new IllegalArgumentException()).when(userService).employeeActivation(id);

        // Test
        ResponseEntity<Boolean> response = userController.activateEmployee(id);

        // Verify
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody());
    }

    @Test
    public void testDeactivateEmployee_Success() {
        // Mock behavior
        int id = 123;
        Employee user = new Employee();
        user.setId(123L);
        user.setActive(true);
        when(userService.employeeDeactivation(id)).thenReturn(user);

        // Test
        ResponseEntity<Boolean> response = userController.deactivateEmployee(id);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    public void testDeactivateEmployee_Failure() {
        // Mock behavior
        int id = 123;
        doThrow(new IllegalArgumentException()).when(userService).employeeDeactivation(id);

        // Test
        ResponseEntity<Boolean> response = userController.deactivateEmployee(id);

        // Verify
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(response.getBody());
    }

    @Test
    public void testGetAgentsLeftLimit() {
        // Mock behavior
        Long id = 123L;
        BigDecimal limit = new BigDecimal("1000.00");
        when(userService.getAgentsLeftLimit(id)).thenReturn(limit);

        // Test
        ResponseEntity<BigDecimal> response = userController.getAgentsLeftLimit(id);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(limit, response.getBody());
    }

    @Test
    public void testResetAgentsLeftLimit() {
        // Mock behavior
        Long id = 123L;
        when(userService.getAgentsLeftLimit(id)).thenReturn(new BigDecimal("1000.00"));

        // Test
        ResponseEntity<Void> response = userController.resetAgentsLeftLimit(id);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDecreaseAgentLimit() {
        // Mock behavior
        Long id = 123L;
        Double amount = 50.0;
        when(userService.getAgentsLeftLimit(id)).thenReturn(new BigDecimal("1000.00"));

        // Test
        ResponseEntity<Void> response = userController.decreaseAgentLimit(id, amount);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void createCompanyEmployeeSuccess() {
        // Mock behavior
        CompanyEmployeeDto employeeDto = new CompanyEmployeeDto();
        employeeDto.setEmail("email");
        employeeDto.setRole(RoleType.USER);
        employeeDto.setPib(123L);
        when(userService.createCompanyEmployee(employeeDto)).thenReturn(employeeDto);

        // Test
        ResponseEntity<?> response = userController.createCompanyEmployee(employeeDto);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}

