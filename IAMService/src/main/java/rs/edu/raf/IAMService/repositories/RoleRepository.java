package rs.edu.raf.IAMService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import rs.edu.raf.IAMService.data.entites.Employee;
import rs.edu.raf.IAMService.data.entites.Role;
import rs.edu.raf.IAMService.data.enums.RoleType;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleType(RoleType roleType);
}
