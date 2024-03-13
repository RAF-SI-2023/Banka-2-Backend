package rs.edu.raf.BankService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.BankService.data.entities.UserAccountUserProfileConnectionToken;

@Repository
public interface UserAccountUserProfileConnectionTokenRepository extends JpaRepository<UserAccountUserProfileConnectionToken, Long> {

    UserAccountUserProfileConnectionToken findByAccountNumber(String accountNumber);
}

