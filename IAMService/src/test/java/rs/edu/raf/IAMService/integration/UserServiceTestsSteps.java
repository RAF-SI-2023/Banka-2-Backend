package rs.edu.raf.IAMService.integration;

import io.cucumber.java.an.E;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import rs.edu.raf.IAMService.IAMTestConfig;
import rs.edu.raf.IAMService.data.dto.UserDto;
import rs.edu.raf.IAMService.data.entites.Agent;
import rs.edu.raf.IAMService.data.entites.Employee;
import rs.edu.raf.IAMService.data.entites.Role;
import rs.edu.raf.IAMService.data.entites.User;
import rs.edu.raf.IAMService.data.enums.RoleType;
import rs.edu.raf.IAMService.repositories.UserRepository;
import rs.edu.raf.IAMService.services.UserService;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class UserServiceTestsSteps extends IAMTestConfig {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private long userID;
    private List<UserDto> users;
    @Given("User with email {string}")
    public void userWithEmail(String email) {
        try{
            User user = new User();
            user.setEmail(email);
            user.setUsername(email);
            user.setPassword("user");
            user.setRole(new Role(RoleType.USER));
            //user.setPermissions(List.of(per1, per2));
            userRepository.save(user);
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @When("deleting user with email {string}")
    public void deletingUserWithEmail(String email) {
        try{
            var res = userService.findUserByEmail(email);
            if (res.isPresent()){
                userService.deleteUserByEmail(email);
            }
            else fail("User with email " + email + " not found");
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Then("user with email {string} is deleted from database")
    public void userWithEmailIsDeletedFromDatabase(String email) {
        try{
            var res = userService.findUserByEmail(email);
            if (res.isPresent()){
                fail("User with email " + email + " is not deleted");
            }
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Given("bad email")
    public void badEmail() {
        // do nothing
    }

    @Then("deleting user with that email will fail")
    public void deletingUserWithThatEmailWillFail() {
        try{
            assertThrows(Exception.class, () -> {
                userService.findUserByEmail("bad email @ bad test");
            });
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }



    @Given("agent exists")
    public void agentExists() {
        Agent agent1 = new Agent();
        agent1.setEmail("testAgent@gmail.com");
        agent1.setUsername("testAgent@gmail.com");
        agent1.setPassword("Password");
        agent1.setRole(new Role(RoleType.AGENT));
        agent1.setLimit(new BigDecimal("100.00"));
        agent1.setLeftOfLimit(new BigDecimal("10.00"));

        try{
            userID = userRepository.save(agent1).getId();
        }
        catch (Exception e){
            fail(e.getMessage());
        }

    }

    @When("agent's left limit is reset")
    public void agentSLeftLimitIsReset() {
        try {
            userService.resetAgentsLeftLimit(userID);
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Then("agent's left limit is equal to their max limit")
    public void agentSLeftLimitIsEqualToTheirMaxLimit() {
        try {
            assertEquals(((Agent)userRepository.findById(userID).get()).getLimit(), userService.getAgentsLeftLimit(userID));
            userRepository.deleteById(userID); // no longer needed
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Given("user exists")
    public void userExists() {
        try{
            User user = new User();
            user.setEmail("testEmail@gmail.com");
            user.setUsername("testUser");
            user.setPassword("user");
            user.setRole(new Role(RoleType.USER));
            //user.setPermissions(List.of(per1, per2));
            userID = userRepository.save(user).getId();
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Then("reseting user's limit will fail")
    public void resetingUserSLimitWillFail() {
        try {
            assertThrows(Exception.class, ()->{
                userService.resetAgentsLeftLimit(userID);
            });
            userRepository.deleteById(userID);
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Given("unactivated employee exists")
    public void unactivatedEmployeeExists() {
        try{
            Employee employee1 = new Employee();
            employee1.setEmail("testEmployeeMail@gmail.com");
            employee1.setActive(false);
            employee1.setUsername("TestEmployee");
            employee1.setPassword("password");
            employee1.setRole(new Role(RoleType.EMPLOYEE));
            // employee1.setPermissions(List.of(per1, per2));
            userID = userRepository.save(employee1).getId();
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @When("unactivated employee is activated")
    public void unactivatedEmployeeIsActivated() {
        try {
            userService.employeeActivation((int)userID); // CHECK: should it be int?
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }


    @Then("employee isActive status is true")
    public void employeeIsActiveStatusIs() {
        try {
            assertTrue(((Employee) userRepository.findById(userID).get()).isActive());
            userRepository.deleteById(userID); // not needed anymore
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Given("an activated employee exists")
    public void anActivatedEmployeeExists() {
        try{
            Employee employee1 = new Employee();
            employee1.setEmail("testEmployeeMail@gmail.com");
            employee1.setActive(true);
            employee1.setUsername("TestEmployee");
            employee1.setPassword("password");
            employee1.setRole(new Role(RoleType.EMPLOYEE));
            // employee1.setPermissions(List.of(per1, per2));
            userID = userRepository.save(employee1).getId();
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @When("activated employee is deactivated")
    public void activatedEmployeeIsDeactivated() {
        try {
            userService.employeeDeactivation((int)userID); // CHECK: should it be int?
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Then("employee isActive status is false")
    public void employeeIsActiveStatusIsFalse() {
        try {
            assertTrue(! ((Employee) userRepository.findById(userID).get()).isActive());
            userRepository.deleteById(userID); // not needed anymore
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Then("activating user will fail")
    public void activatingUserWillFail() {
        // user is not an employee
        try{
            assertThrows(Exception.class, ()->{
                userService.employeeActivation((int)userID);
            });
            assertThrows(Exception.class, ()->{
                userService.employeeDeactivation((int)userID);
            });
            userRepository.deleteById(userID);
        }
        catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Given("users exist")
    public void usersExist() {
        // do nothing, BootstrapData makes users
    }

    @When("calling find all users")
    public void callingFindAllUsers() {
        try{
            users = userService.findAll();
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }


    @Then("get list of all users")
    public void getListOfAllUsers() {
        try{
            for (UserDto userDto : users) {
                if (userRepository.findById(userDto.getId()).isEmpty())
                    fail("User returned from findAll() and that user is not in repository");
            }
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

    @Given("new user {string} exists")
    public void newUserExists(String name) {
        try{
            User user = new User();
            user.setEmail("testEmail@gmail.com");
            user.setUsername(name);
            user.setPassword("user");
            user.setRole(new Role(RoleType.USER));
            //user.setPermissions(List.of(per1, per2));
            userID = userRepository.save(user).getId();
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }


    @Then("find new user {string}")
    public void findNewUser(String name) {
        try{
            boolean usernameFound = false;
            for (UserDto userDto : users) {
                if (userRepository.findById(userDto.getId()).get().getUsername().equals(name))
                    usernameFound = true;
            }
            assertTrue(usernameFound);
            userRepository.deleteById(userID); // removing as not needed
        }
        catch (Exception e)
        {
            fail(e.getMessage());
        }
    }

    @When("updating user's name to {string}")
    public void updatingUserSNameTo(String newName) {
        try{
            UserDto userDto = userService.findById(userID);
            userDto.setUsername(newName);
            userService.updateUser(userDto);
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Then("user's name in database is {string}")
    public void userSNameInDatabaseIs(String name) {
        try{
            assertEquals(userRepository.findById(userID).get().getUsername(), name);
            userRepository.deleteById(userID); // remove as not needed
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Given("user does not exist")
    public void userDoesNotExist() {
        // dont do anything
    }

    @Then("updating user's name will fail")
    public void updatingUserSNameWillFail() {
        try{
            UserDto userDto = userService.findById(userID);
            userDto.setUsername("New Name");
            userService.updateUser(userDto);
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }
}
