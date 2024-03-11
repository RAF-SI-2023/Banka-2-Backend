package rs.edu.raf.IAMService.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import rs.edu.raf.IAMService.data.dto.EmployeeDto;
import rs.edu.raf.IAMService.data.dto.UserDto;

@Service
public interface UserService extends UserDetailsService {
    UserDto findByEmail(String email);
    UserDto createAdmin(UserDto userDto, String password);
    EmployeeDto createEmployee(EmployeeDto employeeDto);
}
