package rs.edu.raf.IAMService.services.impl;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import rs.edu.raf.IAMService.data.dto.*;
import rs.edu.raf.IAMService.data.entites.CorporateClient;
import rs.edu.raf.IAMService.data.entites.PrivateClient;
import rs.edu.raf.IAMService.data.entites.User;
import rs.edu.raf.IAMService.exceptions.UserNotFoundException;
import rs.edu.raf.IAMService.mapper.UserMapper;
import rs.edu.raf.IAMService.repositories.UserRepository;
import rs.edu.raf.IAMService.services.UserService;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RabbitTemplate rabbitTemplate;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username: " + username + " not found."));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

    @Override
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User with username: " + email + " not found."));
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
}
