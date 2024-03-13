package rs.edu.raf.IAMService.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import rs.edu.raf.IAMService.data.dto.ClientActivationMessageDto;
import rs.edu.raf.IAMService.data.dto.CorporateClientDto;
import rs.edu.raf.IAMService.data.dto.PrivateClientDto;
import rs.edu.raf.IAMService.data.entites.*;
import rs.edu.raf.IAMService.data.enums.PermissionType;
import rs.edu.raf.IAMService.data.enums.RoleType;
import rs.edu.raf.IAMService.exceptions.UserNotFoundException;
import rs.edu.raf.IAMService.mapper.UserMapper;
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

    @Test
    public void createPrivateClient_validDto_returnsSavedPrivateClient() {
        // given
        PrivateClientDto requestDto = getPrivateClientDto();
        PrivateClientDto responseDto = getPrivateClientDto();
        responseDto.setId(1L);
        PrivateClient client = getPrivateClient();
        when(userMapper.privateClientDtoToPrivateClient(any(PrivateClientDto.class))).thenReturn(client);
        when(userRepository.save(any(PrivateClient.class))).thenReturn(client);
        when(userMapper.privateClientToPrivateClientDto(any(PrivateClient.class))).thenReturn(responseDto);

        // when
        PrivateClientDto result = userService.createPrivateClient(requestDto);

        // then
        verify(userRepository, times(1)).save(any(PrivateClient.class));
        verify(rabbitTemplate, times(1)).convertAndSend(eq("password-activation"), any(ClientActivationMessageDto.class));
        assertNotNull(result.getId());
        assertEquals(requestDto.getName(), result.getName());
        assertEquals(requestDto.getSurname(), result.getSurname());
        assertEquals(requestDto.getUsername(), result.getUsername());
        assertEquals(requestDto.getGender(), result.getGender());
        assertEquals(requestDto.getAddress(), result.getAddress());
        assertEquals(requestDto.getEmail(), result.getEmail());
        assertEquals(requestDto.getPhone(), result.getPhone());
        assertEquals(requestDto.getPrimaryAccountNumber(), result.getPrimaryAccountNumber());
        assertEquals(requestDto.getRole().getRole(), result.getRole().getRole());
        assertEquals(requestDto.getPermissions().get(0).getAuthority(), result.getPermissions().get(0).getAuthority());
    }

    @Test
    public void createCorporateClient_validDto_returnsSavedCorporateClient() {
        // given
        CorporateClientDto requestDto = getCorporateClientDto();
        CorporateClientDto responseDto = getCorporateClientDto();
        responseDto.setId(1L);
        CorporateClient client = new CorporateClient();
        when(userMapper.corporateClientDtoToCorporateClient(any(CorporateClientDto.class))).thenReturn(client);
        when(userRepository.save(any(CorporateClient.class))).thenReturn(client);
        when(userMapper.corporateClientToCorporateClientDto(any(CorporateClient.class))).thenReturn(responseDto);

        // when
        CorporateClientDto result = userService.createCorporateClient(requestDto);

        // then
        verify(userRepository, times(1)).save(client);
        verify(rabbitTemplate, times(1)).convertAndSend(eq("password-activation"), any(ClientActivationMessageDto.class));
        assertNotNull(result.getId());
        assertEquals(requestDto.getName(), result.getName());
        assertEquals(requestDto.getUsername(), result.getUsername());
        assertEquals(requestDto.getAddress(), result.getAddress());
        assertEquals(requestDto.getEmail(), result.getEmail());
        assertEquals(requestDto.getPhone(), result.getPhone());
        assertEquals(requestDto.getPrimaryAccountNumber(), result.getPrimaryAccountNumber());
        assertEquals(requestDto.getRole().getRole(), result.getRole().getRole());
        assertEquals(requestDto.getPermissions().get(0).getAuthority(), result.getPermissions().get(0).getAuthority());
    }

    @Test
    public void activateClient_userExists_updatePassword() {
        // given
        String clientId = "1";
        String password = "newPassword";
        User user = new User();
        user.setActive(false);
        when(userRepository.findById(Long.parseLong(clientId))).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(new User());

        // when
        userService.activateClient(clientId, password);

        // then
        assertTrue(user.isActive());
        assertEquals("encodedPassword", user.getPassword());
        verify(userRepository, times(1)).findById(any());
        verify(passwordEncoder, times(1)).encode(any());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void activateClient_userDoesNotExist_throwException() {
        // given
        String clientId = "1";
        String password = "newPassword";
        when(userRepository.findById(Long.parseLong(clientId))).thenReturn(Optional.empty());

        // then
        assertThrows(UserNotFoundException.class,
                () -> userService.activateClient(clientId, password));
        verify(userRepository, times(1)).findById(any());
        verify(passwordEncoder, times(0)).encode(any());
        verify(userRepository, times(0)).save(any());
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
        dto.setDateOfBirth(new Date());
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
        client.setDateOfBirth(new Date());
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
        dto.setDateOfBirth(new Date());
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
        client.setDateOfBirth(new Date());
        client.setPhone("test phone number");
        client.setUsername("test username");
        client.setPrimaryAccountNumber("test account number");
        client.setRole(new Role(RoleType.USER));
        client.setPermissions(List.of(new Permission(PermissionType.PERMISSION_1)));

        return client;
    }
}