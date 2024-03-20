package rs.edu.raf.IAMService.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.IAMService.services.CompanyService;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
@CrossOrigin
public class CompanyController {
    private final CompanyService companyService;


    @GetMapping("/find-company-by-id/{id}")
    public ResponseEntity<?> findCompanyById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(companyService.getCompanyById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{pib}")
    public ResponseEntity<?> deleteCompanyByPib(@PathVariable Long pib){
        try{
            return ResponseEntity.ok(companyService.deleteCompanyByPib(pib));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


}
