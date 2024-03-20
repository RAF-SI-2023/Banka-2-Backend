package rs.edu.raf.IAMService.services.impl;

import org.springframework.stereotype.Service;
import rs.edu.raf.IAMService.data.dto.CompanyDto;
import rs.edu.raf.IAMService.data.entites.Company;
import rs.edu.raf.IAMService.exceptions.CompanyNotFoundException;
import rs.edu.raf.IAMService.mapper.CompanyMapper;
import rs.edu.raf.IAMService.repositories.CompanyRepository;
import rs.edu.raf.IAMService.services.CompanyService;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    public CompanyServiceImpl(CompanyRepository companyRepository, CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
    }

    @Override
    public void deleteCompanyByRegistryNumber(String registrationNumber) {
        companyRepository.deleteByregistryNumber(registrationNumber);
    }

    public CompanyDto getCompanyById(Long id) {
        Optional<Company> company = companyRepository.findById(id);
        if (company.isPresent()) {
            return companyMapper.companyToCompanyDto(company.get());
        } else {
            throw new CompanyNotFoundException("Company with id " + id + " not found");
        }

    }

    @Override
    public CompanyDto createCompany(CompanyDto companyDto) {
        Company company = companyMapper.companyDtoToCompany(companyDto);
        company = companyRepository.save(company);
        return companyMapper.companyToCompanyDto(company);
    }

    @Override
    public List<CompanyDto> findAllCompanies() {
        return companyMapper.companiesToCompanyDtos(
                companyRepository.findAll()
        );
    }

}
