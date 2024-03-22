package rs.edu.raf.IAMService.services.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import rs.edu.raf.IAMService.data.dto.*;
import rs.edu.raf.IAMService.data.entites.*;
import rs.edu.raf.IAMService.data.enums.RoleType;
import rs.edu.raf.IAMService.exceptions.EmailNotFoundException;
import rs.edu.raf.IAMService.exceptions.EmailTakenException;
import rs.edu.raf.IAMService.exceptions.MissingRoleException;
import rs.edu.raf.IAMService.exceptions.UserNotFoundException;
import rs.edu.raf.IAMService.mapper.UserMapper;
import rs.edu.raf.IAMService.repositories.RoleRepository;
import rs.edu.raf.IAMService.repositories.UserRepository;
import rs.edu.raf.IAMService.services.UserService;
import rs.edu.raf.IAMService.utils.SpringSecurityUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final RabbitTemplate rabbitTemplate;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, RoleRepository roleRepository, RabbitTemplate rabbitTemplate, ObjectMapper objectMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        this.passwordEncoder = passwordEncoder;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + " not found."));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

    @Override
    public UserDto findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User with id: " + id + " not found."));
        return checkInstance(user);
    }

    @Override
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User with email: " + email + " not found."));
        return checkInstance(user);
    }

    @Override
    public User employeeActivation(int id) {
        Employee employee = (Employee) userRepository.findById(id).orElseThrow(() -> new NotFoundException("Employee with ID: " + id + " not found."));
        employee.setActive(true);
        return updateEntity(employee);
    }

    @Override
    public User employeeDeactivation(int id) {
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
    public Integer deleteUserByEmail(String email) {
        if (SpringSecurityUtil.hasRoleRole("ROLE_ADMIN")) {
            return userRepository.removeUserByEmail(email);
        }
        if (SpringSecurityUtil.hasRoleRole("ROLE_EMPLOYEE")) {
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getRole().getRoleType() == RoleType.USER) {
                    return userRepository.removeUserByEmail(email);
                }
            }
        }
        if (SpringSecurityUtil.hasRoleRole("ROLE_USER")) {
            if (SpringSecurityUtil.getPrincipalEmail().equals(email)) {
                return userRepository.removeUserByEmail(email);
            }
        }
        return -1;
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

        Role role = roleRepository.findByRoleType(RoleType.USER)
                .orElseThrow(() -> new MissingRoleException("USER"));

        client.setRole(role);

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

        Role role = roleRepository.findByRoleType(RoleType.USER)
                .orElseThrow(() -> new MissingRoleException("USER"));

        client.setRole(role);

        sendClientActivationMessage(savedClient.getEmail());

        return userMapper.corporateClientToCorporateClientDto(savedClient);
    }

    @Transactional
    @Override
    public Long passwordActivation(String email, String password) {
        User clientToBeActivated = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email: " + email + " not found."));
        clientToBeActivated.setPassword(passwordEncoder.encode(password));
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
    public List<Permission> getUserPermissions(Long id) {
        List<Permission> permissionList = new ArrayList<>(userRepository.findById(id).get().getPermissions());
        return permissionList;
    }

    @Override
    public void addUserPermission(Long id, Permission permission) {
        List<Permission> permissionList = getUserPermissions(id);
        permissionList.add(permission);
    }

    @Override
    public void removeUserPermission(Long id, Permission permission) {
        List<Permission> permissionList = getUserPermissions(id);
        permissionList.remove(permission);
    }

    @Override
    public void deleteAndSetUserPermissions(Long id, List<Permission> permissionList) {
        getUserPermissions(id).clear();
        getUserPermissions(id).addAll(permissionList);
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
    public void PasswordResetsendToQueue(String email, String urlLink) {
        PasswordChangeDto passwordChangeDto = new PasswordChangeDto();
        passwordChangeDto.setEmail(email);
        passwordChangeDto.setUrlLink(urlLink);
        try {
            String json = objectMapper.writeValueAsString(passwordChangeDto);
            rabbitTemplate.convertAndSend("password-forgot", json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User updateEntity(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        if (userRepository.findByEmail(employeeDto.getEmail()).isPresent())
            throw new EmailTakenException(employeeDto.getEmail());

        Employee newEmployee = userMapper.employeeDtoToEmployee(employeeDto);
        newEmployee.setRole(roleRepository.findByRoleType(RoleType.EMPLOYEE)
                .orElseThrow(() -> new MissingRoleException("EMPLOYEE")));

        newEmployee = userRepository.save(newEmployee);
        sendToActivationQueue(newEmployee);

        return userMapper.employeeToEmployeeDto(newEmployee);
    }

    public void sendToActivationQueue(Employee employee) {
        ActivationRequestDto activationRequestDto = new ActivationRequestDto();
        activationRequestDto.setEmail(employee.getEmail());
        activationRequestDto.setActivationUrl("https://google.com");
        rabbitTemplate.convertAndSend("password-activation", activationRequestDto);
    }

    @Override
    public EmployeeDto activateEmployee(String email, String password) {
        Employee employee = userRepository.findEmployeeByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException(email));

        employee.setPassword(passwordEncoder.encode(password));
        employee.setActive(true);
        employee = userRepository.save(employee);
        return userMapper.employeeToEmployeeDto(employee);
    }

    @Override
    public AgentDto createAgent(AgentDto agentDto) {
        if (userRepository.findByEmail(agentDto.getEmail()).isPresent())
            throw new EmailTakenException(agentDto.getEmail());

        Agent agent = userMapper.agentDtoToAgent(agentDto);
        agent.setRole(roleRepository.findByRoleType(RoleType.AGENT)
                .orElseThrow(() -> new MissingRoleException("AGENT")));

        agent = userRepository.save(agent);

        ActivationRequestDto activationRequestDto = new ActivationRequestDto();
        activationRequestDto.setEmail(agent.getEmail());
        activationRequestDto.setActivationUrl("https://google.com");
        rabbitTemplate.convertAndSend("password-activation", activationRequestDto);

        return userMapper.agentToAgentDto(agent);
    }

    @Override
    public User setAgentLimit(int id, BigDecimal limit) {
        Agent agent = (Agent) userRepository.findById(id).orElseThrow(() -> new NotFoundException("Agent with ID: " + id + " not found."));
        agent.setLimit(limit);
        return updateEntity(agent);
    }
}
