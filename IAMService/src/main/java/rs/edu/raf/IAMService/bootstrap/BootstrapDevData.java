package rs.edu.raf.IAMService.bootstrap;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
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
@Profile("dev")
public class BootstrapDevData implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(BootstrapDevData.class);
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


    public void run(String... args) throws Exception {

        logger.info("DATA LOADING IN PROGRESS...");

        Role adminRole = roleRepository.findByRoleType(RoleType.ADMIN).get();
        Role employeeRole = roleRepository.findByRoleType(RoleType.EMPLOYEE).get();
        Role supervisorRole = roleRepository.findByRoleType(RoleType.SUPERVISOR).get();
        Role agentRole = roleRepository.findByRoleType(RoleType.AGENT).get();
        Role userRole = roleRepository.findByRoleType(RoleType.USER).get();

        Permission per1 = permissionRepository.findByPermissionType(PermissionType.PERMISSION_1).get();
        Permission per2 = permissionRepository.findByPermissionType(PermissionType.PERMISSION_2).get();

        // ##############################
        // #     DODAVANJE USER-a       #
        // ##############################

        // ##############################
        // #          COMPANIES         #
        // ##############################
        if (companyRepository.count() == 0) {
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
        }


/*

    PODACI ISPOD SU NE-TESTNI PODACI
    PODACI ISPOD SU NE-TESTNI PODACI
    PODACI ISPOD SU NE-TESTNI PODACI
    PODACI ISPOD SU NE-TESTNI PODACI

    NIJE DOZVOLJENO MENJANJE POSTOJEĆIH PODATAKA !!!!!!!!!!!!!!!
    NIJE DOZVOLJENO MENJANJE POSTOJEĆIH PODATAKA !!!!!!!!!!!!!!!
    NIJE DOZVOLJENO MENJANJE POSTOJEĆIH PODATAKA !!!!!!!!!!!!!!!
    NIJE DOZVOLJENO MENJANJE POSTOJEĆIH PODATAKA !!!!!!!!!!!!!!!

    NIJE DOZVOLJENO JE DODAVANJE NOVIH PODATAKA !!!!!!!!!!!!!!!
    NIJE DOZVOLJENO JE DODAVANJE NOVIH PODATAKA !!!!!!!!!!!!!!!
    NIJE DOZVOLJENO JE DODAVANJE NOVIH PODATAKA !!!!!!!!!!!!!!!
    NIJE DOZVOLJENO JE DODAVANJE NOVIH PODATAKA !!!!!!!!!!!!!!!

*/

/*
################################################################################################################
                    USERS
################################################################################################################
*/

        User admin = new User();
        admin.setEmail(myEmail1);
        admin.setUsername(myEmail1);
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setRole(adminRole);
        admin.setPermissions(List.of(per1, per2));

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

        Supervisor supervisor = new Supervisor();
        supervisor.setEmail("milos@gmail.com");
        supervisor.setUsername("milos@gmail.com");
        supervisor.setPhone("+38695380456");
        supervisor.setDateOfBirth(511739146L);
        supervisor.setAddress("Fontana V/5, Beograd, Srbija");
        supervisor.setPassword(passwordEncoder.encode("supervisor"));
        supervisor.setRole(supervisorRole);
        supervisor.setPermissions(List.of(per1, per2));

        Supervisor supervisor2 = new Supervisor();
        supervisor2.setEmail("andrej@gmail.com");
        supervisor2.setUsername("andrej@gmail.com");
        supervisor2.setPassword(passwordEncoder.encode("Andrej123!"));
        supervisor2.setRole(supervisorRole);
        supervisor2.setDateOfBirth(925940746L);
        supervisor2.setPhone("+3811111637");
        supervisor2.setPermissions(List.of(per1));
        supervisor2.setAddress("Glavna 5, Zemun, Srbija");

        PrivateClient privateClient = new PrivateClient();
        privateClient.setEmail(myEmail2);
        privateClient.setUsername(myEmail2);
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

        PrivateClient privateClient1 = new PrivateClient();
        privateClient1.setEmail(myEmail3);
        privateClient1.setUsername(myEmail3);
        privateClient1.setPassword(passwordEncoder.encode("private"));
        privateClient1.setRole(userRole);
        privateClient1.setPhone("+38111234872");
        privateClient1.setAddress("Bulevar Kralja Aleksandra 3, Novi Sad, Srbija");
        privateClient1.setDateOfBirth(852241546L);
        privateClient1.setPermissions(List.of(per1, per2));
        privateClient1.setName("Bogdan");
        privateClient1.setSurname("Nastasic");
        privateClient1.setGender("M");
        privateClient1.setPrimaryAccountNumber("1112222333333333");

        saveUserIfNotExists(admin);
        saveUserIfNotExists(employee1);
        saveUserIfNotExists(employee2);
        saveUserIfNotExists(agent1);
        saveUserIfNotExists(agent2);
        saveUserIfNotExists(agent3);
        saveUserIfNotExists(supervisor);
        saveUserIfNotExists(supervisor2);
        saveUserIfNotExists(privateClient);
        saveUserIfNotExists(privateClient1);


    }

    private void saveUserIfNotExists(User user) {
        if (userRepository.findByEmail(user.getEmail()).isEmpty()) {
            userRepository.save(user);
        }
    }

}