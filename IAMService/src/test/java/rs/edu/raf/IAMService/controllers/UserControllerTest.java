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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import rs.edu.raf.IAMService.TestSecurityConfig;
import rs.edu.raf.IAMService.data.dto.ClientActivationDto;
import rs.edu.raf.IAMService.data.dto.CorporateClientDto;
import rs.edu.raf.IAMService.data.dto.PrivateClientDto;
import rs.edu.raf.IAMService.exceptions.UserNotFoundException;
import rs.edu.raf.IAMService.filters.JwtFilter;
import rs.edu.raf.IAMService.services.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import({TestSecurityConfig.class})
@WebMvcTest(controllers = UserController.class,
        excludeFilters = {
            @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtFilter.class)
        })
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

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
        ClientActivationDto activationDto = new ClientActivationDto();
        activationDto.setPassword("newPassword");

        when(userService.activateClient(clientId, activationDto.getPassword()))
                .thenReturn(Long.valueOf(clientId));

        mockMvc.perform(patch("/api/users/public/" + clientId + "/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(activationDto)))
                .andExpect(status().isOk());
    }

    @Test
    void activateClient_userDoesNotExist_returnsNotFound() throws Exception {
        String clientId = "non existing id";
        ClientActivationDto activationDto = new ClientActivationDto();
        activationDto.setPassword("newPassword");

        when(userService.activateClient(clientId, activationDto.getPassword()))
                .thenThrow(UserNotFoundException.class);

        mockMvc.perform(patch("/api/users/public/" + clientId + "/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(activationDto)))
                .andExpect(status().isNotFound());
    }
}