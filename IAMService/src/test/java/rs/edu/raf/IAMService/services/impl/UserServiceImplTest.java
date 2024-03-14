package rs.edu.raf.IAMService.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import rs.edu.raf.IAMService.data.dto.EmployeeDto;
import rs.edu.raf.IAMService.data.entites.Employee;
import rs.edu.raf.IAMService.data.entites.Role;
import rs.edu.raf.IAMService.data.enums.RoleType;
import rs.edu.raf.IAMService.exceptions.EmailNotFoundException;
import rs.edu.raf.IAMService.exceptions.EmailTakenException;
import rs.edu.raf.IAMService.exceptions.MissingRoleException;
import rs.edu.raf.IAMService.mapper.UserMapper;
import rs.edu.raf.IAMService.repositories.RoleRepository;
import rs.edu.raf.IAMService.repositories.UserRepository;
import rs.edu.raf.IAMService.services.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateEmployee_Success() {
        // Setup
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setEmail("test@user.com");

        Employee employee = new Employee();

        // Mocking userRepository
        when(userRepository.findByEmail(employeeDto.getEmail())).thenReturn(Optional.empty());

        when(userMapper.employeeDtoToEmployee(employeeDto)).thenReturn(employee);

        // Mocking roleRepository
        Role role = new Role(RoleType.EMPLOYEE);
        when(roleRepository.findByRoleType(RoleType.EMPLOYEE)).thenReturn(Optional.of(role));

        // Mocking userRepository.save()
        when(userRepository.save(any(Employee.class))).thenReturn(employee);

        // Mocking userMapper
        when(userMapper.employeeToEmployeeDto(employee)).thenReturn(employeeDto);

        // Execution
        EmployeeDto result = userService.createEmployee(employeeDto);

        // Assertion
        assertNotNull(result);
    }

    @Test
    public void testCreateEmployee_EmailTaken() {
        // Setup
        EmployeeDto employeeDto = new EmployeeDto(/* provide employee details */);

        // Mocking userRepository
        when(userRepository.findByEmail(employeeDto.getEmail())).thenReturn(Optional.of(new Employee()));

        // Execution and Assertion
        assertThrows(EmailTakenException.class, () -> userService.createEmployee(employeeDto));
    }

    @Test
    public void testCreateEmployee_MissingRole() {
        // Setup
        EmployeeDto employeeDto = new EmployeeDto();
        when(userRepository.findByEmail(employeeDto.getEmail())).thenReturn(Optional.empty());
        when(roleRepository.findByRoleType(RoleType.EMPLOYEE)).thenReturn(Optional.empty());

        // Execution and Assertion
        assertThrows(MissingRoleException.class, () -> userService.createEmployee(employeeDto));
    }

    @Test
    public void testActivateEmployee_Success() {
        // Setup
        String email = "test@example.com";
        String password = "password123";
        Employee employee = new Employee(/* provide employee details */);
        when(userRepository.findEmployeeByEmail(email)).thenReturn(Optional.of(employee));

        // Mocking password encoder
        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

        // Mocking userRepository.save()
        when(userRepository.save(any(Employee.class))).thenReturn(employee);

        // Mocking userMapper
        EmployeeDto employeeDto = new EmployeeDto(/* provide employee details */);
        when(userMapper.employeeToEmployeeDto(employee)).thenReturn(employeeDto);

        // Execution
        EmployeeDto result = userService.activateEmployee(email, password);

        // Assertion
        assertNotNull(result);
    }

    @Test
    public void testActivateEmployee_EmailNotFound() {
        // Setup
        String email = "nonexistent@example.com";
        String password = "password123";
        when(userRepository.findEmployeeByEmail(email)).thenReturn(Optional.empty());

        // Execution and Assertion
        assertThrows(EmailNotFoundException.class, () -> userService.activateEmployee(email, password));
    }

}