package rs.edu.raf.IAMService;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import rs.edu.raf.IAMService.repositories.CompanyRepository;
import rs.edu.raf.IAMService.services.impl.CompanyServiceImpl;

import static org.mockito.Mockito.verify;

@SpringBootTest
class IamServiceApplicationTests {
	@Mock
	private CompanyRepository companyRepository;

	@InjectMocks
	private CompanyServiceImpl companyService;

	@Test
	public void testDeleteCompanyByRegistrationNumber() {
		String registrationNumber = "XYZ123";
		companyService.deleteCompanyByRegistryNumber(registrationNumber);
		verify(companyRepository).deleteByregistryNumber(registrationNumber);
	}
	@Test
	void contextLoads() {
	}

}
