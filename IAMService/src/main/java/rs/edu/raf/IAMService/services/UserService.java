package rs.edu.raf.IAMService.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import rs.edu.raf.IAMService.data.dto.UserDto;
import rs.edu.raf.IAMService.data.entites.Permission;
import rs.edu.raf.IAMService.data.entites.User;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService extends UserDetailsService {
    UserDto findByEmail(String email);

    Optional<User> findUserByEmail(String email);

    List<Permission> getUserPermissions(Long id);

    void addUserPermission(Long id, Permission permission);

    void removeUserPermission(Long id, Permission permission);

    void deleteAndSetUserPermissions(Long id, List<Permission> permissionList);

    void sendToQueue(String email,String urlLink);

    User updateEntity(User user);

}
