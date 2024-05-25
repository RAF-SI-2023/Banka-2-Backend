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
import java.util.NoSuchElementException;

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

    @Value("${MY_EMAIL_1:lukapavlovic032@gmail.com}")
    private String myEmail1;

    @Value("${MY_EMAIL_2:lpavlovic11521rn@raf.rs}")
    private String myEmail2;

    @Value("${MY_EMAIL_3:lukapa369@gmail.com}")
    private String myEmail3;

    @Value("${MY_EMAIL_4:defaultemail4@gmail.com}")
    private String myEmail4;

    @Value("${MY_EMAIL_5:defaultemail5@gmail.com}")
    private String myEmail5;




    public void run(String... args)  {
        try{
            logger.info("IAMService: DEV DATA LOADING IN PROGRESS...");

            loadCompanies();
            loadUsers();

            logger.info("IAMService: DEV DATA LOADING FINISHED...");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadUsers() {

        logger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        logger.info(String.valueOf(roleRepository.findAll().size()));
        logger.info(roleRepository.findAll().toString());
        logger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");

        Role adminRole;
        Role employeeRole;
        Role supervisorRole;
        Role agentRole;
        Role userRole;

        try {
            adminRole = roleRepository.findByRoleType(RoleType.ADMIN).get();
        } catch (Exception e) {
            logger.info("NEMA AdMIN ROLE");
            throw new NoSuchElementException();
        }


        try {
            employeeRole = roleRepository.findByRoleType(RoleType.EMPLOYEE).get();
        } catch (Exception e) {
            logger.info("NEMA EMPLOYEE ROLE");
            throw new NoSuchElementException();

        }


        try {
            supervisorRole = roleRepository.findByRoleType(RoleType.SUPERVISOR).get();
        } catch (Exception e) {
            logger.info("NEMA SUPERVISOR ROLE");
            throw new NoSuchElementException();

        }


        try {
            agentRole = roleRepository.findByRoleType(RoleType.AGENT).get();
        } catch (Exception e) {
            logger.info("NEMA AGENT ROLE");
            throw new NoSuchElementException();

        }


        try {
            userRole = roleRepository.findByRoleType(RoleType.USER).get();
        } catch (Exception e) {
            logger.info("NEMA USER ROLE");
            throw new NoSuchElementException();

        }



        Permission per1;
        Permission per2;

        try {
            per1 = permissionRepository.findByPermissionType(PermissionType.PERMISSION_1).get();
        } catch (Exception e) {
            logger.info("NEMA PERISIJE 1");
            throw new NoSuchElementException();

        }

        try {
            per2 = permissionRepository.findByPermissionType(PermissionType.PERMISSION_2).get();
        } catch (Exception e) {
            logger.info("NEMA PERISIJE 2");
            throw new NoSuchElementException();

        }


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
        agent1.setEmail("agent1@gmail.com");
        agent1.setUsername("agent1@gmail.com");
        agent1.setPassword(passwordEncoder.encode("agent1"));
        agent1.setRole(agentRole);
        agent1.setPhone("+38111317456");
        agent1.setAddress("Jurija Gargarina 3, Beograd, Srbija");
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


        Long[] pib = {123456789L, 987654321L, 456789123L, 789123456L, 654321987L};
        String[] emails = {"myEmail1@gmail.com", "myEmail2@gmail.com", "myEmail3@gmail.com", myEmail4, myEmail5};
        String[] usernames = {"companyEmployee1", "companyEmployee2", "companyEmployee3", "companyEmployee4", "companyEmployee5"};
        String[] phoneNumbers = {"+38111236456", "+38111236457", "+38111236458", "+38111236459", "+38111236460"};
        String[] addresses = {"Pariske komune 5, Beograd, Srbija", "Bulevar Kralja Aleksandra 5, Beograd, Srbija", "Nemanjina 5, Beograd, Srbija", "Jurija Gargarina 3, Beograd, Srbija", "Dr Huga Klana 1, Beograd, Srbija"};
        Long[] dateOfBirth = {511739146L, 606433546L, 473765600L, 204155146L, 216596746L};

        for ( int i = 0; i < 5; i++ ) {
            CompanyEmployee     companyEmployee = new CompanyEmployee();
            companyEmployee.setEmail(emails[i]);
            companyEmployee.setUsername(usernames[i]);
            companyEmployee.setPassword(passwordEncoder.encode("companyemployee"));
            companyEmployee.setPhone(phoneNumbers[i]);
            companyEmployee.setAddress(addresses[i]);
            companyEmployee.setRole(userRole);
            companyEmployee.setDateOfBirth(dateOfBirth[i]);
            companyEmployee.setPib(pib[i]);
            companyEmployee.setPermissions(List.of(per1, per2));

            saveUserIfNotExists(companyEmployee);
        }

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


    private void loadCompanies() {

        if (companyRepository.count() == 0) {
            String[] companyNames = {"Vought International", "NCR Corp.", "Umbrella Corp.", "Doofenshmirtz Inc.", "E-Corp"};
            String[] faxNumber = {"123456", "98765", "42146", "78909", "34567"};
            String[] phoneNumber = {"+38111236456", "+38111236457", "+38111236458", "+38111236459", "+38111236460"};
            String[] address = {"Trg Republike V/5, Beograd, Srbija", "Bulevar Kralja Aleksandra 5, Beograd, Srbija", "Nemanjina 5, Beograd, Srbija", "Jurija Gargarina 3, Beograd, Srbija", "Dr Huga Klana 1, Beograd, Srbija"};
            Long[] pib = {123456789L, 987654321L, 456789123L, 789123456L, 654321987L};
            Integer[] registryNumber = {123456789, 987654321, 456789123, 789123456, 654321987};
            Integer[] identificationNumber = {123456, 987654, 456789, 789123, 654321};
            Integer[] activityCode = {12345, 54321, 67890, 98765, 45678};
            for ( int i = 0; i < 5; i++ ) {
                Company company = new Company();
                company.setCompanyName(companyNames[i]);
                company.setFaxNumber(faxNumber[i]);
                company.setPhoneNumber(phoneNumber[i]);
                company.setAddress(address[i]);
                company.setPib(pib[i]);
                company.setRegistryNumber(registryNumber[i]);
                company.setIdentificationNumber(identificationNumber[i]);
                company.setActivityCode(activityCode[i]);
                companyRepository.save(company);
            }
        }

    }

    private void saveUserIfNotExists(User user) {
        if (userRepository.findByEmail(user.getEmail()).isEmpty()) {
            userRepository.save(user);
        }
    }

}