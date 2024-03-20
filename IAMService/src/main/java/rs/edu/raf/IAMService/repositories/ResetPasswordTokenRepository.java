package rs.edu.raf.IAMService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.IAMService.data.dto.PasswordChangeTokenDto;
import rs.edu.raf.IAMService.data.entites.PasswordChangeToken;
import rs.edu.raf.IAMService.data.entites.Permission;
import rs.edu.raf.IAMService.data.entites.User;

import java.util.Optional;

@Repository
public interface ResetPasswordTokenRepository extends JpaRepository<PasswordChangeToken, Long> {

    Optional<PasswordChangeToken> findByEmail(String email);

}
