package rs.edu.raf.IAMService.unit.mapper;

import org.junit.jupiter.api.Test;
import rs.edu.raf.IAMService.data.dto.CompanyDto;
import rs.edu.raf.IAMService.data.entites.Company;
import rs.edu.raf.IAMService.mapper.CompanyMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CompanyMapperTest {

    private final CompanyMapper companyMapper = new CompanyMapper();

    @Test
    void companiesToCompanyDtos_Success() {
        // Create a list of companies for testing
        List<Company> companies = new ArrayList<>();
        companies.add(
                new Company(
                        1L,
                        "Company A",
                        "Fax A",
                        "Phone A",
                        "Address A",
                        13579L,
                        13579,
                        13579,
                        13579
                )
        );
        companies.add(
                new Company(
                        2L,
                        "Company B",
                        "Fax B",
                        "Phone B",
                        "Address B",
                        24680L,
                        24680,
                        24680,
                        24680
                )
        );

        // Call the mapper function
        List<CompanyDto> companyDtos = companyMapper.companiesToCompanyDtos(companies);

        // Assert the result
        assertEquals(2, companyDtos.size());

        assertEquals(1L, companyDtos.get(0).getId());
        assertEquals("Company A", companyDtos.get(0).getCompanyName());
        assertEquals("Fax A", companyDtos.get(0).getFaxNumber());
        assertEquals("Phone A", companyDtos.get(0).getPhoneNumber());
        assertEquals("Address A", companyDtos.get(0).getAddress());
        assertEquals(13579L, companyDtos.get(0).getPib());
        assertEquals(13579, companyDtos.get(0).getRegistryNumber());
        assertEquals(13579, companyDtos.get(0).getIdentificationNumber());
        assertEquals(13579, companyDtos.get(0).getActivityCode());

        assertEquals(2L, companyDtos.get(1).getId());
        assertEquals("Company B", companyDtos.get(1).getCompanyName());
        assertEquals("Fax B", companyDtos.get(1).getFaxNumber());
        assertEquals("Phone B", companyDtos.get(1).getPhoneNumber());
        assertEquals("Address B", companyDtos.get(1).getAddress());
        assertEquals(24680L, companyDtos.get(1).getPib());
        assertEquals(24680, companyDtos.get(1).getRegistryNumber());
        assertEquals(24680, companyDtos.get(1).getIdentificationNumber());
        assertEquals(24680, companyDtos.get(1).getActivityCode());
    }

    @Test
    void companyDtosToCompanies_Success() {
        // Create a list of company DTOs for testing
        List<CompanyDto> companyDtos = new ArrayList<>();
        companyDtos.add(
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
        companyDtos.add(
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

        // Call the mapper function
        List<Company> companies = companyMapper.companyDtosToCompanies(companyDtos);

        // Assert the result
        assertEquals(2, companies.size());

        assertEquals(1L, companies.get(0).getId());
        assertEquals("Company A", companies.get(0).getCompanyName());
        assertEquals("Fax A", companies.get(0).getFaxNumber());
        assertEquals("Phone A", companies.get(0).getPhoneNumber());
        assertEquals("Address A", companies.get(0).getAddress());
        assertEquals(13579L, companies.get(0).getPib());
        assertEquals(13579, companies.get(0).getRegistryNumber());
        assertEquals(13579, companies.get(0).getIdentificationNumber());
        assertEquals(13579, companies.get(0).getActivityCode());

        assertEquals(2L, companies.get(1).getId());
        assertEquals("Company B", companies.get(1).getCompanyName());
        assertEquals("Fax B", companies.get(1).getFaxNumber());
        assertEquals("Phone B", companies.get(1).getPhoneNumber());
        assertEquals("Address B", companies.get(1).getAddress());
        assertEquals(24680L, companies.get(1).getPib());
        assertEquals(24680, companies.get(1).getRegistryNumber());
        assertEquals(24680, companies.get(1).getIdentificationNumber());
        assertEquals(24680, companies.get(1).getActivityCode());
    }
}