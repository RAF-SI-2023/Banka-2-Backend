package rs.edu.raf.IAMService.integration.users;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import rs.edu.raf.IAMService.data.dto.UserDto;
import rs.edu.raf.IAMService.data.entites.Agent;
import rs.edu.raf.IAMService.data.entites.Employee;
import rs.edu.raf.IAMService.data.entites.Role;
import rs.edu.raf.IAMService.data.entites.User;
import rs.edu.raf.IAMService.data.enums.RoleType;
import rs.edu.raf.IAMService.repositories.RoleRepository;
import rs.edu.raf.IAMService.repositories.UserRepository;
import rs.edu.raf.IAMService.services.UserService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTestsSteps extends UserServiceIntegrationTestsConfig {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private long fromInactiveToActivatedEmployeeID;

    private long fromActivatedToInactiveEmployeeID;

    private long notEmployeeID;

    private long agentID;

    private long notAgentID;

    private long userID;

    private List<UserDto> users;

    private BigDecimal leftOfLimit;


    @Given("Agent {string} exists")
    public void agentExists(String email) {
       try{
           User user = userService.findUserByEmail(email).get();
           agentID = user.getId();
           assertEquals(user.getRole().getRoleType(), RoleType.AGENT);
       }
       catch (Exception e){
           fail("user with email " + email + " not found");
       }
    }

    @When("Agent's {string} left limit is reset")
    public void agentLeftLimitReset(String email) {
       try{
           // checked if user exists step before
//           leftOfLimit = ((Agent ) userRepository.findByEmail(email).get()).getLeftOfLimit();
           userService.resetAgentsLeftLimit(agentID);
       }
       catch (Exception e){
           fail(e.getMessage());
       }
    }

    @Then("agent's {string} left limit is equal to their max limit")
    public void agentSLeftLimitIsEqualToTheirMaxLimit(String email) {
       try {
           assertEquals(
                   ((Agent) userRepository.findByEmail(email).get()).getLimit(),
                   userService.getAgentsLeftLimit(agentID)
           );
       }
       catch (Exception e){
           fail(e.getMessage());
       }
    }

    @Given("user that is not employee")
    public void getUserThatIsNotEmployee() {
       try{
           notEmployeeID = userService.findUserByEmail("notEmployee@gmail.com").get().getId();
       }
       catch (Exception e){
           fail(e.getMessage());
       }
    }

    @Given("user that is not agent")
    public void getUserThatIsNotAgent() {
        try{
            notAgentID = userService.findUserByEmail("activeEmplyee@gmail.com").get().getId();
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }


    @Then("resetting user's limit will fail")
    public void resettingUserSLimitWillFail() {
       try{
           assertThrows(Exception.class, ()->
               userService.resetAgentsLeftLimit(notAgentID)
           );
       }
       catch (Exception e){
           fail(e.getMessage());
       }
    }

    @Given("unactivated employee exists")
    public void unactivatedEmployeeExists() {
        // pravimo neaktivnog zaposlenog

       try{
           User user = userService.findUserByEmail("inactiveEmplyee@gmail.com").get();
           fromInactiveToActivatedEmployeeID = user.getId();
           assertEquals(user.getRole().getRoleType(), RoleType.EMPLOYEE);
       }
       catch (Exception e){
           fail(e.getMessage());
       }
    }

    @When("unactivated employee is activated")
    public void unactivatedEmployeeIsActivated() {
       try {
           User user = userService.employeeActivation((int) fromInactiveToActivatedEmployeeID);
           fromActivatedToInactiveEmployeeID = user.getId();
       }
       catch (Exception e){
           fail(e.getMessage());
       }
    }


    @Then("employee isActive status is true")
    public void employeeIsActiveStatusIs() {
       try {
           assertTrue(((Employee) userRepository.findById(fromInactiveToActivatedEmployeeID).get()).isActive());
       }
       catch (Exception e){
           fail(e.getMessage());
       }
    }

    @Given("an activated employee exists")
    public void anActivatedEmployeeExists() {
       try{
           User user = userService.findUserByEmail("activeEmplyee@gmail.com").get();
           fromActivatedToInactiveEmployeeID = user.getId();
           assertEquals(user.getRole().getRoleType(), RoleType.EMPLOYEE);
       }
       catch (Exception e){
           fail(e.getMessage());
       }
    }

    @When("activated employee is deactivated")
    public void activatedEmployeeIsDeactivated() {
       try {
           userService.employeeDeactivation((int)fromActivatedToInactiveEmployeeID); // CHECK: should it be int?
       }
       catch (Exception e){
           fail(e.getMessage());
       }
    }

    @Then("employee isActive status is false")
    public void employeeIsActiveStatusIsFalse() {
       try {
           assertFalse(((Employee) userRepository.findById(fromActivatedToInactiveEmployeeID).get()).isActive());
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
               userService.employeeActivation((int)notEmployeeID);
           });
           assertThrows(Exception.class, ()->{
               userService.employeeDeactivation((int)notEmployeeID);
           });
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

    @Then("find user {string}")
    public void findUser(String email) {
       try{
           boolean userFound = false;
           for (UserDto userDto : users) {
               if (userRepository.findById(userDto.getId()).get().getEmail().equals(email))
                   userFound = true;
           }
           assertTrue(userFound);
       }
       catch (Exception e)
       {
           fail(e.getMessage());
       }
    }

    @When("updating user's Phone to {string}")
    public void updatingUserPhoneTo(String newPhone) {
       try{
           UserDto dto = userService.findById(userID);
           dto.setPhone(newPhone);
           userService.updateUser(dto);
       }
       catch (Exception e){
           fail(e.getMessage());
       }
    }

    @Then("user's phone in database is {string}")
    public void userPhoneInDatabaseIs(String newPhone) {
       try{
           assertEquals(userService.findById(userID).getPhone(), newPhone);
       }
       catch (Exception e){
           fail(e.getMessage());
       }
    }

    @Given("New User {string} is added")
    public void newUserIsAdded(String email) {
       try{
           Role role = roleRepository.findByRoleType(RoleType.USER).get();

           User user = new User();
           user.setEmail(email);
           user.setUsername(email);
           user.setDateOfBirth(511739146L);
           user.setPhone("+38111236456");
           user.setAddress("Pariske komune 5, Beograd, Srbija");
           user.setRole(role);
           userRepository.save(user);
       }
       catch (Exception e){
           fail(e.getMessage());
       }
    }

    @Given("User with email {string} exists")
    public void userWithEmailExists(String email) {
       try{
           Optional<User> optionalUser = userRepository.findByEmail(email);
           assertTrue(optionalUser.isPresent());
           userID = optionalUser.get().getId();
       }
       catch (Exception e){
           fail(e.getMessage());
       }
    }
}
