package rs.edu.raf.IAMService.bootstrap;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.edu.raf.IAMService.data.entites.*;
import rs.edu.raf.IAMService.data.enums.PermissionType;
import rs.edu.raf.IAMService.data.enums.RoleType;
import rs.edu.raf.IAMService.repositories.CompanyRepository;
import rs.edu.raf.IAMService.repositories.PermissionRepository;
import rs.edu.raf.IAMService.repositories.RoleRepository;
import rs.edu.raf.IAMService.repositories.UserRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(BootstrapData.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyRepository companyRepository;
    @Value("${MY_EMAIL_1}")
    private String myEmail1;
    @Value("${MY_EMAIL_2}")
    private String myEmail2;
    @Value("${MY_EMAIL_3}")
    private String myEmail3;
    @Value("${MY_EMAIL_4}")
    private String myEmail4;

    @Override
    public void run(String... args) throws Exception {

        logger.info("DATA LOADING IN PROGRESS...");
        List<Role> roles = new ArrayList<>();
        Role adminRole = new Role();
        adminRole.setRoleType(RoleType.ADMIN);
        roles.add(adminRole);

        Role employeeRole = new Role();
        employeeRole.setRoleType(RoleType.EMPLOYEE);
        roles.add(employeeRole);

        Role supervisorRole = new Role();
        supervisorRole.setRoleType(RoleType.SUPERVISOR);
        roles.add(supervisorRole);

        Role agentRole = new Role();
        agentRole.setRoleType(RoleType.AGENT);
        roles.add(agentRole);

        Role userRole = new Role();
        userRole.setRoleType(RoleType.USER);
        roles.add(userRole);
        if (roleRepository.count() == 0) {
            roleRepository.saveAll(roles);
        }
        // ##############################
        // #        PERMISSIONS         #
        // ##############################
        List<Permission> permissions = new ArrayList<>();
        Permission per1 = new Permission();
        per1.setPermissionType(PermissionType.PERMISSION_1);
        permissions.add(per1);

        Permission per2 = new Permission();
        per2.setPermissionType(PermissionType.PERMISSION_2);
        permissions.add(per2);
        if (permissionRepository.count() == 0) {
            permissionRepository.saveAll(permissions);
        }
        // ##############################
        // #           USERS            #
        // ##############################
        List<User> users = new ArrayList<>();
        User admin = new User();
        admin.setEmail(myEmail1);
        admin.setUsername(myEmail1);
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setRole(adminRole);
        admin.setPermissions(List.of(per1, per2));
        users.add(admin);

        Employee employee1 = new Employee();
        employee1.setEmail(myEmail2);
        employee1.setActive(true);
        employee1.setUsername(myEmail2);
        employee1.setPassword(passwordEncoder.encode("employee"));
        employee1.setRole(employeeRole);
        employee1.setPermissions(List.of(per1, per2));
        users.add(employee1);

        Agent agent1 = new Agent();
        agent1.setEmail("agent1@gmail.com");
        agent1.setUsername("agent1@gmail.com");
        agent1.setPassword(passwordEncoder.encode("agent"));
        agent1.setRole(agentRole);
        agent1.setLimit(new BigDecimal("12345.67"));
        agent1.setLeftOfLimit(new BigDecimal("578.42"));
        users.add(agent1);

        Agent agent2 = new Agent();
        agent2.setEmail("agent2@gmail.com");
        agent2.setUsername("agent2@gmail.com");
        agent2.setPassword(passwordEncoder.encode("agent"));
        agent2.setRole(agentRole);
        agent2.setLimit(new BigDecimal("22378.55"));
        agent2.setLeftOfLimit(new BigDecimal("1063.31"));
        users.add(agent2);

        Agent agent3 = new Agent();
        agent3.setEmail("agent3@gmail.com");
        agent3.setUsername("agent3@gmail.com");
        agent3.setPassword(passwordEncoder.encode("agent"));
        agent3.setRole(agentRole);
        agent3.setLimit(new BigDecimal("36890.67"));
        agent3.setLeftOfLimit(new BigDecimal("1578.87"));
        users.add(agent3);

        Supervisor supervisor = new Supervisor();
        supervisor.setEmail("supervisor@gmail.com");
        supervisor.setUsername("supervisor@gmail.com");
        supervisor.setPassword(passwordEncoder.encode("supervisor"));
        supervisor.setRole(supervisorRole);
        users.add(supervisor);

        CorporateClient corporateClient = new CorporateClient();
        corporateClient.setEmail(myEmail3);
        corporateClient.setUsername(myEmail3);
        corporateClient.setPassword(passwordEncoder.encode("corporate"));
        corporateClient.setRole(userRole);
        corporateClient.setPermissions(List.of(per1, per2));
        corporateClient.setName("Miladin");
        corporateClient.setPrimaryAccountNumber("3334444999999999");
        users.add(corporateClient);

        PrivateClient privateClient = new PrivateClient();
        privateClient.setEmail(myEmail4);
        privateClient.setUsername(myEmail4);
        privateClient.setPassword(passwordEncoder.encode("private"));
        privateClient.setRole(userRole);
        privateClient.setPermissions(List.of(per1, per2));
        privateClient.setName("Zvezdanko");
        privateClient.setSurname("Zvezdankovic");
        privateClient.setGender("M");
        privateClient.setPrimaryAccountNumber("3334444111111111");
        users.add(privateClient);

        if (userRepository.count() == 0) {
            userRepository.saveAll(users);
        }


        // ##############################
        // #          COMPANIES         #
        // ##############################
        if (companyRepository.count() != 0) {
            return;
        }
        Company company = new Company();
        company.setCompanyName("Example Ltd.");
        company.setFaxNumber("123456");
        company.setPhoneNumber("+38111236456");
        company.setAddress("Trg Republike V/5, Beograd, Srbija");
        company.setPib(123456789L);
        company.setRegistryNumber(123456789);
        company.setIdentificationNumber(123456);
        company.setActivityCode(12345);
        companyRepository.save(company);

        logger.info("DATA LOADING FINISHED...");
    }
}