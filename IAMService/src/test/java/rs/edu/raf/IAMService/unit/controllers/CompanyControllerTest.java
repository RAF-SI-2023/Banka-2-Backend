package rs.edu.raf.IAMService.unit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import rs.edu.raf.IAMService.controllers.CompanyController;
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
        mockMvc.perform(get("/api/companies/all")
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
        mockMvc.perform(get("/api/companies/all")
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
                .andExpect(status().is(404))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(content().string("Some error occurred")); // Check the error message (Same message always, does not look at Exception Message currently)
    }

    @Test
    void updateCompany_CompanyExists() throws Exception{
        CompanyDto companyDto = new CompanyDto(1L, "name", "num", "num", 1L, 1, 1, 1, "adr");

        //doReturn(companyDto).when(companyService).updateCompany(companyDto);
        when(companyService.updateCompany(companyDto)).thenReturn(companyDto);

        mockMvc.perform(put("/api/companies/update")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(companyDto)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
  
    @Test
    void updateCompany_CompanyDoesNotExist_throwsException() throws Exception{
        CompanyDto companyDto = new CompanyDto(1L, "name", "num", "num", 1L, 1, 1, 1, "adr");

        when(companyService.updateCompany(companyDto)).thenThrow(CompanyNotFoundException.class);

        mockMvc.perform(put("/api/companies/update")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(companyDto)))
        .andExpect(status().isNotFound());
    } 

    @Test
    void findCompanyById_Success() throws Exception {
        // Mocking data
        CompanyDto company = new CompanyDto(
                2L,
                "Company B",
                "Fax B",
                "Phone B",
                24680L,
                24680,
                24680,
                24680,
                "Address B"

        );

        when(companyService.getCompanyById(2L)).thenReturn(company);

        // Perform GET request and validate response
        mockMvc.perform(get("/api/companies/id/" + 2L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(9)) // Check if two companies are returned
                .andExpect(jsonPath("$.companyName").value("Company B")); // Check the second company name
    }

    @Test
    void findCompanyById_Exception() throws Exception {
        // Mocking service to throw an exception
        when(companyService.getCompanyById(1000L)).thenThrow(new RuntimeException("Some error occurred"));

        // Perform GET request and validate response
        mockMvc.perform(get("/api/companies/id/" + 1000L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(content().string("Some error occurred")); // Check the error message
    }

    @Test
    public void testDeleteCompanyById() throws Exception {
        Long id = Long.valueOf(1);

       doNothing().when(companyService).deleteCompanyById(id);

        ResponseEntity<?> responseEntity = companyController.deleteCompanyById(id);

        assert responseEntity != null;
        assert responseEntity.getStatusCode() == HttpStatus.OK;

        verify(companyService, times(1)).deleteCompanyById(id);
    }

    @Test
    public void testDeleteCompanyByIdentificationNumber() {
        Integer identificationNumber = 123456;

        // Mock behavior of companyService.deleteCompanyByIdentificationNumber method
        doNothing().when(companyService).deleteCompanyByIdentificationNumber(identificationNumber);

        // Call the controller method
        ResponseEntity<?> responseEntity = companyController.deleteCompanyByIdentificationNumber(identificationNumber);

        // Verify that the service method was called once with the correct identification number
        verify(companyService, times(1)).deleteCompanyByIdentificationNumber(identificationNumber);

        // Verify that the response entity is as expected
        assert responseEntity != null;
        assert responseEntity.getStatusCode() == HttpStatus.OK;
    }
    @Test
    public void testFindCompanyByIdentificationNumber_Success() {
        Integer identificationNumber = 123456;
        CompanyDto company = new CompanyDto(
                2L,
                "Company B",
                "Fax B",
                "Phone B",
                24680L,
                24680,
                123456,
                24680,
                "Address B"
        );
        // Mock behavior of companyService.getCompanyByIdNumber method
        when(companyService.getCompanyByIdNumber(identificationNumber)).thenReturn(company);

        // Call the controller method
        ResponseEntity<?> responseEntity = companyController.findCompanyByIdentificationNumber(identificationNumber);

        // Verify that the service method was called once with the correct identification number
        verify(companyService, times(1)).getCompanyByIdNumber(identificationNumber);

        // Verify that the response entity is as expected
        assert responseEntity != null;
        assert responseEntity.getStatusCode() == HttpStatus.OK;
        assert responseEntity.getBody().equals(company);
    }

    @Test
    public void testFindCompanyByIdentificationNumber_NotFound() {
        Integer identificationNumber = 123456;
        String errorMessage = "Company not found";
        // Mock behavior of companyService.getCompanyByIdNumber method to throw an exception
        when(companyService.getCompanyByIdNumber(identificationNumber)).thenThrow(new RuntimeException(errorMessage));

        // Call the controller method
        ResponseEntity<?> responseEntity = companyController.findCompanyByIdentificationNumber(identificationNumber);

        // Verify that the service method was called once with the correct identification number
        verify(companyService, times(1)).getCompanyByIdNumber(identificationNumber);

        // Verify that the response entity is as expected
        assert responseEntity != null;
        assert responseEntity.getStatusCode() == HttpStatus.NOT_FOUND;
        assert responseEntity.getBody().equals(errorMessage);
    }
}

