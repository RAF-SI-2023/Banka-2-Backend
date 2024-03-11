package rs.edu.raf.IAMService.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.edu.raf.IAMService.data.entites.Role;
import rs.edu.raf.IAMService.data.entites.User;
import rs.edu.raf.IAMService.data.enums.RoleType;
import rs.edu.raf.IAMService.repositories.RoleRepository;
import rs.edu.raf.IAMService.repositories.UserRepository;

@Component
public class BootstrapData implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(BootstrapData.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    public BootstrapData(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }


    @Override
    public void run(String... args) throws Exception {

        logger.info("DATA LOADING IN PROGRESS...");

        Role role = new Role(RoleType.USER);

        roleRepository.save(role);


        User user1 = new User();
        user1.setUsername("lol");
        user1.setPassword(this.passwordEncoder.encode("lol"));
        user1.setEmail("lol");
        user1.setAddress("lol");
        user1.setRole(null);
        user1.setPhone("lol");
        user1.setPermissions(null);
        user1.setDateOfBirth(null);
        user1.setRole(role);


        this.userRepository.save(user1);

        logger.info("DATA LOADING FINISHED...");
    }
}
