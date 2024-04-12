package rs.edu.raf.IAMService.integration.users.company;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.webjars.NotFoundException;
import rs.edu.raf.IAMService.data.dto.CompanyDto;
import rs.edu.raf.IAMService.exceptions.CompanyNotFoundException;
import rs.edu.raf.IAMService.repositories.CompanyRepository;
import rs.edu.raf.IAMService.services.CompanyService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CompanyServiceTestsSteps extends CompanyServiceTestsConfig {
    @Autowired
    CompanyService companyService;

    @Autowired
    CompanyRepository companyRepository;
    List<CompanyDto> companyDtoList;
    Long companyId;
    CompanyDto companyDto;
    Long createdId;

    @When("fetching all companies")
    public void fetchingAllCompanies() {
        companyDtoList = companyService.findAllCompanies();
    }

    @Then("return list of companies")
    public void returnListOfCompanies() {
        assertNotNull(companyDtoList);
    }

    @Given("company with identification number {string} exists")
    public void companyWithIdentificationNumberExists(String arg0) {
        int idNum = Integer.parseInt(arg0);
        try {
            assertDoesNotThrow(() -> companyService.getCompanyByIdNumber(idNum));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @When("delete the company with identification number {string}")
    public void deleteTheCompanyWithIdentificationNumber(String arg0) {
        companyService.deleteCompanyByIdentificationNumber(Integer.parseInt(arg0));
    }

    @Then("company with identification number {string} should be deleted from the database")
    public void companyWithIdentificationNumberShouldBeDeletedFromTheDatabase(String arg0) {
        assertThrows(CompanyNotFoundException.class, () -> companyService.getCompanyByIdNumber(Integer.parseInt(arg0)));

    }


    @Given("there is no company with identification number {int} in the database")
    public void thereIsNoCompanyWithIdentificationNumberInTheDatabase(int arg0) {
        assertFalse(companyRepository.findByIdentificationNumber(arg0).isPresent());
    }

    @Then("I attempt to delete the company with identification number {int}")
    public void iAttemptToDeleteTheCompanyWithIdentificationNumber(int arg0) {
        assertThrows(NotFoundException.class, () -> companyService.deleteCompanyByIdentificationNumber(arg0));
    }


    @When("calling find all companies")
    public void callingFindAllCompanies() {
        try {
            companyDtoList = companyService.findAllCompanies();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Then("get list of all companies")
    public void getListOfAllCompanies() {
        assertNotNull(companyDtoList);
    }

    @Given("Company with pib {string} exists")
    public void companyWithPibExists(String arg0) {
        assertTrue(companyRepository.findByPib(Long.parseLong(arg0)).isPresent());
    }

    @Then("find company by pib {string}")
    public void findCompanyByPib(String arg0) {
        Long pib = Long.parseLong(arg0);
        try {
            boolean found = false;
            for (CompanyDto companyDto : companyDtoList) {
                System.out.println("idx " + companyDto.getId());
                if (companyRepository.findByPib(companyDto.getPib()).get().getPib().equals(pib))
                    found = true;
            }
            assertTrue(found);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Given("Company with id {string} exists")
    public void companyWithIdExists(String arg0) {
        try {
            assertTrue(companyRepository.findById(Long.parseLong(arg0)).isPresent());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }


    @Given("company with id {string} does not exist")
    public void companyWithIdDoesNotExist(String arg0) {
        try {
            assertFalse(companyRepository.findById(Long.parseLong(arg0)).isPresent());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @When("updating company name with {string}")
    public void updatingCompanyNameWith(String arg0) {

        companyDto.setCompanyName(arg0);

        assertDoesNotThrow(() -> companyService.updateCompany(companyDto));
    }

    @Then("company successfully updated with {string}")
    public void companySuccessfullyUpdatedWith(String arg0) {
        assertEquals(companyService.getCompanyById(companyId).getCompanyName(), arg0);
    }

    @Given("company exists")
    public void companyExists() {
        try {
            companyDto = companyService.getCompanyByPib(123456789L);
            companyId = companyDto.getId();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }


    @When("create new company")
    public void createNewCompany() {
        CompanyDto toBeCreated = new CompanyDto(
                999L,
                "Comp name",
                "1111111",
                "1111111",
                12345L,
                101,
                102,
                103,
                "Adresa"
        );
        try {
            assertDoesNotThrow(() -> {
                createdId = companyService.createCompany(toBeCreated).getId();
            });
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

    @Then("company successfully created")
    public void companySuccessfullyCreated() {
        try {
            assertTrue(companyRepository.findById(createdId).isPresent());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @When("create new company {string} {string} {string} {string} {string} {string} {string} {string} {string}")
    public void createNewCompany(String id, String compName, String faxNum, String phoneNum, String pib, String regNum, String idNum, String activityCode, String address) {
        CompanyDto toBeCreated = new CompanyDto(
                Long.parseLong(id),
                compName,
                faxNum,
                phoneNum,
                Long.parseLong(pib),
                Integer.parseInt(regNum),
                Integer.parseInt(idNum),
                Integer.parseInt(activityCode),
                address
        );
        try {
            assertDoesNotThrow(() -> {
                createdId = companyService.createCompany(toBeCreated).getId();
            });
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @After
    public void cleanDb() {
        companyRepository.deleteAll();
    }
}
