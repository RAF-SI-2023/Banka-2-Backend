package rs.edu.raf.IAMService.integration.users;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import rs.edu.raf.IAMService.data.dto.UserDto;
import rs.edu.raf.IAMService.data.entites.Agent;
import rs.edu.raf.IAMService.data.entites.Employee;
import rs.edu.raf.IAMService.data.entites.User;
import rs.edu.raf.IAMService.data.enums.RoleType;
import rs.edu.raf.IAMService.repositories.RoleRepository;
import rs.edu.raf.IAMService.repositories.UserRepository;
import rs.edu.raf.IAMService.services.UserService;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTestsSteps extends UserServiceIntegrationTestsConfig {

   @Autowired
   private UserService userService;

   @Autowired
   private UserRepository userRepository;

   @Autowired
   private RoleRepository roleRepository;

   private long userID;
   private List<UserDto> users;

   BigDecimal originalLimit;

   @Given("Agent {string} exists")
   public void agentExists(String email) {
       try{
           var user = userService.findUserByEmail(email).get();
           userID = user.getId();
           assertEquals(user.getRole().getRoleType(), RoleType.AGENT);

       }
       catch (Exception e){
           fail("user with email " + email + " not found");
       }
   }

   @When("Agent's {string} left limit is reset")
   public void agentSLeftLimitIsReset(String email) {
       try{
           // checked if user exists step before
           originalLimit = ((Agent ) userRepository.findByEmail(email).get()).getLeftOfLimit();
           userService.resetAgentsLeftLimit(userID);
       }
       catch (Exception e){
           fail(e.getMessage());
       }
   }

   @Then("agent's {string} left limit is equal to their max limit")
   public void agentSLeftLimitIsEqualToTheirMaxLimit(String email) {
       try {
           assertEquals(((Agent) userRepository.findByEmail(email).get()).getLimit(), userService.getAgentsLeftLimit(userID));
       }
       catch (Exception e){
           fail(e.getMessage());
       }
   }

   @Given("user exists")
   public void userExists() {
       try{
           userID = userService.findUserByEmail("nikola@gmail.com").get().getId();
       }
       catch (Exception e){
           fail(e.getMessage());
       }
   }


   @Then("resetting user's limit will fail")
   public void resettingUserSLimitWillFail() {
       try{
           assertThrows(Exception.class, ()->{
               userService.resetAgentsLeftLimit(userID);
           });
       }
       catch (Exception e){
           fail(e.getMessage());
       }
   }

   @Given("unactivated employee exists")
   public void unactivatedEmployeeExists() {
       try{
           var user = userService.findUserByEmail("mirkomail@gmail.com").get();
           userID = user.getId();
           assertEquals(user.getRole().getRoleType(), RoleType.EMPLOYEE);
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
       }
       catch (Exception e){
           fail(e.getMessage());
       }
   }

   @Given("an activated employee exists")
   public void anActivatedEmployeeExists() {
       try{
           var user = userService.findUserByEmail("lazar@gmail.com").get();
           userID = user.getId();
           assertEquals(user.getRole().getRoleType(), RoleType.EMPLOYEE);
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
           assertFalse(((Employee) userRepository.findById(userID).get()).isActive());
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
   public void updatingUserSPhoneTo(String newPhone) {
       try{
           var dto = userService.findById(userID);
           dto.setPhone(newPhone);
           userService.updateUser(dto);
       }
       catch (Exception e){
           fail(e.getMessage());
       }
   }

   @Then("user's phone in database is {string}")
   public void userSPhoneInDatabaseIs(String newPhone) {
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
           var role = roleRepository.findByRoleType(RoleType.USER);

           User user = new User();
           user.setEmail(email);
           user.setUsername(email);
           user.setDateOfBirth(511739146L);
           user.setPhone("+38111236456");
           user.setAddress("Pariske komune 5, Beograd, Srbija");
           user.setRole(role.get());
           userRepository.save(user);
       }
       catch (Exception e){
           fail(e.getMessage());
       }
   }

   @Given("User with email {string} exists")
   public void userWithEmailExists(String email) {
       try{
           var res = userRepository.findByEmail(email);
           assertTrue(res.isPresent());
           userID = res.get().getId();
       }
       catch (Exception e){
           fail(e.getMessage());
       }
   }
}
