package rs.edu.raf.IAMService.mapper;

import org.junit.jupiter.api.Test;
import rs.edu.raf.IAMService.data.dto.CorporateClientDto;
import rs.edu.raf.IAMService.data.dto.EmployeeDto;
import rs.edu.raf.IAMService.data.dto.PrivateClientDto;
import rs.edu.raf.IAMService.data.dto.UserDto;
import rs.edu.raf.IAMService.data.entites.CorporateClient;
import rs.edu.raf.IAMService.data.entites.Employee;
import rs.edu.raf.IAMService.data.entites.PrivateClient;
import rs.edu.raf.IAMService.data.entites.User;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();

    @Test
    public void testCorporateClientDtoToCorporateClient() {
        // Arrange
        CorporateClientDto dto = new CorporateClientDto();
        dto.setDateOfBirth(Date.valueOf(LocalDate.of(1980, 1, 10)));
        dto.setEmail("corporate@example.com");
        // Set other DTO properties as needed

        // Act
        CorporateClient corporateClient = userMapper.corporateClientDtoToCorporateClient(dto);

        // Assert
        assertEquals(dto.getDateOfBirth(), corporateClient.getDateOfBirth());
        assertEquals(dto.getEmail(), corporateClient.getEmail());
        // Add more assertions to verify other properties
    }

    @Test
    public void testPrivateClientDtoToPrivateClient() {
        // Arrange
        PrivateClientDto dto = new PrivateClientDto();
        dto.setDateOfBirth(Date.valueOf(LocalDate.of(1980, 1, 10)));
        dto.setEmail("private@example.com");
        // Set other DTO properties as needed

        // Act
        PrivateClient privateClient = userMapper.privateClientDtoToPrivateClient(dto);

        // Assert
        assertEquals(dto.getDateOfBirth(), privateClient.getDateOfBirth());
        assertEquals(dto.getEmail(), privateClient.getEmail());
        // Add more assertions to verify other properties
    }

    @Test
    public void testEmployeeDtoToEmployee() {
        // Arrange
        EmployeeDto dto = new EmployeeDto();
        dto.setDateOfBirth(Date.valueOf(LocalDate.of(1980, 1, 10)));
        dto.setEmail("employee@example.com");
        // Set other DTO properties as needed

        // Act
        Employee employee = userMapper.employeeDtoToEmployee(dto);

        // Assert
        assertEquals(dto.getDateOfBirth(), employee.getDateOfBirth());
        assertEquals(dto.getEmail(), employee.getEmail());
        // Add more assertions to verify other properties
    }

    @Test
    public void testUserDtoToUser() {
        // Arrange
        UserDto userDto = new UserDto();
        // Set DTO properties here

        // Act
        User user = userMapper.userDtoToUser(userDto);

        // Assert
        // Add assertions here to verify that the User object is correctly mapped from the DTO
    }

    @Test
    public void testCorporateClientToCorporateClientDto() {
        // Arrange
        CorporateClient corporateClient = new CorporateClient();
        // Set CorporateClient properties here

        // Act
        CorporateClientDto dto = userMapper.corporateClientToCorporateClientDto(corporateClient);

        // Assert
        // Add assertions here to verify that the CorporateClientDto object is correctly mapped from the CorporateClient
    }

    @Test
    public void testPrivateClientToPrivateClientDto() {
        // Arrange
        PrivateClient privateClient = new PrivateClient();
        // Set PrivateClient properties here

        // Act
        PrivateClientDto dto = userMapper.privateClientToPrivateClientDto(privateClient);

        // Assert
        // Add assertions here to verify that the PrivateClientDto object is correctly mapped from the PrivateClient
    }

    @Test
    public void testEmployeeToEmployeeDto() {
        // Arrange
        Employee employee = new Employee();
        // Set Employee properties here

        // Act
        EmployeeDto dto = userMapper.employeeToEmployeeDto(employee);

        // Assert
        // Add assertions here to verify that the EmployeeDto object is correctly mapped from the Employee
    }

    @Test
    public void testUserToUserDto() {
        // Arrange
        User user = new User();
        // Set User properties here

        // Act
        UserDto dto = userMapper.userToUserDto(user);

        // Assert
        // Add assertions here to verify that the UserDto object is correctly mapped from the User
    }
}

