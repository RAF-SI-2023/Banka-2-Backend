package rs.edu.raf.IAMService.services.impl;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import rs.edu.raf.IAMService.data.dto.EmployeeDto;
import rs.edu.raf.IAMService.data.dto.PasswordChangeDto;
import rs.edu.raf.IAMService.data.dto.CorporateClientDto;
import rs.edu.raf.IAMService.data.dto.PrivateClientDto;
import rs.edu.raf.IAMService.data.dto.UserDto;
import rs.edu.raf.IAMService.data.entites.CorporateClient;
import rs.edu.raf.IAMService.data.entites.Employee;
import rs.edu.raf.IAMService.data.entites.PrivateClient;
import rs.edu.raf.IAMService.data.dto.*;
import rs.edu.raf.IAMService.data.entites.User;
import rs.edu.raf.IAMService.exceptions.UserNotFoundException;
import rs.edu.raf.IAMService.mapper.UserMapper;
import rs.edu.raf.IAMService.repositories.UserRepository;
import rs.edu.raf.IAMService.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RabbitTemplate rabbitTemplate;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, RabbitTemplate rabbitTemplate, ObjectMapper objectMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        this.passwordEncoder = passwordEncoder;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + " not found."));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

    @Override
    public UserDto findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User with id: " + id + " not found."));
        return checkInstance(user);
    }

    @Override
    public UserDto findByEmail(String email) throws NotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User with email: " + email + " not found."));
        return checkInstance(user);
    }
    public User employeeActivation(int id){
        Employee employee = (Employee) userRepository.findById(id).orElseThrow(() -> new NotFoundException("Employee with ID: " + id + " not found."));
        employee.setActive(true);
        return updateEntity(employee);
    }

    @Override
    public User employeeDeactivation(int id){
        Employee employee = (Employee) userRepository.findById(id).orElseThrow(() -> new NotFoundException("Employee with ID: " + id + " not found."));
        employee.setActive(false);
        return updateEntity(employee);
    }

    @Override
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        return new ArrayList<>(users.stream().map(this::checkInstance).toList());
    }

    @Override
    public UserDto deleteUserByEmail(String email) {
        User u = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User with email: " + email + " not found."));
        userRepository.removeUserByEmail(email);
        return checkInstance(u);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail()).orElseThrow(() -> new NotFoundException("User with email: " + userDto.getEmail() + " not found."));
        if (user instanceof Employee && userDto instanceof EmployeeDto) {
            Employee employee = userMapper.employeeDtoToEmployee((EmployeeDto) userDto);
            ((Employee) user).setDepartment(employee.getDepartment());
            ((Employee) user).setPosition(employee.getPosition());
            ((Employee) user).setGender(employee.getGender());
            ((Employee) user).setName(employee.getName());
            ((Employee) user).setSurname(employee.getSurname());

        } else if (user instanceof CorporateClient && userDto instanceof CorporateClientDto) {
            CorporateClient corporateClient = userMapper.corporateClientDtoToCorporateClient((CorporateClientDto) userDto);
            ((CorporateClient) user).setName(corporateClient.getName());
            ((CorporateClient) user).setName(corporateClient.getName());

        } else if (user instanceof PrivateClient && userDto instanceof PrivateClientDto) {
            PrivateClient privateClient = userMapper.privateClientDtoToPrivateClient((PrivateClientDto) userDto);
            ((PrivateClient) user).setSurname(privateClient.getSurname());
            ((PrivateClient) user).setName(privateClient.getName());
            ((PrivateClient) user).setGender(privateClient.getGender());

        }
        User u = userMapper.userDtoToUser(userDto);
        user.setPhone(u.getPhone());
        user.setAddress(u.getAddress());
        user.setDateOfBirth(u.getDateOfBirth());
        return checkInstance(userRepository.save(user));
    }

    public UserDto checkInstance(User user) {

        if (user instanceof Employee)
            return userMapper.employeeToEmployeeDto((Employee) user);
        if (user instanceof CorporateClient)
            return userMapper.corporateClientToCorporateClientDto((CorporateClient) user);
        if (user instanceof PrivateClient)
            return userMapper.privateClientToPrivateClientDto((PrivateClient) user);
        return userMapper.userToUserDto(user);
    }

    @Transactional
    @Override
    public PrivateClientDto createPrivateClient(PrivateClientDto privateClientDto) {
        PrivateClient client = userMapper
                .privateClientDtoToPrivateClient(privateClientDto);
        PrivateClient savedClient = userRepository.save(client);

        sendClientActivationMessage(savedClient.getEmail());

        return userMapper.privateClientToPrivateClientDto(savedClient);
    }

    @Transactional
    @Override
    public CorporateClientDto createCorporateClient(CorporateClientDto corporateClientDto) {
        CorporateClient client = userMapper
                .corporateClientDtoToCorporateClient(corporateClientDto);
        CorporateClient savedClient = userRepository.save(client);

        sendClientActivationMessage(savedClient.getEmail());

        return userMapper.corporateClientToCorporateClientDto(savedClient);
    }

    @Transactional
    @Override
    public Long activateClient(String clientId, String password) {
        User clientToBeActivated = userRepository.findById(Long.parseLong(clientId))
                .orElseThrow(() -> new UserNotFoundException("User with id: " + clientId + " not found."));

        clientToBeActivated.setPassword(passwordEncoder.encode(password));
        clientToBeActivated.setActive(true);

        return userRepository.save(clientToBeActivated).getId();
    }

    private void sendClientActivationMessage(String email) {
        rabbitTemplate.convertAndSend(
                "password-activation",
                new ClientActivationMessageDto("url", email));
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void sendToQueue(String email, String urlLink) {
        PasswordChangeDto passwordChangeDto = new PasswordChangeDto();
        passwordChangeDto.setEmail(email);
        passwordChangeDto.setUrlLink(urlLink);
        try {
            String json = objectMapper.writeValueAsString(passwordChangeDto);
            rabbitTemplate.convertAndSend("password-change-queue", json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User updateEntity(User user) {
        return this.userRepository.save(user);
    }


}
