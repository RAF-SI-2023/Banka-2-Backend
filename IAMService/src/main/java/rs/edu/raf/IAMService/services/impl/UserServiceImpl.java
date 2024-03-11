package rs.edu.raf.IAMService.services.impl;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import rs.edu.raf.IAMService.data.dto.PasswordChangeDto;
import rs.edu.raf.IAMService.data.dto.UserDto;
import rs.edu.raf.IAMService.data.entites.User;
import rs.edu.raf.IAMService.mapper.UserMapper;
import rs.edu.raf.IAMService.repositories.UserRepository;
import rs.edu.raf.IAMService.services.UserService;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, RabbitTemplate rabbitTemplate) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + " not found."));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

    @Override
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User with username: " + email + " not found."));
        return userMapper.userToUserDto(user);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {


        return userRepository.findByEmail(email);
    }


    @Override
    public void sendToQueue(String email,String urlLink) {
        PasswordChangeDto passwordChangeDto = new PasswordChangeDto();
        passwordChangeDto.setEmail(email);
        passwordChangeDto.setUrlLink(urlLink);
        rabbitTemplate.convertAndSend("password-change-queue", passwordChangeDto);
    }

    @Override
    public User updateEntity(User user) {

        return this.userRepository.save(user);
    }


}
