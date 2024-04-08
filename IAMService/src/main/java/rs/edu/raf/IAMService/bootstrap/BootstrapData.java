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

        User user = new User();
        user.setEmail("nikola@gmail.com");
        user.setUsername("nikola@gmail.com");
        user.setPassword(passwordEncoder.encode("Nikola123!"));
        user.setRole(userRole);
        user.setPermissions(null);
        user.setPhone("+38111236456");
        user.setDateOfBirth(336779146L);
        user.setAddress("Trg Republike V/5, Beograd, Srbija");
        users.add(user);

        Employee employee1 = new Employee();
        employee1.setEmail("lazar@gmail.com");
        employee1.setSurname("Jankovic");
        employee1.setActive(true);
        employee1.setUsername("lazar@gmail.com");
        employee1.setName("Lazar");
        employee1.setDateOfBirth(511739146L);
        employee1.setPassword(passwordEncoder.encode("employee"));
        employee1.setRole(employeeRole);
        employee1.setPermissions(List.of(per1, per2));
        employee1.setGender("M");
        employee1.setDepartment("IT");
        employee1.setPhone("+38111236456");
        employee1.setAddress("Pariske komune 5, Beograd, Srbija");
        employee1.setPosition("Software Developer");
        users.add(employee1);


        Employee employee2 = new Employee();
        employee2.setEmail("mirkomail@gmail.com");
        employee2.setName("Mirko");
        employee2.setSurname("Markovic");
        employee2.setGender("M");
        employee2.setActive(false);
        employee2.setDateOfBirth(606433546L);
        employee2.setUsername("mirkomail@gmail.com");
        employee2.setPassword(passwordEncoder.encode("Mirko123!"));
        employee2.setRole(employeeRole);
        employee2.setPermissions(List.of(per1));
        employee2.setDepartment("HR");
        employee2.setPosition("HR Manager");
        employee2.setPhone("+38111239531");
        employee2.setAddress("Dr Huga Klana 1, Beograd, Srbija");
        users.add(employee2);


        Agent agent1 = new Agent();
        agent1.setEmail("dusan@gmail.com");
        agent1.setUsername("dusan@gmail.com");
        agent1.setPassword(passwordEncoder.encode("Dusan123!"));
        agent1.setRole(agentRole);
        agent1.setPhone("+38111317456");
        agent1.setAddress("Juriga Gargarina 3, Beograd, Srbija");
        agent1.setDateOfBirth(204155146L);
        agent1.setLimit(new BigDecimal("12345.67"));
        agent1.setLeftOfLimit(new BigDecimal("578.42"));
        users.add(agent1);

        Employee employee3 = new Employee();
        employee3.setEmail("ana@gmail.com");
        employee3.setSurname("Petrovic");
        employee3.setActive(true);
        employee3.setUsername("ana@gmail.com");
        employee3.setName("Ana");
        employee3.setDateOfBirth(473765600L);
        employee3.setPassword(passwordEncoder.encode("employee"));
        employee3.setRole(employeeRole);
        employee3.setPermissions(List.of(per1, per2));
        employee3.setGender("F");
        employee3.setDepartment("Marketing");
        employee3.setPhone("+38111236457");
        employee3.setAddress("Nemanjina 5, Beograd, Srbija");
        employee3.setPosition("Marketing Specialist");
        users.add(employee3);

        Agent agent2 = new Agent();
        agent2.setEmail("lana@gmail.com");
        agent2.setDateOfBirth(216596746L);
        agent2.setPhone("+38111236456");
        agent2.setUsername("lana@gmail.com");
        agent2.setPassword(passwordEncoder.encode("agent"));
        agent2.setRole(agentRole);
        agent2.setLimit(new BigDecimal("22378.55"));
        agent2.setLeftOfLimit(new BigDecimal("1063.31"));
        agent2.setAddress("Bulevar Kralja Aleksandra 5, Beograd, Srbija");
        users.add(agent2);

        Agent agent3 = new Agent();
        agent3.setEmail("peri@gmail.com");
        agent3.setUsername("peri@gmail.com");
        agent3.setPhone("+38111435156");
        agent3.setDateOfBirth(344555146L);
        agent3.setAddress("Kralja Petra 11, Beograd, Srbija");
        agent3.setPassword(passwordEncoder.encode("agent"));
        agent3.setRole(agentRole);
        agent3.setLimit(new BigDecimal("36890.67"));
        agent3.setLeftOfLimit(new BigDecimal("1578.87"));
        users.add(agent3);

        Supervisor supervisor = new Supervisor();
        supervisor.setEmail("milos@gmail.com");
        supervisor.setUsername("milos@gmail.com");
        supervisor.setPhone("+38695380456");
        supervisor.setDateOfBirth(511739146L);
        supervisor.setAddress("Fontana V/5, Beograd, Srbija");
        supervisor.setPassword(passwordEncoder.encode("supervisor"));
        supervisor.setRole(supervisorRole);
        supervisor.setPermissions(List.of(per1, per2));
        users.add(supervisor);

        Supervisor supervisor2 = new Supervisor();
        supervisor2.setEmail("andrej@gmail.com");
        supervisor2.setUsername("andrej@gmail.com");
        supervisor2.setPassword(passwordEncoder.encode("Andrej123!"));
        supervisor2.setRole(supervisorRole);
        supervisor2.setDateOfBirth(925940746L);
        supervisor2.setPhone("+3811111637");
        supervisor2.setPermissions(List.of(per1));
        supervisor2.setAddress("Glavna 5, Zemun, Srbija");
        users.add(supervisor2);

        CorporateClient corporateClient = new CorporateClient();
        corporateClient.setEmail("vladimir@gmail.com");
        corporateClient.setUsername("vladimir@gmail.com");
        corporateClient.setPassword(passwordEncoder.encode("Vladimir123!"));
        corporateClient.setRole(userRole);
        corporateClient.setPhone("+38111236456");
        corporateClient.setDateOfBirth(915227146L);
        corporateClient.setAddress("Tosin bunar 21, Beograd, Srbija");
        corporateClient.setPermissions(List.of(per1, per2));
        corporateClient.setName("Corporate");
        corporateClient.setPrimaryAccountNumber("3334444999999999");
        users.add(corporateClient);

        CorporateClient corporateClient1 = new CorporateClient();
        corporateClient1.setEmail("milica@gmail.com");
        corporateClient1.setUsername("milica@gmail.com");
        corporateClient1.setPassword(passwordEncoder.encode("Milica123!"));
        corporateClient1.setDateOfBirth(954711946L);
        corporateClient1.setRole(userRole);
        corporateClient1.setPermissions(List.of(per2));
        corporateClient1.setPhone("+38111239905");
        corporateClient1.setName("Milica");
        corporateClient1.setAddress("Svetog Save 52, Beograd, Srbija");
        corporateClient1.setPrimaryAccountNumber("3334444999991234");
        users.add(corporateClient1);

        PrivateClient privateClient = new PrivateClient();
        privateClient.setEmail("andrija@gmail.com");
        privateClient.setUsername("andrija@gmail.com");
        privateClient.setPassword(passwordEncoder.encode("private"));
        privateClient.setRole(userRole);
        privateClient.setPhone("+38111234972");
        privateClient.setAddress("Bulevar Kralja Aleksandra 5, Beograd, Srbija");
        privateClient.setDateOfBirth(852241546L);
        privateClient.setPermissions(List.of(per1, per2));
        privateClient.setName("Andrija");
        privateClient.setSurname("Lekic");
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
    }

}