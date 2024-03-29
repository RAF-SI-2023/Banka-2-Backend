package rs.edu.raf.IAMService.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.webjars.NotFoundException;
import rs.edu.raf.IAMService.data.dto.ClientActivationMessageDto;
import rs.edu.raf.IAMService.data.dto.CorporateClientDto;
import rs.edu.raf.IAMService.data.dto.EmployeeDto;
import rs.edu.raf.IAMService.data.dto.PrivateClientDto;
import rs.edu.raf.IAMService.data.entites.*;
import rs.edu.raf.IAMService.data.enums.PermissionType;
import rs.edu.raf.IAMService.data.enums.RoleType;
import rs.edu.raf.IAMService.exceptions.EmailNotFoundException;
import rs.edu.raf.IAMService.exceptions.EmailTakenException;
import rs.edu.raf.IAMService.exceptions.MissingRoleException;
import rs.edu.raf.IAMService.exceptions.UserNotFoundException;
import rs.edu.raf.IAMService.mapper.UserMapper;
import rs.edu.raf.IAMService.repositories.RoleRepository;
import rs.edu.raf.IAMService.repositories.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    public void createPrivateClient_validDto_returnsSavedPrivateClient() {
//        // given
//        PrivateClientDto requestDto = getPrivateClientDto();
//        PrivateClientDto responseDto = getPrivateClientDto();
//        responseDto.setId(1L);
//        PrivateClient client = getPrivateClient();
//        when(userMapper.privateClientDtoToPrivateClient(any(PrivateClientDto.class))).thenReturn(client);
//        when(userRepository.save(any(PrivateClient.class))).thenReturn(client);
//        when(userMapper.privateClientToPrivateClientDto(any(PrivateClient.class))).thenReturn(responseDto);
//
//        // when
//        PrivateClientDto result = userService.createPrivateClient(requestDto);
//
//        // then
//        verify(userRepository, times(1)).save(any(PrivateClient.class));
//        verify(rabbitTemplate, times(1)).convertAndSend(eq("password-activation"), any(ClientActivationMessageDto.class));
//        assertNotNull(result.getId());
//        assertEquals(requestDto.getName(), result.getName());
//        assertEquals(requestDto.getSurname(), result.getSurname());
//        assertEquals(requestDto.getUsername(), result.getUsername());
//        assertEquals(requestDto.getGender(), result.getGender());
//        assertEquals(requestDto.getAddress(), result.getAddress());
//        assertEquals(requestDto.getEmail(), result.getEmail());
//        assertEquals(requestDto.getPhone(), result.getPhone());
//        assertEquals(requestDto.getPrimaryAccountNumber(), result.getPrimaryAccountNumber());
//        assertEquals(requestDto.getRole().getRole(), result.getRole().getRole());
//        assertEquals(requestDto.getPermissions().get(0).getAuthority(), result.getPermissions().get(0).getAuthority());
//    }

//    @Test
//    public void createCorporateClient_validDto_returnsSavedCorporateClient() {
//        // given
//        CorporateClientDto requestDto = getCorporateClientDto();
//        CorporateClientDto responseDto = getCorporateClientDto();
//        responseDto.setId(1L);
//        CorporateClient client = new CorporateClient();
//        when(userMapper.corporateClientDtoToCorporateClient(any(CorporateClientDto.class))).thenReturn(client);
//        when(userRepository.save(any(CorporateClient.class))).thenReturn(client);
//        when(userMapper.corporateClientToCorporateClientDto(any(CorporateClient.class))).thenReturn(responseDto);
//
//        // when
//        CorporateClientDto result = userService.createCorporateClient(requestDto);
//
//        // then
//        verify(userRepository, times(1)).save(client);
//        verify(rabbitTemplate, times(1)).convertAndSend(eq("password-activation"), any(ClientActivationMessageDto.class));
//        assertNotNull(result.getId());
//        assertEquals(requestDto.getName(), result.getName());
//        assertEquals(requestDto.getUsername(), result.getUsername());
//        assertEquals(requestDto.getAddress(), result.getAddress());
//        assertEquals(requestDto.getEmail(), result.getEmail());
//        assertEquals(requestDto.getPhone(), result.getPhone());
//        assertEquals(requestDto.getPrimaryAccountNumber(), result.getPrimaryAccountNumber());
//        assertEquals(requestDto.getRole().getRole(), result.getRole().getRole());
//        assertEquals(requestDto.getPermissions().get(0).getAuthority(), result.getPermissions().get(0).getAuthority());
//    }

//    @Test
//    public void activateClient_userExists_updatePassword() {
//        // given
//        String clientId = "1";
//        String password = "newPassword";
//        User user = new User();
//        when(userRepository.findById(Long.parseLong(clientId))).thenReturn(Optional.of(user));
//        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
//        when(userRepository.save(any(User.class))).thenReturn(new User());
//
//        // when
//        userService.passwordActivation(clientId, password);
//
//        // then
//        assertEquals("encodedPassword", user.getPassword());
//        verify(userRepository, times(1)).findById(any());
//        verify(passwordEncoder, times(1)).encode(any());
//        verify(userRepository, times(1)).save(any(User.class));
//    }

//    @Test
//    public void activateClient_userDoesNotExist_throwException() {
//        // given
//        String clientId = "1";
//        String password = "newPassword";
//        when(userRepository.findById(Long.parseLong(clientId))).thenReturn(Optional.empty());
//
//        // then
//        assertThrows(UserNotFoundException.class,
//                () -> userService.passwordActivation(clientId, password));
//        verify(userRepository, times(1)).findById(any());
//        verify(passwordEncoder, times(0)).encode(any());
//        verify(userRepository, times(0)).save(any());
//    }

    @Test
    public void employeeActivationTest() {
        // given
        Employee employee = new Employee();
        employee.setActive(false);
        when(userRepository.findById(1)).thenReturn(Optional.of(employee));
        when(userRepository.save(employee)).thenReturn(employee);

        // when
        userService.employeeActivation(1);

        // then
        assertTrue(employee.isActive());

    }

    @Test
    public void employeeActivation_doesNotExist_throwsException_Test() {
        // given
        Employee employee = new Employee();
        employee.setActive(false);
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class,
                () -> userService.employeeActivation(1));

    }

    @Test
    public void employeeDeactivationTest() {
        // given
        Employee employee = new Employee();
        employee.setActive(true);
        when(userRepository.findById(1)).thenReturn(Optional.of(employee));
        when(userRepository.save(employee)).thenReturn(employee);

        // when
        userService.employeeDeactivation(1);

        // then
        assertFalse(employee.isActive());
    }

    @Test
    public void employeeDeactivation_doesNotExist_throwsException_Test() {
        // given
        Employee employee = new Employee();
        employee.setActive(false);
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class,
                () -> userService.employeeDeactivation(1));

    }


    private PrivateClientDto getPrivateClientDto() {
        PrivateClientDto dto = new PrivateClientDto();
        dto.setId(null);
        dto.setName("test name");
        dto.setSurname("test surname");
        dto.setUsername("test username");
        dto.setGender("test gender");
        dto.setAddress("test address");
        dto.setEmail("test email");
        dto.setDateOfBirth(new Date().getTime());
        dto.setPhone("test phone number");
        dto.setUsername("test username");
        dto.setPrimaryAccountNumber("test account number");
        dto.setRole(new Role(RoleType.USER).getRoleType());
        dto.setPermissions(List.of(new Permission(PermissionType.PERMISSION_1).getPermissionType()));

        return dto;
    }

    private PrivateClient getPrivateClient() {
        PrivateClient client = new PrivateClient();
        client.setId(1L);
        client.setName("test name");
        client.setSurname("test surname");
        client.setUsername("test username");
        client.setGender("test gender");
        client.setAddress("test address");
        client.setEmail("test email");
        client.setDateOfBirth(new Date().getTime());
        client.setPhone("test phone number");
        client.setUsername("test username");
        client.setPrimaryAccountNumber("test account number");
        client.setRole(new Role(RoleType.USER));
        client.setPermissions(List.of(new Permission(PermissionType.PERMISSION_1)));

        return client;
    }

    private CorporateClientDto getCorporateClientDto() {
        CorporateClientDto dto = new CorporateClientDto();
        dto.setId(1L);
        dto.setName("test name");
        dto.setUsername("test username");
        dto.setAddress("test address");
        dto.setEmail("test email");
        dto.setDateOfBirth(new Date().getTime());
        dto.setPhone("test phone number");
        dto.setUsername("test username");
        dto.setPrimaryAccountNumber("test account number");
        dto.setRole(new Role(RoleType.USER).getRoleType());
        dto.setPermissions(List.of(new Permission(PermissionType.PERMISSION_1).getPermissionType()));

        return dto;
    }

    private CorporateClient getCorporateClient() {
        CorporateClient client = new CorporateClient();
        client.setId(1L);
        client.setName("test name");
        client.setUsername("test username");
        client.setAddress("test address");
        client.setEmail("test email");
        client.setDateOfBirth(new Date().getTime());
        client.setPhone("test phone number");
        client.setUsername("test username");
        client.setPrimaryAccountNumber("test account number");
        client.setRole(new Role(RoleType.USER));
        client.setPermissions(List.of(new Permission(PermissionType.PERMISSION_1)));

        return client;
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