package rs.edu.raf.IAMService.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.IAMService.data.dto.CompanyDto;
import rs.edu.raf.IAMService.data.entites.Company;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CompanyMapper {
    public CompanyDto companyToCompanyDto(Company company) {
        return new CompanyDto(
                company.getId(),
                company.getCompanyName(),
                company.getFaxNumber(),
                company.getPhoneNumber(),
                company.getPib(),
                company.getRegistryNumber(),
                company.getIdentificationNumber(),
                company.getActivityCode(),
                company.getAddress())
                ;
    }

    public Company companyDtoToCompany(CompanyDto companyDto) {
        return new Company(
                companyDto.getId(),
                companyDto.getCompanyName(),
                companyDto.getFaxNumber(),
                companyDto.getPhoneNumber(),
                companyDto.getAddress(),
                companyDto.getPib(),
                companyDto.getRegistryNumber(),
                companyDto.getIdentificationNumber(),
                companyDto.getActivityCode());
    }

    public List<CompanyDto> companiesToCompanyDtos(List<Company> companies) {
        return companies.stream()
                .map(this::companyToCompanyDto)
                .collect(Collectors.toList());
    }

    public List<Company> companyDtosToCompanies(List<CompanyDto> companyDtos) {
        return companyDtos.stream()
                .map(this::companyDtoToCompany)
                .collect(Collectors.toList());
    }
}
