package rs.edu.raf.IAMService.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.IAMService.data.dto.CorporateClientDto;
import rs.edu.raf.IAMService.data.dto.EmployeeDto;
import rs.edu.raf.IAMService.data.dto.PrivateClientDto;
import rs.edu.raf.IAMService.data.dto.UserDto;
import rs.edu.raf.IAMService.data.entites.*;

import java.util.Locale;

@Component
public class UserMapper {

    public CorporateClient corporateClientDtoToCorporateClient(CorporateClientDto dto) {
        return new CorporateClient(
                dto.getDateOfBirth(),
                dto.getEmail(),
                dto.getEmail(),
                dto.getPhone(),
                dto.getAddress(),
                dto.getPermissions().stream().map(Permission::new).toList(),
                dto.getName(),
                dto.getPrimaryAccountNumber(),
                false
        );
    }

    public PrivateClient privateClientDtoToPrivateClient(PrivateClientDto dto) {
        return new PrivateClient(
                dto.getDateOfBirth(),
                dto.getEmail(),
                dto.getEmail(),
                dto.getPhone(),
                dto.getAddress(),
                dto.getPermissions().stream().map(Permission::new).toList(),
                dto.getName(),
                dto.getSurname(),
                dto.getGender(),
                dto.getPrimaryAccountNumber(),
                false
        );
    }

    public Employee employeeDtoToEmployee(EmployeeDto dto) {
        return new Employee(
                dto.getDateOfBirth(),
                dto.getEmail(),
                dto.getUsername(),
                dto.getPhone(),
                dto.getAddress(),
                new Role(dto.getRole()),
                dto.getPermissions().stream().map(Permission::new).toList(),
                dto.getName(),
                dto.getSurname(),
                dto.getGender(),
                dto.getPosition(),
                dto.getDepartment().toLowerCase(Locale.ROOT),
                false
        );
    }

    public User userDtoToUser(UserDto dto) {
        return new User(
                dto.getDateOfBirth(),
                dto.getEmail(),
                dto.getEmail(),
                dto.getPhone(),
                dto.getAddress(),
                new Role(dto.getRole()),
                dto.getPermissions().stream().map(Permission::new).toList(),
                dto.isActive()
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
                user.getPermissions().stream().map(Permission::getPermissionType).toList(),
                user.getName(),
                user.getPrimaryAccountNumber(),
                user.isActive()
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
                user.getPermissions().stream().map(Permission::getPermissionType).toList(),
                user.isActive(),
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
                user.getPermissions().stream().map(Permission::getPermissionType).toList(),
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
                user.getRole().getRoleType(),
                user.getPermissions().stream().map(Permission::getPermissionType).toList(),
                user.isActive()
        );
    }

}
