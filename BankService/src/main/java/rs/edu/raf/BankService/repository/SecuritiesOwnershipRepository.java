package rs.edu.raf.BankService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.BankService.data.entities.SecuritiesOwnership;

@Repository
public interface SecuritiesOwnershipRepository extends JpaRepository<SecuritiesOwnership, Long> {

}
