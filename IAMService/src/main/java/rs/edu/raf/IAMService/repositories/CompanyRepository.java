package rs.edu.raf.IAMService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.IAMService.data.entites.Company;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Long deleteByPib(Long pib);
    Integer deleteByregistryNumber(String registrationNumber);
    Optional<Company> findByPib(Long pib);
    Optional<Company> findByIdentificationNumber(Integer idNumber);
}
