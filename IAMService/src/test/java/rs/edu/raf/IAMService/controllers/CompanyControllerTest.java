package rs.edu.raf.IAMService.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.*;

import rs.edu.raf.IAMService.data.dto.CompanyDto;
import rs.edu.raf.IAMService.data.entites.Company;
import rs.edu.raf.IAMService.exceptions.CompanyNotFoundException;
import rs.edu.raf.IAMService.services.CompanyService;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import rs.edu.raf.IAMService.mapper.CompanyMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CompanyControllerTest {
    private MockMvc mockMvc;

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private CompanyController companyController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(companyController).build();
    }

    @Test
    void updateCompany_CompanyExists() throws Exception{
        CompanyDto companyDto = new CompanyDto(1L, "name", "num", "num", 1L, 1, 1, 1, "adr");

        doReturn(companyDto).when(companyService).updateCompany(companyDto);

        mockMvc.perform(put("/api/companies/update-company")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(companyDto)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    @Test
    void updateCompany_CompanyDoesNotExist_throwsException() throws Exception{
        CompanyDto companyDto = new CompanyDto(1L, "name", "num", "num", 1L, 1, 1, 1, "adr");

        doThrow(CompanyNotFoundException.class).when(companyService).updateCompany(companyDto);

        mockMvc.perform(put("/api/companies/update-company")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(companyDto)))
        .andExpect(status().isNotFound());
    } 
}
