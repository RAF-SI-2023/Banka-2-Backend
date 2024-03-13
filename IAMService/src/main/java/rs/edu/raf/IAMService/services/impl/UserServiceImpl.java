package rs.edu.raf.IAMService.services.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import rs.edu.raf.IAMService.data.dto.ActivationRequestDto;
import rs.edu.raf.IAMService.data.dto.EmployeeDto;
import rs.edu.raf.IAMService.data.dto.UserDto;
import rs.edu.raf.IAMService.data.entites.Employee;
import rs.edu.raf.IAMService.data.entites.Role;
import rs.edu.raf.IAMService.data.entites.User;
import rs.edu.raf.IAMService.data.enums.RoleType;
import rs.edu.raf.IAMService.exceptions.EmailNotFoundException;
import rs.edu.raf.IAMService.exceptions.EmailTakenException;
import rs.edu.raf.IAMService.exceptions.MissingRoleException;
import rs.edu.raf.IAMService.mapper.UserMapper;
import rs.edu.raf.IAMService.repositories.RoleRepository;
import rs.edu.raf.IAMService.repositories.UserRepository;
import rs.edu.raf.IAMService.services.UserService;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final RabbitTemplate rabbitTemplate;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + " not found."));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

    @Override
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EmailNotFoundException(email));
        return userMapper.userToUserDto(user);
    }

    @Override
    public UserDto createAdmin(UserDto userDto, String password) {
        User user = userMapper.userDtoToUser(userDto);
        user.setPassword(password);
        user.setRole(roleRepository.save(userDto.getRole()));
        roleRepository.save(new Role(RoleType.EMPLOYEE));
        return userMapper.userToUserDto(userRepository.save(user));
    }

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        if (userRepository.findByEmail(employeeDto.getEmail()).isPresent())
            throw new EmailTakenException(employeeDto.getEmail());

        Employee newEmployee = userMapper.employeeDtoToEmployee(employeeDto);
        newEmployee.setRole(roleRepository.findByRoleType(RoleType.EMPLOYEE)
                .orElseThrow(() -> new MissingRoleException(RoleType.EMPLOYEE)));

        newEmployee = userRepository.save(newEmployee);
        sendToQueue(newEmployee);

        return userMapper.employeeToEmployeeDto(newEmployee);
    }

    private void sendToQueue(Employee employee) {
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
}
