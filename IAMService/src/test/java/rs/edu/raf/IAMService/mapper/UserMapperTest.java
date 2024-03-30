package rs.edu.raf.IAMService.mapper;

import org.junit.jupiter.api.Test;
import rs.edu.raf.IAMService.data.dto.*;
import rs.edu.raf.IAMService.data.entites.*;
import rs.edu.raf.IAMService.data.enums.PermissionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();

    @Test
    public void testCorporateClientDtoToCorporateClient() {
        // Arrange
        CorporateClientDto dto = new CorporateClientDto();
        dto.setDateOfBirth(Long.valueOf(LocalDate.of(1980, 1, 10).toEpochDay()));
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
        dto.setDateOfBirth(Long.valueOf(LocalDate.of(1980, 1, 10).toEpochDay()));
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
        dto.setDateOfBirth(Long.valueOf(LocalDate.of(1980, 1, 10).toEpochDay()));
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

    @Test
    void testSupervisorDtoToSupervisor() {
        // given
        List<PermissionType> permissions = new ArrayList<>();
        permissions.add(PermissionType.PERMISSION_1);
        permissions.add(PermissionType.PERMISSION_2);
        SupervisorDto dto = new SupervisorDto(
                null,
                LocalDate.now().toEpochDay(),
                "email@example.com",
                "email@example.com",
                "555-1234",
                "123 Main St",
                permissions
        );

        // when
        Supervisor supervisor = userMapper.supervisorDtoToSupervisor(dto);

        // then
        assertEquals(dto.getDateOfBirth(), supervisor.getDateOfBirth());
        assertEquals(dto.getEmail(), supervisor.getEmail());
        assertEquals(dto.getEmail(), supervisor.getUsername());
        assertEquals(dto.getPhone(), supervisor.getPhone());
        assertEquals(dto.getAddress(), supervisor.getAddress());
        assertEquals(dto.getPermissions(), supervisor.getPermissions().stream().map(Permission::getPermissionType)
                .collect(Collectors.toList()));
    }

    @Test
    void testSupervisorToSupervisorDto() {
        // given
        List<Permission> permissions = new ArrayList<>();
        permissions.add(new Permission(PermissionType.PERMISSION_1));
        permissions.add(new Permission(PermissionType.PERMISSION_2));
        Supervisor supervisor = new Supervisor(
                LocalDate.now().toEpochDay(),
                "email@example.com",
                "email@example.com",
                "555-1234",
                "123 Main St",
                permissions
        );
        supervisor.setId(1L);

        // when
        SupervisorDto dto = userMapper.supervisorToSupervisorDto(supervisor);

        // then
        assertEquals(supervisor.getId(), dto.getId());
        assertEquals(supervisor.getDateOfBirth(), dto.getDateOfBirth());
        assertEquals(supervisor.getEmail(), dto.getEmail());
        assertEquals(supervisor.getUsername(), dto.getUsername());
        assertEquals(supervisor.getPhone(), dto.getPhone());
        assertEquals(supervisor.getAddress(), dto.getAddress());
        assertEquals(permissions.stream().map(Permission::getPermissionType).toList(), dto.getPermissions());
    }

    @Test
    void testAgentDtoToAgent() {
        // given
        List<PermissionType> permissions = new ArrayList<>();
        permissions.add(PermissionType.PERMISSION_1);
        permissions.add(PermissionType.PERMISSION_2);
        BigDecimal limit = new BigDecimal("100.00");
        BigDecimal leftOfLimit = new BigDecimal("50.00");
        AgentDto dto = new AgentDto(
                null,
                LocalDate.now().toEpochDay(),
                "agent@example.com",
                "agent@example.com",
                "555-6789",
                "456 Another St",
                permissions,
                limit,
                leftOfLimit
        );

        // when
        Agent agent = userMapper.agentDtoToAgent(dto);

        // then
        assertEquals(dto.getDateOfBirth(), agent.getDateOfBirth());
        assertEquals(dto.getEmail(), agent.getEmail());
        assertEquals(dto.getUsername(), agent.getUsername());
        assertEquals(dto.getPhone(), agent.getPhone());
        assertEquals(dto.getAddress(), agent.getAddress());
        assertEquals(dto.getPermissions(), agent.getPermissions().stream().map(Permission::getPermissionType)
                .collect(Collectors.toList()));
        assertEquals(dto.getLimit(), agent.getLimit());
        assertEquals(dto.getLeftOfLimit(), agent.getLeftOfLimit());
    }

    @Test
    void testAgentToAgentDto() {
        // given
        List<Permission> permissions = new ArrayList<>();
        permissions.add(new Permission(PermissionType.PERMISSION_1));
        permissions.add(new Permission(PermissionType.PERMISSION_2));
        BigDecimal limit = new BigDecimal("100.00");
        BigDecimal leftOfLimit = new BigDecimal("50.00");
        Agent agent = new Agent(
                LocalDate.now().toEpochDay(),
                "agent@example.com",
                "agentUser",
                "555-6789",
                "456 Another St",
                permissions,
                limit,
                leftOfLimit
        );
        agent.setId(1L);

        // when
        AgentDto dto = userMapper.agentToAgentDto(agent);

        // then
        assertEquals(agent.getId(), dto.getId());
        assertEquals(agent.getDateOfBirth(), dto.getDateOfBirth());
        assertEquals(agent.getEmail(), dto.getEmail());
        assertEquals(agent.getUsername(), dto.getUsername());
        assertEquals(agent.getPhone(), dto.getPhone());
        assertEquals(agent.getAddress(), dto.getAddress());
        assertEquals(agent.getPermissions().stream().map(Permission::getPermissionType).collect(Collectors.toList()), dto.getPermissions());
        assertEquals(agent.getLimit(), dto.getLimit());
        assertEquals(agent.getLeftOfLimit(), dto.getLeftOfLimit());
    }
}

