package rs.edu.raf.IAMService.bootstrap;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.edu.raf.IAMService.data.entites.Employee;
import rs.edu.raf.IAMService.data.entites.Permission;
import rs.edu.raf.IAMService.data.entites.Role;
import rs.edu.raf.IAMService.data.entites.User;
import rs.edu.raf.IAMService.data.enums.PermissionType;
import rs.edu.raf.IAMService.data.enums.RoleType;
import rs.edu.raf.IAMService.repositories.PermissionRepository;
import rs.edu.raf.IAMService.repositories.RoleRepository;
import rs.edu.raf.IAMService.repositories.UserRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    @Value("${MY_EMAIL_1}")
    private String myEmail1;

    @Value("${MY_EMAIL_2}")
    private String myEmail2;

    private static final Logger logger = LoggerFactory.getLogger(BootstrapData.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        logger.info("DATA LOADING IN PROGRESS...");


        Role adminRole = new Role();
        adminRole.setRoleType(RoleType.ADMIN);
        roleRepository.save(adminRole);

        Role employeeRole = new Role();
        employeeRole.setRoleType(RoleType.EMPLOYEE);
        roleRepository.save(employeeRole);

        Role supervisorRole = new Role();
        supervisorRole.setRoleType(RoleType.SUPERVISOR);
        roleRepository.save(supervisorRole);

        Role agentRole = new Role();
        agentRole.setRoleType(RoleType.AGENT);
        roleRepository.save(agentRole);

        Role userRole = new Role();
        userRole.setRoleType(RoleType.USER);
        roleRepository.save(userRole);

        // ##############################

        Permission per1 = new Permission();
        per1.setPermissionType(PermissionType.PERMISSION_1);
        permissionRepository.save(per1);

        Permission per2 = new Permission();
        per2.setPermissionType(PermissionType.PERMISSION_2);
        permissionRepository.save(per2);

        User admin = new User();
        admin.setEmail(myEmail1);
        admin.setUsername(myEmail1);
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setRole(userRole);
        admin.setPermissions(List.of(per1, per2));
        userRepository.save(admin);

        Employee employee1 = new Employee();
        employee1.setEmail(myEmail2);
        employee1.setActive(true);
        employee1.setUsername(myEmail2);
        employee1.setPassword(passwordEncoder.encode("employee1"));
        employee1.setRole(userRole);
        employee1.setPermissions(List.of(per1, per2));
        userRepository.save(employee1);

        logger.info("DATA LOADING FINISHED...");
    }
}