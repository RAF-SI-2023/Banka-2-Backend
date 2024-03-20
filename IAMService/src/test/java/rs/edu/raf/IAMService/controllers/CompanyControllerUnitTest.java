package rs.edu.raf.IAMService.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.edu.raf.IAMService.services.CompanyService;

@ExtendWith(MockitoExtension.class)
public class CompanyControllerUnitTest {
    @InjectMocks
    private CompanyController companyController;
    @Mock
    private CompanyService companyService;

    void testDeleteCompanyByPib(){

    }
}
