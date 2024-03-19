package rs.edu.raf.IAMService.services;

import org.springframework.stereotype.Service;
import rs.edu.raf.IAMService.data.dto.CompanyDto;

import java.util.List;

@Service
public interface CompanyService {
    CompanyDto getCompanyById(Long id);

    CompanyDto createCompany(CompanyDto companyDto);

    List<CompanyDto> findAllCompanies();

    void deleteCompanyById(Long id);
}
