package rs.edu.raf.IAMService.controllers;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.IAMService.data.dto.CompanyDto;
import rs.edu.raf.IAMService.services.CompanyService;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
@CrossOrigin
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping("/find-all")
    public ResponseEntity<?> findAllCompanies() {
        try {
            return ResponseEntity.ok(companyService.findAllCompanies());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/find-company-by-id/{id}")
    public ResponseEntity<?> findCompanyById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(companyService.getCompanyById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/find-company-by-pib/{pib}")
    public ResponseEntity<?> findCompanyByPib(@PathVariable Long pib) {
        try {
            return ResponseEntity.ok(companyService.getCompanyByPib(pib));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    public ResponseEntity<?> createCompany(@RequestBody CompanyDto companyDto) {
        try {
            return ResponseEntity.ok(companyService.createCompany(companyDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-company-by-id/{id}")
    public ResponseEntity<?> deleteCompanyById(@PathVariable Long id) {
        companyService.deleteCompanyById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete-company-by-identificationNumber/{identificationNumber}")
    public ResponseEntity<?> deleteCompanyByIdentificationNumber(@PathVariable Integer identificationNumber) {
        companyService.deleteCompanyByIdentificationNumber(identificationNumber);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/find-company-by-id-identificationNumber/{id}")
    public ResponseEntity<?> findCompanyByIdentificationNumber(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(companyService.getCompanyByIdNumber(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/update-company")
    public ResponseEntity<?> updateCompany(@RequestBody CompanyDto companyDto) {
        try {
            return ResponseEntity.ok(companyService.updateCompany(companyDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
