package rs.edu.raf.IAMService.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class BootstrapData implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(BootstrapData.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionsRepository;

    @Autowired
    public BootstrapData(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, PermissionRepository permissionsRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.permissionsRepository = permissionsRepository;
    }


    @Override
    public void run(String... args) throws Exception {

        logger.info("DATA LOADING IN PROGRESS...");


        List<User> allUser = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");

        Role admin, employee, supervisor, agent, user;
        Permission premission1, premission2, premission3, premission4;

        //role
        admin = new Role(RoleType.ADMIN);
        employee = new Role(RoleType.EMPLOYEE);
        supervisor = new Role(RoleType.SUPERVISOR);
        agent = new Role(RoleType.AGENT);
        user = new Role(RoleType.USER);

        //add role
        roleRepository.save(admin);
        roleRepository.save(employee);
        roleRepository.save(supervisor);
        roleRepository.save(agent);
        roleRepository.save(user);

        //permission
        premission1 = new Permission(PermissionType.PERMISSION_1);
        premission2 = new Permission(PermissionType.PERMISSION_2);
        premission3 = new Permission(PermissionType.PERMISSION_3);
        premission4 = new Permission(PermissionType.PERMISSION_4);

        //add permission
        permissionsRepository.save(premission1);
        permissionsRepository.save(premission2);
        permissionsRepository.save(premission3);
        permissionsRepository.save(premission4);

        //admin creation
        Employee adminUser = new Employee();

        //   LocalDateTime.of(1990, 9, 22, 0, 0);
        Date date = Date.from(LocalDateTime.of(1990, 9, 22, 0, 0).atZone(java.time.ZoneId.systemDefault()).toInstant());

        adminUser.setName("Michale");
        adminUser.setSurname("Robbin");
        adminUser.setGender("Male");
        adminUser.setPosition("Main administrator");
        adminUser.setDepartment("Administration");
        adminUser.setActive(false);
        adminUser.setDateOfBirth(date);
        adminUser.setEmail("admin2290@gmail.com");
        adminUser.setUsername("admin2290@gmail.com");
        adminUser.setPhone("+3812290011");
        adminUser.setAddress("Cecilia Chapman, " + "711-2880 Nulla Street " + "Mankato Mississippi 96522");
        adminUser.setPassword(this.passwordEncoder.encode("admin2290"));
        adminUser.setRole(admin);
        adminUser.setPermissions(List.of(premission1, premission2, premission3, premission4));

        allUser.add(adminUser);

        //employees creation
        Employee employee1 = new Employee();
        Employee employee2 = new Employee();

        Date date1 = Date.from(LocalDateTime.of(1993, 9, 10, 0, 0).atZone(java.time.ZoneId.systemDefault()).toInstant());


        //employee1
        employee1.setName("Mario");
        employee1.setSurname("Saule");
        employee1.setGender("Male");
        employee1.setPosition("Financial menager");
        employee1.setDepartment("Finance");
        employee1.setActive(false);
        employee1.setDateOfBirth(date1);
        employee1.setEmail("employee1009@gmail.com");
        employee1.setUsername("employee1009@gmail.com");
        employee1.setPhone("+3811009111");
        employee1.setAddress("Celeste Slater, " + "606-3727 Ullamcorper. Street " + "Roseville NH 11523");
        employee1.setPassword(this.passwordEncoder.encode("employee1009"));
        employee1.setRole(employee);
        employee1.setPermissions(List.of(premission3, premission4));

        allUser.add(employee1);

        Date date2 = Date.from(LocalDateTime.of(1995, 12, 4, 0, 0).atZone(java.time.ZoneId.systemDefault()).toInstant());
        //employee2
        employee2.setName("Zanifer");
        employee2.setSurname("Foster");
        employee2.setGender("Female");
        employee2.setPosition("Operational menager");
        employee2.setDepartment("Operational");
        employee2.setActive(false);
        employee2.setDateOfBirth(date2);
        employee2.setEmail("employee0412@gmail.com");
        employee2.setUsername("employee0412@gmail.com");
        employee2.setPhone("+3810412111");
        employee2.setAddress("Hiroko Potter, " + "P.O. Box 887 2508 Dolor. Avenu " + "Muskegon KY 12482");
        employee2.setPassword(this.passwordEncoder.encode("employee0412"));
        employee2.setRole(employee);
        employee2.setPermissions(List.of(premission3, premission4));

        allUser.add(employee2);

        userRepository.saveAll(allUser);

        logger.info("DATA LOADING FINISHED...");
    }
}