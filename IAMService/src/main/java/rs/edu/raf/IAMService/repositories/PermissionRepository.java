package rs.edu.raf.IAMService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.IAMService.data.entites.Permission;
import rs.edu.raf.IAMService.data.enums.PermissionType;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByPermissionType(PermissionType permissionType);
}
