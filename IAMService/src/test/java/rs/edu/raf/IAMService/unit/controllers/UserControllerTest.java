package rs.edu.raf.IAMService.unit.controllers;

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
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import rs.edu.raf.IAMService.controllers.UserController;
import rs.edu.raf.IAMService.unit.TestSecurityConfig;
import rs.edu.raf.IAMService.data.dto.AgentDto;
import rs.edu.raf.IAMService.data.dto.CorporateClientDto;
import rs.edu.raf.IAMService.data.dto.PasswordActivationDto;
import rs.edu.raf.IAMService.data.dto.PrivateClientDto;
import rs.edu.raf.IAMService.exceptions.UserNotFoundException;
import rs.edu.raf.IAMService.filters.JwtFilter;
import rs.edu.raf.IAMService.jwtUtils.JwtUtil;
import rs.edu.raf.IAMService.services.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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

    @Test
    void createAgent_happyFlow_returnsOk() throws Exception {
//        AgentDto agentDto = new AgentDto();
//        when(userService.createAgent(any(AgentDto.class)))
//                .thenReturn(agentDto);
//
//        mockMvc.perform(post("/api/users/create/agent")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(agentDto)))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

}