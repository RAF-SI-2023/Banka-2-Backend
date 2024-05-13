package rs.edu.raf.IAMService.services;

import org.springframework.stereotype.Service;
import rs.edu.raf.IAMService.data.dto.CompanyDto;

import java.util.List;

@Service
public interface CompanyService {
    void deleteCompanyByIdentificationNumber(Integer identificationNumber);

    CompanyDto getCompanyById(Long id);

    CompanyDto getCompanyByPib(Long pib);

    CompanyDto createCompany(CompanyDto companyDto);

    List<CompanyDto> findAllCompanies();

    void deleteCompanyById(Long id);

    CompanyDto getCompanyByIdNumber(Integer idNumber);

    CompanyDto updateCompany(CompanyDto companyDto);

    CompanyDto getCompanyByName(String name);
}
