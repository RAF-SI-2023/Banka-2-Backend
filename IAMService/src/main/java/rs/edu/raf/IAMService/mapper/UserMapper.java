package rs.edu.raf.IAMService.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.IAMService.data.dto.*;
import rs.edu.raf.IAMService.data.entites.*;

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
                dto.getPermissions().stream().map(Permission::new).toList(),
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
                dto.getPermissions().stream().map(Permission::new).toList(),
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
                new Role(dto.getRole()),
                dto.getPermissions().stream().map(Permission::new).toList()
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
                user.getPermissions().stream().map(Permission::getPermissionType).toList(),
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
                user.getPermissions().stream().map(Permission::getPermissionType).toList()
        );
    }

    public Supervisor supervisorDtoToSupervisor(SupervisorDto dto) {
        return new Supervisor(
                dto.getDateOfBirth(),
                dto.getEmail(),
                dto.getEmail(),
                dto.getPhone(),
                dto.getAddress(),
                dto.getPermissions().stream().map(Permission::new).toList()
        );
    }

    public SupervisorDto supervisorToSupervisorDto(Supervisor supervisor) {
        return new SupervisorDto(
                supervisor.getId(),
                supervisor.getDateOfBirth(),
                supervisor.getEmail(),
                supervisor.getUsername(),
                supervisor.getPhone(),
                supervisor.getAddress(),
                supervisor.getPermissions().stream().map(Permission::getPermissionType).toList()
        );
    }

    public Agent agentDtoToAgent(AgentDto dto) {
        return new Agent(
                dto.getDateOfBirth(),
                dto.getEmail(),
                dto.getUsername(),
                dto.getPhone(),
                dto.getAddress(),
                dto.getPermissions().stream().map(Permission::new).toList(),
                dto.getLimit(),
                dto.getLeftOfLimit()
        );
    }

    public AgentDto agentToAgentDto(Agent agent) {
        return new AgentDto(
                agent.getId(),
                agent.getDateOfBirth(),
                agent.getEmail(),
                agent.getUsername(),
                agent.getPhone(),
                agent.getAddress(),
                agent.getPermissions().stream().map(Permission::getPermissionType).toList(),
                agent.getLimit(),
                agent.getLeftOfLimit()
        );
    }
}
