package rs.edu.raf.IAMService.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import rs.edu.raf.IAMService.data.dto.EmployeeDto;
import rs.edu.raf.IAMService.data.dto.UserDto;
import rs.edu.raf.IAMService.data.entites.User;

import java.util.Optional;

@Service
public interface UserService extends UserDetailsService {
    UserDto findByEmail(String email);

    User employeeActivation(int id);

    User employeeDeactivation(int id);

    Optional<User> findUserByEmail(String email);

    void sendToQueue(String email,String urlLink);

    User updateEntity(User user);
}
