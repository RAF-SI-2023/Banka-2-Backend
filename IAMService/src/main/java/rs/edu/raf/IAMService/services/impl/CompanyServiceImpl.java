package rs.edu.raf.IAMService.services.impl;

import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

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

    public CompanyDto getCompanyById(Long id) {
        Optional<Company> company = companyRepository.findById(id);
        if (company.isPresent()) {
            return companyMapper.companyToCompanyDto(company.get());
        } else {
            throw new CompanyNotFoundException("Company with id " + id + " not found");
        }

    }

    @Override
    public CompanyDto getCompanyByPib(Long pib) {
        Optional<Company> company = companyRepository.findByPib(pib);
        if(company.isPresent()){
            return companyMapper.companyToCompanyDto(company.get());
        }else{
            throw new CompanyNotFoundException("Company with pib " + pib + " not found");
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

    @Override
    public void deleteCompanyById(Long id) {
        companyRepository.deleteById(id);
    }

    // Returns updated Company entity with only allowed modifications
    private Company getModifiedCompanyEntity(Company companyToModify, CompanyDto companyDto){
        companyToModify.setCompanyName(companyDto.getCompanyName());
        companyToModify.setFaxNumber(companyDto.getFaxNumber());
        companyToModify.setPhoneNumber(companyDto.getPhoneNumber());
        companyToModify.setActivityCode(companyDto.getActivityCode());

        return companyToModify;
    }

    public CompanyDto updateCompany(CompanyDto companyDto){
        var company = companyRepository.findById(companyDto.getId());
        if (!company.isPresent())
            throw new CompanyNotFoundException("Company with id " + companyDto.getId() + " not found");
        
        // save() return value may return incorrect data, so there is a need to call getModifiedCompanyEntity
        // example: Identification Number should not be updated, save() will return UPDATED value here
        Company modifiedCompany = getModifiedCompanyEntity(company.get(), companyDto);
        return companyMapper.companyToCompanyDto(companyRepository.save(modifiedCompany));
    }

}
