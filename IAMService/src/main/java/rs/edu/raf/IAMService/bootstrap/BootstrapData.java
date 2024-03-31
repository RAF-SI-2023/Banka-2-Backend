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
import java.util.List;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    @Value("${MY_EMAIL_1}")
    private String myEmail1;

    @Value("${MY_EMAIL_2}")
    private String myEmail2;

    @Value("${MY_EMAIL_3}")
    private String myEmail3;

    @Value("${MY_EMAIL_4}")
    private String myEmail4;

    private static final Logger logger = LoggerFactory.getLogger(BootstrapData.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyRepository companyRepository;

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
        admin.setRole(adminRole);
        admin.setPermissions(List.of(per1, per2));
        userRepository.save(admin);

        Employee employee1 = new Employee();
        employee1.setEmail(myEmail2);
        employee1.setActive(true);
        employee1.setUsername(myEmail2);
        employee1.setPassword(passwordEncoder.encode("employee"));
        employee1.setRole(employeeRole);
        employee1.setPermissions(List.of(per1, per2));
        userRepository.save(employee1);

        Agent agent = new Agent();
        agent.setEmail("agent@gmail.com");
        agent.setUsername("agent@gmail.com");
        agent.setPassword(passwordEncoder.encode("agent"));
        agent.setRole(agentRole);
        agent.setLimit(new BigDecimal("12345.67"));
        agent.setLeftOfLimit(new BigDecimal("578.42"));
        employee1.setPermissions(List.of(per1, per2));
        userRepository.save(agent);

        Supervisor supervisor = new Supervisor();
        supervisor.setEmail("supervisor@gmail.com");
        supervisor.setUsername("supervisor@gmail.com");
        supervisor.setPassword(passwordEncoder.encode("supervisor"));
        supervisor.setRole(supervisorRole);
        employee1.setPermissions(List.of(per1, per2));
        userRepository.save(supervisor);

        CorporateClient corporateClient= new CorporateClient();
        corporateClient.setEmail(myEmail3);
        corporateClient.setUsername(myEmail3);
        corporateClient.setPassword(passwordEncoder.encode("corporate"));
        corporateClient.setRole(userRole);
        corporateClient.setPermissions(List.of(per1, per2));
        corporateClient.setName("Miladin");
        corporateClient.setPrimaryAccountNumber("3334444999999999");
        userRepository.save(corporateClient);

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
        userRepository.save(privateClient);

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