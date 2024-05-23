package rs.edu.raf.BankService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.BankService.data.entities.UserAccountUserProfileActivationCode;

@Repository
public interface UserAccountUserProfileActivationCodeRepository extends JpaRepository<UserAccountUserProfileActivationCode, Long> {

    UserAccountUserProfileActivationCode findByAccountNumber(String accountNumber);
}

