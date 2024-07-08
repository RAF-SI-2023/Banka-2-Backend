package rs.edu.raf.IAMService.bootstrap;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/**
 * Ova klasa ce da se runnuje prilikom pokretanja maven test komande
 * ----------------------------------------------------------------
 * PODACI ISPOD SU TESTNI PODACI
 * PODACI ISPOD SU TESTNI PODACI
 * PODACI ISPOD SU TESTNI PODACI
 * PODACI ISPOD SU TESTNI PODACI
 * ----------------------------------------------------------------
 * NIJE DOZVOLJENO MENJANJE POSTOJEĆIH PODATAKA !!!!!!!!!!!!!!!
 * NIJE DOZVOLJENO MENJANJE POSTOJEĆIH PODATAKA !!!!!!!!!!!!!!!
 * NIJE DOZVOLJENO MENJANJE POSTOJEĆIH PODATAKA !!!!!!!!!!!!!!!
 * NIJE DOZVOLJENO MENJANJE POSTOJEĆIH PODATAKA !!!!!!!!!!!!!!!
 * ----------------------------------------------------------------
 * DOZVOLJENO JE DODAVANJE NOVIH PODATAKA !!!!!!!!!!!!!!!
 * DOZVOLJENO JE DODAVANJE NOVIH PODATAKA !!!!!!!!!!!!!!!
 * DOZVOLJENO JE DODAVANJE NOVIH PODATAKA !!!!!!!!!!!!!!!
 * DOZVOLJENO JE DODAVANJE NOVIH PODATAKA !!!!!!!!!!!!!!!
 */

@Component
@RequiredArgsConstructor
@Profile("test")
public class BootstrapTestData implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(BootstrapTestData.class);
    private static final Object lock = new Object();
    private static Boolean alreadySetup = false;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyRepository companyRepository;

    @Override
    public void run(String... args) throws Exception {

        synchronized (lock) {
            if (alreadySetup) {
                return;
            }
            alreadySetup = true;
        }

        logger.info("IAMService: TEST DATA LOADING IN PROGRESS...");

        Role employeeRole = roleRepository.findByRoleType(RoleType.EMPLOYEE).get();
        Role agentRole = roleRepository.findByRoleType(RoleType.AGENT).get();
        Role userRole = roleRepository.findByRoleType(RoleType.USER).get();
        Role admin = roleRepository.findByRoleType(RoleType.ADMIN).get();

        Permission per1 = permissionRepository.findByPermissionType(PermissionType.PERMISSION_1).get();
        Permission per2 = permissionRepository.findByPermissionType(PermissionType.PERMISSION_2).get();

        Employee inactiveEmployee = new Employee();
        inactiveEmployee.setDateOfBirth(511739146L);
        inactiveEmployee.setEmail("inactiveEmplyee@gmail.com");
        inactiveEmployee.setUsername("inactiveEmplyee@gmail.com");
        inactiveEmployee.setPhone("+38111236456");
        inactiveEmployee.setAddress("Pariske komune 5, Beograd, Srbija");
        inactiveEmployee.setName("name");
        inactiveEmployee.setSurname("surname");
        inactiveEmployee.setGender("Male");
        inactiveEmployee.setPosition("position");
        inactiveEmployee.setDepartment("department");
        inactiveEmployee.setActive(false);
        inactiveEmployee.setPermissions(List.of(per1, per2));
        inactiveEmployee.setRole(employeeRole);

        Employee activeEmployee = new Employee();
        activeEmployee.setDateOfBirth(511739146L);
        activeEmployee.setEmail("activeEmplyee@gmail.com");
        activeEmployee.setUsername("activeEmplyee@gmail.com");
        activeEmployee.setPhone("+38111236456");
        activeEmployee.setAddress("Pariske komune 5, Beograd, Srbija");
        activeEmployee.setName("name");
        activeEmployee.setSurname("surname");
        activeEmployee.setGender("Male");
        activeEmployee.setPosition("position");
        activeEmployee.setDepartment("department");
        activeEmployee.setActive(true);
        activeEmployee.setPermissions(List.of(per1, per2));
        activeEmployee.setRole(employeeRole);

        CorporateClient notEmployee = new CorporateClient();
        notEmployee.setDateOfBirth(511739146L);
        notEmployee.setEmail("notEmployee@gmail.com");
        notEmployee.setUsername("notEmployee@gmail.com");
        notEmployee.setPhone("+38111236456");
        notEmployee.setAddress("Pariske komune 5, Beograd, Srbija");
        notEmployee.setName("name");
        notEmployee.setPrimaryAccountNumber("12345678901234567890");
        notEmployee.setPermissions(List.of(per1, per2));
        notEmployee.setRole(userRole);

        Agent agent = new Agent();
        agent.setDateOfBirth(511739146L);
        agent.setEmail("agentLimitReset@gmail.com");
        agent.setUsername("agentLimitReset@gmail.com");
        agent.setPhone("+38111236456");
        agent.setAddress("Pariske komune 5, Beograd, Srbija");
        agent.setLimit(new BigDecimal(1000));
        agent.setLeftOfLimit(new BigDecimal(1000));
        agent.setPermissions(List.of(per1, per2));
        agent.setRole(agentRole);

        User loginTestUser = new User();
        loginTestUser.setEmail("loginTestUser@gmail.com");
        loginTestUser.setUsername("loginTestUser@gmail.com");
        loginTestUser.setPassword(passwordEncoder.encode("loginTestUser"));
        loginTestUser.setRole(userRole);
        loginTestUser.setPermissions(List.of(per1, per2));
        loginTestUser.setPhone("+38111236456");
        loginTestUser.setDateOfBirth(336779146L);
        loginTestUser.setAddress("Trg Republike V/5, Beograd, Srbija");

        User passwordChangeTestUser = new User();
        passwordChangeTestUser.setEmail("passwordChangeTestUser@gmail.com");
        passwordChangeTestUser.setUsername("passwordChangeTestUser@gmail.com");
        passwordChangeTestUser.setPassword(passwordEncoder.encode("passwordChangeTestUser"));
        passwordChangeTestUser.setRole(userRole);
        passwordChangeTestUser.setPermissions(List.of(per1, per2));
        passwordChangeTestUser.setPhone("+38111236456");
        passwordChangeTestUser.setDateOfBirth(336779146L);
        passwordChangeTestUser.setAddress("Trg Republike V/5, Beograd, Srbija");

        User dummyUser = new User();
        dummyUser.setEmail("dummyUser@gmail.com");
        dummyUser.setUsername("dummyUser@gmail.com");
        dummyUser.setPassword(passwordEncoder.encode("dummyUser"));
        dummyUser.setRole(userRole);
        dummyUser.setPermissions(List.of(per1, per2));
        dummyUser.setPhone("+38111236456");
        dummyUser.setDateOfBirth(336779146L);
        dummyUser.setAddress("Trg Republike V/5, Beograd, Srbija");

        Employee dummyEmployee = new Employee();
        dummyEmployee.setEmail("dummyEmployee@gmail.com");
        dummyEmployee.setUsername("dummyEmployee@gmail.com");
        dummyEmployee.setPassword(passwordEncoder.encode("dummyEmployee"));
        dummyEmployee.setPhone("+38111236456");
        dummyEmployee.setDateOfBirth(336779146L);
        dummyEmployee.setAddress("Trg Republike V/5, Beograd, Srbija");
        dummyEmployee.setDateOfBirth(511739146L);
        dummyEmployee.setName("name");
        dummyEmployee.setSurname("surname");
        dummyEmployee.setGender("Male");
        dummyEmployee.setPosition("position");
        dummyEmployee.setDepartment("department");
        dummyEmployee.setActive(true);
        dummyEmployee.setPermissions(List.of(per1, per2));
        dummyEmployee.setRole(employeeRole);

        User dummyAdminUser = new User();
        dummyAdminUser.setEmail("dummyAdminUser@gmail.com");
        dummyAdminUser.setUsername("dummyAdminUser@gmail.com");
        dummyAdminUser.setPassword(passwordEncoder.encode("dummyAdminUser"));
        dummyAdminUser.setRole(admin);
        dummyAdminUser.setPermissions(List.of(per1, per2));
        dummyAdminUser.setPhone("+38111236456");
        dummyAdminUser.setDateOfBirth(336779146L);
        dummyAdminUser.setAddress("Trg Republike V/5, Beograd, Srbija");

        Agent dummyAgent = new Agent();
        dummyAgent.setEmail("dummyAgent@gmail.com");
        dummyAgent.setUsername("dummyAgent@gmail.com");
        dummyAgent.setPassword(passwordEncoder.encode("dummyAgent"));
        dummyAgent.setRole(agentRole);
        dummyAgent.setPhone("+38111317456");
        dummyAgent.setAddress("Juriga Gargarina 3, Beograd, Srbija");
        dummyAgent.setDateOfBirth(204155146L);
        dummyAgent.setLimit(new BigDecimal("12345.67"));
        dummyAgent.setLeftOfLimit(new BigDecimal("578.42"));

        saveUserIfNotExists(inactiveEmployee);
        saveUserIfNotExists(activeEmployee);
        saveUserIfNotExists(notEmployee);
        saveUserIfNotExists(agent);
        saveUserIfNotExists(loginTestUser);
        saveUserIfNotExists(passwordChangeTestUser);
        saveUserIfNotExists(dummyUser);
        saveUserIfNotExists(dummyEmployee);
        saveUserIfNotExists(dummyAdminUser);
        saveUserIfNotExists(dummyAgent);

        logger.info("IAMService: TEST DATA LOADING FINISHED...");

    }

    private void saveUserIfNotExists(User user) {
        if (userRepository.findByEmail(user.getEmail()).isEmpty()) {
            userRepository.save(user);
        }
    }
}
