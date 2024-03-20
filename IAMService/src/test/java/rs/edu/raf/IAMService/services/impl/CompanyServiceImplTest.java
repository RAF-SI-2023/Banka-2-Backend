package rs.edu.raf.IAMService.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.IAMService.data.dto.CompanyDto;
import rs.edu.raf.IAMService.data.entites.Company;
import rs.edu.raf.IAMService.exceptions.CompanyNotFoundException;
import rs.edu.raf.IAMService.mapper.CompanyMapper;
import rs.edu.raf.IAMService.repositories.CompanyRepository;
import rs.edu.raf.IAMService.services.CompanyService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private CompanyMapper companyMapper;

    @InjectMocks
    private CompanyServiceImpl companyService;

    @Test
    void createCompany_Success() {
        // Mocking data
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

        Company company = new Company(
                1L,
                "Company A",
                "Fax A",
                "Phone A",
                "Address A",
                13579L,
                13579,
                13579,
                13579
        );

        // Mocking repository save method
        when(companyRepository.save(any(Company.class))).thenReturn(company);

        // Mocking mapper
        when(companyMapper.companyDtoToCompany(companyDto)).thenReturn(company);
        when(companyMapper.companyToCompanyDto(company)).thenReturn(companyDto);

        // Call the service method
        CompanyDto createdCompanyDto = companyService.createCompany(companyDto);

        // Verify interactions
        verify(companyMapper, times(1)).companyDtoToCompany(companyDto);
        verify(companyRepository, times(1)).save(company);
        verify(companyMapper, times(1)).companyToCompanyDto(company);

        // Assert the result
        assertEquals(companyDto, createdCompanyDto);
    }

    @Test
    void findAllCompanies_Success() {
        // Mocking data
        List<Company> companies = new ArrayList<>();

        // Mocking repository findAll method
        when(companyRepository.findAll()).thenReturn(companies);

        // Mocking mapper
        when(companyMapper.companiesToCompanyDtos(companies)).thenReturn(new ArrayList<>());

        // Call the service method
        List<CompanyDto> companyDtos = companyService.findAllCompanies();

        // Verify interactions
        verify(companyRepository, times(1)).findAll();
        verify(companyMapper, times(1)).companiesToCompanyDtos(companies);

        // Assert the result
        assertEquals(0, companyDtos.size()); // Assuming no companies are returned
    }
    @Test
    void getCompanyById_Success() {
        // Mocking data
        Optional<Company> company = Optional.of(new Company());
        // Mocking repository findAll method
        when(companyRepository.findById(2L)).thenReturn(company);

        // Mocking mapper
        when(companyMapper.companyToCompanyDto(company.get())).thenReturn(new CompanyDto());
        // Call the service method
        CompanyDto companyDto = companyService.getCompanyById(2L);
        // Verify interactions
        verify(companyRepository, times(1)).findById(2L);
        verify(companyMapper, times(1)).companyToCompanyDto(company.get());

        // Assert the result
        assertNull(companyDto.getId());// Assuming no companies are returned
    }

    @Test
    void getCompanyById_Exception() {
        // Mocking data
        Optional<Company> company = Optional.empty();
        // Mocking repository findAll method
        when(companyRepository.findById(2L)).thenReturn(company);

        // Call the service method
        Exception exception = assertThrows(CompanyNotFoundException.class, () -> {
            CompanyDto companyDto = companyService.getCompanyById(2L);
        });
        String expectedMessage = "Company with id " + 2L + " not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }
}