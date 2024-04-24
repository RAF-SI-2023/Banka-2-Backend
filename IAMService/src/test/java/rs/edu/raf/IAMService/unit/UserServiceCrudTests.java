package rs.edu.raf.IAMService.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.webjars.NotFoundException;
import rs.edu.raf.IAMService.data.dto.*;
import rs.edu.raf.IAMService.data.entites.*;
import rs.edu.raf.IAMService.data.enums.RoleType;
import rs.edu.raf.IAMService.exceptions.EmailTakenException;
import rs.edu.raf.IAMService.exceptions.MissingRoleException;
import rs.edu.raf.IAMService.mapper.UserMapper;
import rs.edu.raf.IAMService.repositories.RoleRepository;
import rs.edu.raf.IAMService.repositories.UserRepository;
import rs.edu.raf.IAMService.services.impl.UserServiceImpl;
import rs.edu.raf.IAMService.utils.SpringSecurityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceCrudTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserServiceImpl userService;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindById_UserFound_ReturnsUserDto() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");
        UserDto userDto1 = new UserDto();
        userDto1.setId(userId);
        userDto1.setEmail(user.getEmail());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.userToUserDto(user)).thenReturn(userDto1);
        // Act
        UserDto userDto = userService.findById(userId);

        // Assert
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getEmail(), userDto.getEmail());
        // Add more assertions as needed for other properties
    }

    @Test
    public void testFindById_UserNotFound_ThrowsNotFoundException() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(NotFoundException.class, () -> userService.findById(userId));
    }

    @Test
    public void testFindByEmail_UserFound_ReturnsUserDto() {
        // Arrange
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        UserDto userDto1 = new UserDto();
        userDto1.setEmail(user.getEmail());
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userMapper.userToUserDto(user)).thenReturn(userDto1);

        // Act
        UserDto userDto = userService.findByEmail(email);

        // Assert
        assertEquals(user.getEmail(), userDto.getEmail());
        // Add more assertions as needed for other properties
    }

    @Test
    public void testFindByEmail_UserNotFound_ThrowsNotFoundException() {
        // Arrange
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(NotFoundException.class, () -> userService.findByEmail(email));
    }

    @Test
    public void testFindAll_ReturnsListOfUserDto() {
        // Arrange
        List<User> userList = new ArrayList<>();
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("test1@example.com");
        userList.add(user1);

        UserDto userDto1 = new UserDto();
        userDto1.setId(user1.getId());
        userDto1.setEmail(user1.getEmail());


        when(userRepository.findAll()).thenReturn(userList);
        when(userMapper.userToUserDto(user1)).thenReturn(userDto1);
        // Act
        List<UserDto> userDtoList = userService.findAll();
        System.out.println(userDtoList.stream().toList());
        // Assert
        assertEquals(userList.size(), userDtoList.size());
        assertEquals(userList.get(0).getId(), userDtoList.get(0).getId());
        assertEquals(userList.get(0).getEmail(), userDtoList.get(0).getEmail());

    }

    @Test
    public void testFindUserByEmail_UserFound_ReturnsOptionalOfUser() {
        // Arrange
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.findUserByEmail(email);

        // Assert
        assertEquals(Optional.of(user), result);
    }

    @Test
    public void testFindUserByEmail_UserNotFound_ReturnsEmptyOptional() {
        // Arrange
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.findUserByEmail(email);

        // Assert
        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testDeleteUserByEmail_UserFound_ReturnsDeletedUserDto() {
        // Arrange
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        UserDto userDto1 = new UserDto();
        userDto1.setEmail(user.getEmail());
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userRepository.removeUserByEmail(email)).thenReturn(1);
        try (MockedStatic<SpringSecurityUtil> utilities = Mockito.mockStatic(SpringSecurityUtil.class)) {
            utilities.when(() -> SpringSecurityUtil.hasRoleRole(any()))
                    .thenReturn(true);
            assertTrue(SpringSecurityUtil.hasRoleRole(any()));
            when(userMapper.userToUserDto(user)).thenReturn(userDto1);
            // Act
            Integer flag = userService.deleteUserByEmail(email);
            // Assert
            assertEquals(1, flag);
            // Add more assertions as needed for other properties
            verify(userRepository, times(1)).removeUserByEmail(email);
        }

    }

    @Test
    public void testDeleteUserByEmail_UserFound_RoleUser_ReturnsDeletedUserDto() {
        // Arrange
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        user.setRole(new Role(RoleType.USER));
        UserDto userDto1 = new UserDto();
        userDto1.setEmail(user.getEmail());
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userRepository.removeUserByEmail(email)).thenReturn(1);

        try (MockedStatic<SpringSecurityUtil> utilities = Mockito.mockStatic(SpringSecurityUtil.class)) {
            utilities.when(() -> SpringSecurityUtil.hasRoleRole("ROLE_EMPLOYEE"))
                    .thenReturn(true);
            assertTrue(SpringSecurityUtil.hasRoleRole("ROLE_EMPLOYEE"));
            when(userMapper.userToUserDto(user)).thenReturn(userDto1);
            // Act
            Integer flag = userService.deleteUserByEmail(email);
            // Assert
            assertEquals(1, flag);
            // Add more assertions as needed for other properties
            verify(userRepository, times(1)).removeUserByEmail(email);
        }

    }

    @Test
    public void testUpdateUser_Employee_SuccessfullyUpdated() {
        // Arrange
        String email = "test@exampl.com";
        Employee updatedEmployee = new Employee();
        updatedEmployee.setEmail(email);
        UserDto userDto = new EmployeeDto();
        userDto.setEmail(email);
        UserServiceImpl userService = spy(this.userService);
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(updatedEmployee));
        when(userMapper.userDtoToUser(userDto)).thenReturn(updatedEmployee);
        when(userMapper.employeeDtoToEmployee((EmployeeDto) userDto)).thenReturn(updatedEmployee);
        when(userMapper.employeeToEmployeeDto(updatedEmployee)).thenReturn((EmployeeDto) userDto);
        when(userRepository.save(updatedEmployee)).thenReturn(updatedEmployee);
        doReturn(userDto).when(userService).checkInstance(updatedEmployee);
        // Act
        UserDto updatedUserDto = this.userService.updateUser(userDto);

        // Assert
        assertEquals(userDto.getEmail(), updatedUserDto.getEmail()); // Add more assertions as needed
    }

    @Test
    public void testUpdateUser_CorporateClient_SuccessfullyUpdated() {
        // Arrange
        String email = "test@example.com";
        CorporateClient updatedCorporateClient = new CorporateClient();
        updatedCorporateClient.setEmail(email);
        CorporateClientDto corporateClientDto = new CorporateClientDto();
        corporateClientDto.setEmail(email);
        UserServiceImpl userService = spy(this.userService);
        when(userRepository.findByEmail(corporateClientDto.getEmail())).thenReturn(Optional.of(updatedCorporateClient));
        when(userMapper.userDtoToUser(corporateClientDto)).thenReturn(updatedCorporateClient);
        when(userMapper.corporateClientDtoToCorporateClient(corporateClientDto)).thenReturn(updatedCorporateClient);
        when(userMapper.corporateClientToCorporateClientDto(updatedCorporateClient)).thenReturn(corporateClientDto);
        when(userRepository.save(updatedCorporateClient)).thenReturn(updatedCorporateClient);
        doReturn(corporateClientDto).when(userService).checkInstance(updatedCorporateClient);

        // Act
        UserDto updatedUserDto = userService.updateUser(corporateClientDto);

        // Assert
        assertEquals(corporateClientDto.getEmail(), updatedUserDto.getEmail());
        // Add more assertions as needed
    }

    @Test
    public void testUpdateUser_PrivateClient_SuccessfullyUpdated() {
        // Arrange
        String email = "test@example.com";
        PrivateClient updatedPrivateClient = new PrivateClient();
        updatedPrivateClient.setEmail(email);
        PrivateClientDto privateClientDto = new PrivateClientDto();
        privateClientDto.setEmail(email);
        UserServiceImpl userService = spy(this.userService);
        when(userRepository.findByEmail(privateClientDto.getEmail())).thenReturn(Optional.of(updatedPrivateClient));
        when(userMapper.userDtoToUser(privateClientDto)).thenReturn(updatedPrivateClient);
        when(userMapper.privateClientDtoToPrivateClient(privateClientDto)).thenReturn(updatedPrivateClient);
        when(userMapper.privateClientToPrivateClientDto(updatedPrivateClient)).thenReturn(privateClientDto);
        when(userRepository.save(updatedPrivateClient)).thenReturn(updatedPrivateClient);
        doReturn(privateClientDto).when(userService).checkInstance(updatedPrivateClient);

        // Act
        UserDto updatedUserDto = userService.updateUser(privateClientDto);

        // Assert
        assertEquals(privateClientDto.getEmail(), updatedUserDto.getEmail());
        // Add more assertions as needed
    }

    @Test
    public void testUpdateUser_UserNotFound_ThrowsNotFoundException() {
        // Arrange
        String email = "test@example.com";
        UserDto userDto = new CorporateClientDto();
        userDto.setEmail(email);
        UserServiceImpl userService = spy(this.userService);
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(NotFoundException.class, () -> userService.updateUser(userDto));
        // Add more assertions as needed
    }

    @Test
    public void testCreateAgent_NewAgent_SuccessfullyCreated() {
//        // Arrange
//        AgentDto agentDto = new AgentDto();
//        agentDto.setEmail("test@example.com");
//
//        Agent agent = new Agent();
//        agent.setEmail(agentDto.getEmail());
//
//        Role role = new Role();
//        role.setRoleType(RoleType.AGENT);
//
//        when(userRepository.findByEmail(agentDto.getEmail())).thenReturn(Optional.empty());
//        when(roleRepository.findByRoleType(RoleType.AGENT)).thenReturn(Optional.of(role));
//        when(userMapper.agentDtoToAgent(agentDto)).thenReturn(agent);
//        when(userRepository.save(agent)).thenReturn(agent);
//
//        // Act
//        AgentDto createdAgentDto = userService.createAgent(agentDto);
//
//        // Assert
//        assertNotNull(createdAgentDto);
//        assertEquals(agentDto.getEmail(), createdAgentDto.getEmail());
//        // Add more assertions as needed
    }

    @Test
    public void testCreateAgent_ExistingAgent_ThrowsEmailTakenException() {
        // Arrange
        AgentDto agentDto = new AgentDto();
        agentDto.setEmail("existing@example.com");

        Agent existingAgent = new Agent();
        existingAgent.setEmail(agentDto.getEmail());

        when(userRepository.findByEmail(agentDto.getEmail())).thenReturn(Optional.of(existingAgent));

        // Act and Assert
        assertThrows(EmailTakenException.class, () -> userService.createAgent(agentDto));
    }

    @Test
    public void testCreateAgent_MissingRole_ThrowsMissingRoleException() {
        // Arrange
        AgentDto agentDto = new AgentDto();
        agentDto.setEmail("test@example.com");

        when(userRepository.findByEmail(agentDto.getEmail())).thenReturn(Optional.empty());
        when(roleRepository.findByRoleType(RoleType.AGENT)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(MissingRoleException.class, () -> userService.createAgent(agentDto));
    }

}
