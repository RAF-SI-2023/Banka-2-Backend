package rs.edu.raf.IAMService.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import rs.edu.raf.IAMService.data.dto.CompanyDto;
import rs.edu.raf.IAMService.data.entites.Company;
import rs.edu.raf.IAMService.mapper.CompanyMapper;
import rs.edu.raf.IAMService.services.CompanyService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.*;
import rs.edu.raf.IAMService.exceptions.CompanyNotFoundException;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CompanyControllerTest {

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
    void findAllCompanies_Success() throws Exception {
        // Mocking data
        List<CompanyDto> companies = new ArrayList<>();
        companies.add(
                new CompanyDto(
                        1L,
                        "Company A",
                        "Fax A",
                        "Phone A",
                        13579L,
                        13579,
                        13579,
                        13579,
                        "Address A"
                )
        );
        companies.add(
                new CompanyDto(
                        2L,
                        "Company B",
                        "Fax B",
                        "Phone B",
                        24680L,
                        24680,
                        24680,
                        24680,
                        "Address B"
                )
        );

        // Mocking service response
        when(companyService.findAllCompanies()).thenReturn(companies);

        // Perform GET request and validate response
        mockMvc.perform(get("/api/companies/find-all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2)) // Check if two companies are returned
                .andExpect(jsonPath("$[0].companyName").value("Company A")) // Check the first company name
                .andExpect(jsonPath("$[1].companyName").value("Company B")); // Check the second company name
    }

    @Test
    void findAllCompanies_Exception() throws Exception {
        // Mocking service to throw an exception
        when(companyService.findAllCompanies()).thenThrow(new RuntimeException("Some error occurred"));

        // Perform GET request and validate response
        mockMvc.perform(get("/api/companies/find-all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(content().string("Some error occurred")); // Check the error message
    }

    @Test
    void createCompany_Success() throws Exception {
        // Mocking request body
        CompanyDto companyDto = new CompanyDto(
                1L,
                "Company A",
                "Fax A",
                "Phone A",
                13579L,
                13579,
                13579,
                13579,
                "Address A"
        );

        when(companyService.createCompany(companyDto)).thenReturn(companyDto);

        // Perform POST request and validate response
        mockMvc.perform(post("/api/companies/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(companyDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.companyName").exists())
                .andExpect(jsonPath("$.faxNumber").exists())
                .andExpect(jsonPath("$.phoneNumber").exists())
                .andExpect(jsonPath("$.registryNumber").exists())
                .andExpect(jsonPath("$.identificationNumber").exists())
                .andExpect(jsonPath("$.activityCode").exists())
                .andExpect(jsonPath("$.address").exists());
    }

    @Test
    void createCompany_Exception() throws Exception {
        // Mocking request body
        CompanyDto companyDto = new CompanyDto(
                1L,
                "Company A",
                "Fax A",
                "Phone A",
                13579L,
                13579,
                13579,
                13579,
                "Address A"
        );

        // Mocking service to throw an exception
        when(companyService.createCompany(companyDto)).thenThrow(new RuntimeException("Some error occurred"));

        // Perform POST request and validate response
        mockMvc.perform(post("/api/companies/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(companyDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(content().string("Some error occurred")); // Check the error message
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
