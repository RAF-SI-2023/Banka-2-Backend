package rs.edu.raf.IAMService.services.impl;

import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import rs.edu.raf.IAMService.data.dto.CompanyDto;
import rs.edu.raf.IAMService.data.entites.Company;
import rs.edu.raf.IAMService.exceptions.CompanyNotFoundException;
import rs.edu.raf.IAMService.mapper.CompanyMapper;
import rs.edu.raf.IAMService.repositories.CompanyRepository;
import rs.edu.raf.IAMService.services.CompanyService;

import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    public CompanyServiceImpl(CompanyRepository companyRepository, CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
    }

    public CompanyDto getCompanyById(Long id) {
        Optional<Company> company = companyRepository.findById(id);
        if (company.isPresent()) {
            return companyMapper.companyToCompanyDto(company.get());
        } else {
            throw new CompanyNotFoundException("Company with id " + id + " not found");
        }

    }

    public CompanyDto updateCompany(CompanyDto companyDto){
        if (!companyRepository.findById(companyDto.getId()).isPresent())
            throw new CompanyNotFoundException("Company with id " + companyDto.getId() + " not found");
        Company company = companyMapper.companyDtoToCompany(companyDto);

        // save will not modify non updatable attributes and will return correct entity
        return companyMapper.companyToCompanyDto(companyRepository.save(company));
    }

}
