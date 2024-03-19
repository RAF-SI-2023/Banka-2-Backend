package rs.edu.raf.IAMService.services.impl;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import java.util.Optional;
import rs.edu.raf.IAMService.data.dto.CompanyDto;
import rs.edu.raf.IAMService.data.entites.Company;
import rs.edu.raf.IAMService.exceptions.CompanyNotFoundException;
import rs.edu.raf.IAMService.mapper.CompanyMapper;
import rs.edu.raf.IAMService.repositories.CompanyRepository;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceImplTest {
    
    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private CompanyMapper companyMapper;

    @InjectMocks
    private CompanyServiceImpl companyService; 

    @Test
    void updateCompany_CompanyExists(){
        Company company = new Company(1L, "name", "num", "num", "adr", 1L, 1, 1, 1);
        CompanyDto companyDto = new CompanyDto(1L, "name", "num", "num", 1L, 1, 1, 1, "adr");

        doReturn(Optional.of(company)).when(companyRepository).findById(1L);
        doReturn(company).when(companyRepository).save(any());
        doReturn(companyDto).when(companyMapper).companyToCompanyDto(company);

        var res = companyService.updateCompany(companyDto);

        assertEquals(company.getId(), res.getId());
        verify(companyRepository).findById(anyLong());
        verify(companyRepository).save(any());
    }

    @Test
    void updateCompany_CompanyDoesNotExist_throwException(){
        Company company = new Company(1L, "a", "a", "a", "a", 1L, 1, 1, 1);
        CompanyDto companyDto = new CompanyDto(1L, "a", "a", "a", 1L, 1, 1, 1, "a");

        doReturn(Optional.ofNullable(null)).when(companyRepository).findById(1L);

        assertThrows(CompanyNotFoundException.class, () -> companyService.updateCompany(companyDto));
        verify(companyRepository).findById(anyLong());
    }

}
