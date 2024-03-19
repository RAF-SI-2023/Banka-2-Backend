package rs.edu.raf.IAMService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rs.edu.raf.IAMService.data.dto.CompanyDto;
import rs.edu.raf.IAMService.data.entites.Company;
import rs.edu.raf.IAMService.exceptions.CompanyNotFoundException;
import rs.edu.raf.IAMService.mapper.CompanyMapper;
import rs.edu.raf.IAMService.repositories.CompanyRepository;
import rs.edu.raf.IAMService.services.impl.CompanyServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class CompanyServiceCrudTests {
    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private CompanyMapper companyMapper;

    @InjectMocks
    CompanyServiceImpl companyService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByPib_CompanyFound_ReturnsCompanyDto(){
        Long companyPib = 123456789L;
        Company company = new Company();
        company.setPib(companyPib);
        company.setCompanyName("Test company");
        CompanyDto companyDto = new CompanyDto();
        companyDto.setPib(companyPib);
        companyDto.setCompanyName(company.getCompanyName());
        when(companyRepository.findByPib(companyPib)).thenReturn(Optional.of(company));
        when(companyMapper.companyToCompanyDto(company)).thenReturn(companyDto);

        CompanyDto companyDto1 = companyService.getCompanyByPib(companyPib);

        assertEquals(company.getPib(), companyDto1.getPib());
        assertEquals(company.getCompanyName(), companyDto1.getCompanyName());
    }

    @Test
    public void testFindByPib_CompanyNotFound_ThrowsCompanyNotFoundException(){
        Long companyPib = 123456789L;
        when(companyRepository.findByPib(companyPib)).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class, () -> companyService.getCompanyByPib(companyPib));
    }
}
