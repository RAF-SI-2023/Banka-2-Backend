package rs.edu.raf.IAMService.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
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
import org.springframework.test.web.servlet.MockMvc;
import rs.edu.raf.IAMService.TestSecurityConfig;
import rs.edu.raf.IAMService.data.dto.PasswordActivationDto;
import rs.edu.raf.IAMService.data.dto.CorporateClientDto;
import rs.edu.raf.IAMService.data.dto.EmployeeDto;
import rs.edu.raf.IAMService.data.dto.PrivateClientDto;
import rs.edu.raf.IAMService.exceptions.EmailTakenException;
import rs.edu.raf.IAMService.exceptions.MissingRoleException;
import rs.edu.raf.IAMService.exceptions.UserNotFoundException;
import rs.edu.raf.IAMService.filters.JwtFilter;
import rs.edu.raf.IAMService.jwtUtils.JwtUtil;
import rs.edu.raf.IAMService.services.UserService;
import rs.edu.raf.IAMService.utils.ChangedPasswordTokenUtil;
import rs.edu.raf.IAMService.utils.SubmitLimiter;
import rs.edu.raf.IAMService.validator.PasswordValidator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE, classes = {JwtFilter.class})
        })
@Import({TestSecurityConfig.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private ChangedPasswordTokenUtil changedPasswordTokenUtil;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private SubmitLimiter submitLimiter;

    @MockBean
    private PasswordValidator passwordValidator;

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

        mockMvc.perform(post("/api/users/public/private-client")
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

        mockMvc.perform(post("/api/users/public/corporate-client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void activateClient_happyFlow_returnsOk() throws Exception {
        String clientId = "1";
        PasswordActivationDto activationDto = new PasswordActivationDto();
        activationDto.setPassword("newPassword");

        when(userService.passwordActivation(clientId, activationDto.getPassword()))
                .thenReturn(Long.valueOf(clientId));

        mockMvc.perform(patch("/api/users/public/" + clientId + "/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(activationDto)))
                .andExpect(status().isOk());
    }

    @Test
    void activateClient_userDoesNotExist_returnsNotFound() throws Exception {
        String clientId = "non existing id";
        PasswordActivationDto activationDto = new PasswordActivationDto();
        activationDto.setPassword("newPassword");

        when(userService.passwordActivation(clientId, activationDto.getPassword()))
                .thenThrow(UserNotFoundException.class);

        mockMvc.perform(patch("/api/users/public/" + clientId + "/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(activationDto)))
                .andExpect(status().isNotFound());
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