package rs.edu.raf.IAMService.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.IAMService.data.dto.CorporateClientDto;
import rs.edu.raf.IAMService.data.dto.EmployeeDto;
import rs.edu.raf.IAMService.data.dto.PrivateClientDto;
import rs.edu.raf.IAMService.data.dto.UserDto;
import rs.edu.raf.IAMService.data.entites.CorporateClient;
import rs.edu.raf.IAMService.data.entites.Employee;
import rs.edu.raf.IAMService.data.entites.PrivateClient;
import rs.edu.raf.IAMService.data.entites.User;

@Component
public class UserMapper {

    public CorporateClient corporateClientDtoToCorporateClient(CorporateClientDto dto) {
        return new CorporateClient(
                dto.getDateOfBirth(),
                dto.getEmail(),
                dto.getEmail(),
                dto.getPhone(),
                dto.getAddress(),
                dto.getPermissions(),
                dto.getName(),
                dto.getPrimaryAccountNumber()
        );
    }

    public PrivateClient privateClientDtoToPrivateClient(PrivateClientDto dto) {
        return new PrivateClient(
                dto.getDateOfBirth(),
                dto.getEmail(),
                dto.getEmail(),
                dto.getPhone(),
                dto.getAddress(),
                dto.getPermissions(),
                dto.getName(),
                dto.getSurname(),
                dto.getGender(),
                dto.getPrimaryAccountNumber()
        );
    }

    public Employee employeeDtoToEmployee(EmployeeDto dto) {
        return new Employee(
                dto.getDateOfBirth(),
                dto.getEmail(),
                dto.getEmail(),
                dto.getPhone(),
                dto.getAddress(),
                dto.getPermissions(),
                dto.getName(),
                dto.getSurname(),
                dto.getGender(),
                dto.getPosition(),
                dto.getDepartment(),
                dto.isActive()
        );
    }

    public User userDtoToUser(UserDto dto) {
        return new User(
                dto.getDateOfBirth(),
                dto.getEmail(),
                dto.getEmail(),
                dto.getPhone(),
                dto.getAddress(),
                dto.getRole(),
                dto.getPermissions()
        );
    }

    public CorporateClientDto corporateClientToCorporateClientDto(CorporateClient user) {
        return new CorporateClientDto(
                user.getId(),
                user.getDateOfBirth(),
                user.getEmail(),
                user.getUsername(),
                user.getPhone(),
                user.getAddress(),
                user.getPermissions(),
                user.getName(),
                user.getPrimaryAccountNumber()
        );
    }

    public PrivateClientDto privateClientToPrivateClientDto(PrivateClient user) {
        return new PrivateClientDto(
                user.getId(),
                user.getDateOfBirth(),
                user.getEmail(),
                user.getUsername(),
                user.getPhone(),
                user.getAddress(),
                user.getPermissions(),
                user.getName(),
                user.getSurname(),
                user.getGender(),
                user.getPrimaryAccountNumber()
        );
    }

    public EmployeeDto employeeToEmployeeDto(Employee user) {
        return new EmployeeDto(
                user.getId(),
                user.getDateOfBirth(),
                user.getEmail(),
                user.getUsername(),
                user.getPhone(),
                user.getAddress(),
                user.getPermissions(),
                user.getName(),
                user.getSurname(),
                user.getGender(),
                user.getPosition(),
                user.getDepartment(),
                user.isActive()
        );
    }

    public UserDto userToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getDateOfBirth(),
                user.getEmail(),
                user.getUsername(),
                user.getPhone(),
                user.getAddress(),
                user.getRole(),
                user.getPermissions()
        );
    }

}
