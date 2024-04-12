//package rs.edu.raf.IAMService.e2e.users;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import io.cucumber.java.en.And;
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.test.web.servlet.ResultActions;
//import rs.edu.raf.IAMService.data.dto.*;
//import rs.edu.raf.IAMService.data.entites.*;
//import rs.edu.raf.IAMService.data.enums.RoleType;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.junit.jupiter.api.Assertions.fail;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import org.springframework.http.MediaType;
//import rs.edu.raf.IAMService.mapper.UserMapper;
//import rs.edu.raf.IAMService.repositories.RoleRepository;
//import rs.edu.raf.IAMService.repositories.UserRepository;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class UserControllerTestsSteps extends UserControllerE2ETestsConfig {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private UserControllerTestsState testsState;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    private final UserMapper userMapper = new UserMapper();
//
//    @Autowired
//    private RoleRepository roleRepository;
//
//    UserDto userDto;
//
//    List<UserDto> users;
//    String oldPhoneNumber;
//    String oldName;
//    String oldSurname;
//
//    @Given("admin logs in")
//    public void adminLogsIn() {
//        // BootstrapData adds admin
//        try{
//            ResultActions resultActions = mockMvc.perform(
//                    post("/api/auth/login")
//                            .contentType(MediaType.APPLICATION_JSON_VALUE)
//                            .accept(MediaType.APPLICATION_JSON_VALUE)
//                            .content("{\"email\":\"lukapavlovic032@gmail.com\",\"password\":\"admin\"}")
//            ).andExpect(status().isOk());
//
//            MvcResult mvcResult = resultActions.andReturn();
//            String loginResponse = mvcResult.getResponse().getContentAsString();
//            TokenDto tokenDto = objectMapper.readValue(loginResponse, TokenDto.class);
//            testsState.setJwtToken(tokenDto.getToken());
//        }
//        catch (Exception e){
//            fail(e.getMessage());
//        }
//    }
//
//    @And("agent {string} exists")
//    public void agentExists(String agentEmail) {
//        // BootstrapData adds admin
//        try{
//            ResultActions resultActions = mockMvc.perform(
//                    get("/api/users/email/" + agentEmail)
//                            .accept(MediaType.APPLICATION_JSON_VALUE)
//                            .header("Authorization", "Bearer " + testsState.getJwtToken())
//            ).andExpect(status().isOk());
//
//            MvcResult mvcResult = resultActions.andReturn();
//            String jsonUserDto = mvcResult.getResponse().getContentAsString();
//            UserDto userDto = objectMapper.readValue(jsonUserDto, UserDto.class);
//            assertEquals(userDto.getRole(), RoleType.AGENT);
//            this.userDto = userDto;
//        }
//        catch (Exception e){
//            fail(e.getMessage());
//        }
//    }
//
//    @When("agent's {string} left limit is reset")
//    public void agentSLeftLimitIsReset(String agentEmail) {
//        try{
//            // Used findByEmail in step before
//            ResultActions resultActions = mockMvc.perform(
//                    patch("/api/users/agent-limit/reset/" + userDto.getId().toString())
//                            .header("Authorization", "Bearer " + testsState.getJwtToken())
//            ).andExpect(status().isOk());
//        }
//        catch (Exception e){
//            fail(e.getMessage());
//        }
//    }
//
//    @Then("agent's {string} left limit is same as their max limit")
//    public void agentSLeftLimitIsSameAsTheirMaxLimit(String agentEmail) {
//        try{
//            String response = mockMvc.perform(get("/api/users/agent-limit/" + userDto.getId().toString())
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .header("Authorization", "Bearer " + testsState.getJwtToken()))
//                    .andExpect(status().isOk())
//                    .andReturn()
//                    .getResponse()
//                    .getContentAsString();
//
//            var value = Double.parseDouble(response);
//
//            Agent agent = (Agent) userRepository.findById(userDto.getId()).get();
//
//            // Left Limit (value) should be equal to getLimit()
//            assertEquals(agent.getLimit().doubleValue(), value, 0.01);
//
//        }
//        catch (Exception e){
//            fail(e.getMessage());
//        }
//    }
//
//    @When("listing all users")
//    public void listingAllUsers() {
//        try{
//            // Used findByEmail in step before
//            ResultActions resultActions = mockMvc.perform(
//                    get("/api/users/all")
//                            .header("Authorization", "Bearer " + testsState.getJwtToken()))
//                .andExpect(status().isOk());
//
//            MvcResult mvcResult = resultActions.andReturn();
//            String str = mvcResult.getResponse().getContentAsString();
//            users = objectMapper.readValue(str, new TypeReference<ArrayList<UserDto>>() {});
//        }
//        catch (Exception e){
//            fail(e.getMessage());
//        }
//    }
//
//    @Then("confirm everyone is present")
//    public void confirmEveryoneIsPresent() {
//        try{
//            for (UserDto userDto : users) {
//                if (userRepository.findById(userDto.getId()).isEmpty())
//                    fail("User with id " + userDto.getId() + " does not exist");
//            }
//        }
//        catch (Exception e){
//            fail(e.getMessage());
//        }
//    }
//
//    @And("user {string} exists")
//    public void userExists(String email) {
//        try{
//            assertTrue(userRepository.findByEmail(email).isPresent());
//        }
//        catch (Exception e){
//            fail(e.getMessage());
//        }
//    }
//
//    @Then("confirm user {string} is present")
//    public void confirmUserIsPresent(String email) {
//        try{
//            var res = users.stream().filter(obj -> obj.getEmail().equals(email)).findFirst();
//            assertTrue(res.isPresent());
//        }
//        catch (Exception e){
//            fail(e.getMessage());
//        }
//    }
//
//    @And("employee {string} exists")
//    public void employeeExists(String email) {
//        try{
//            var employee = userRepository.findByEmail(email);
//            assertTrue(employee.isPresent());
//            assertEquals(employee.get().getRole().getRoleType(), RoleType.EMPLOYEE);
//
//        }catch (Exception e){
//            fail(e.getMessage());
//        }
//    }
//
//    @When("employee's {string} phone number is changed to {string}")
//    public void employeeSPhoneNumberIsChangedTo(String email, String newPhoneNumber) {
//        // employee is set in before calling this
//        // use employe Id
//        try{
//            EmployeeDto employeeDto = userMapper.employeeToEmployeeDto((Employee) userRepository.findByEmail(email).get());
//            oldPhoneNumber = employeeDto.getPhone();
//            employeeDto.setPhone(newPhoneNumber);
//            ResultActions resultActions = mockMvc.perform(
//                    put("/api/users/update/employee")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(employeeDto))
//                            .header("Authorization", "Bearer " + testsState.getJwtToken())
//            ).andExpect(status().isOk());
//        }
//        catch (Exception e){
//            fail(e.getMessage());
//        }
//    }
//
//    @Then("employee's {string} phone number in database is {string}")
//    public void employeeSPhoneNumberInDatabaseIs(String email, String newPhoneNumber) {
//        try{
//            assertEquals(userRepository.findByEmail(email).get().getPhone(), newPhoneNumber);
//        }
//        catch (Exception e){
//            fail(e.getMessage());
//        }
//        // Reverse change (maybe needed)
//        var old = userRepository.findByEmail(email).get();
//        old.setPhone(oldPhoneNumber);
//        userRepository.save(old);
//    }
//
//    @And("corporate client {string} exists")
//    public void corporateClientExists(String email) {
//        try{
//            var client = userRepository.findByEmail(email);
//            assertTrue(client.isPresent());
//
//        }catch (Exception e){
//            fail(e.getMessage());
//        }
//    }
//
//    @When("corporate client's {string} name is changed to {string}")
//    public void corporateClientSUsernameIsChangedTo(String email, String newName) {
//        try{
//            CorporateClientDto corporateClientDto = userMapper.corporateClientToCorporateClientDto((CorporateClient) userRepository.findByEmail(email).get());
//            oldName = corporateClientDto.getName();
//            corporateClientDto.setName(newName);
//            // Used findByEmail in step before
//            ResultActions resultActions = mockMvc.perform(
//                    put("/api/users/update/corporate-client")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(corporateClientDto))
//                            .header("Authorization", "Bearer " + testsState.getJwtToken())
//            ).andExpect(status().isOk());
//        }
//        catch (Exception e){
//            fail(e.getMessage());
//        }
//    }
//
//    @Then("corporate client's {string} name in database is {string}")
//    public void corporateClientSUsernameInDatabaseIs(String email, String newName) {
//        try{
//            assertEquals(((CorporateClient)userRepository.findByEmail(email).get()).getName(), newName);
//        }
//        catch (Exception e){
//            fail(e.getMessage());
//        }
//        // Reverse change (maybe needed)
//        var old = userRepository.findByEmail(email).get();
//        ((CorporateClient) old).setName(oldName);
//        userRepository.save(old);
//    }
//
//    @And("private client {string} exists")
//    public void privateClientExists(String email) {
//        // same as corp client
//        corporateClientExists(email);
//    }
//
//    @When("private client's {string} surname is changed to {string}")
//    public void privateClientSSurnameIsChangedTo(String email, String newSurname) {
//        try{
//            PrivateClientDto privateClientDto = userMapper.privateClientToPrivateClientDto((PrivateClient) userRepository.findByEmail(email).get());
//            oldSurname = privateClientDto.getSurname();
//            privateClientDto.setSurname(newSurname);
//            // Used findByEmail in step before
//            ResultActions resultActions = mockMvc.perform(
//                    put("/api/users/update/private-client")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(privateClientDto))
//                            .header("Authorization", "Bearer " + testsState.getJwtToken())
//            ).andExpect(status().isOk());
//        }
//        catch (Exception e){
//            fail(e.getMessage());
//        }
//    }
//
//    @Then("private client's {string} surname in database is {string}")
//    public void privateClientSSurnameInDatabaseIs(String email, String newSurname) {
//        try{
//            assertEquals(((PrivateClient) userRepository.findByEmail(email).get()).getSurname(), newSurname);
//        }
//        catch (Exception e){
//            fail(e.getMessage());
//        }
//
//        // Reverse change (maybe needed)
//        var old = userRepository.findByEmail(email).get();
//        ((PrivateClient) old).setSurname(oldSurname);
//        userRepository.save(old);
//    }
//
//    @When("user {string} is deleted")
//    public void userIsDeleted(String email) {
//        try{
//            ResultActions resultActions = mockMvc.perform(
//                    delete("/api/users/delete/" + email)
//                            .header("Authorization", "Bearer " + testsState.getJwtToken())
//            ).andExpect(status().isOk());
//        }
//        catch (Exception e){
//            cleanup(email);
//            fail(e.getMessage());
//        }
//    }
//
//    @Then("user {string} is not in database")
//    public void userIsNotInDatabase(String email) {
//        try
//        {
//            assertTrue(userRepository.findByEmail(email).isEmpty());
//        }
//        catch (Exception e){
//            cleanup(email);
//            fail(e.getMessage());
//        }
//    }
//
//    @And("new user {string} is added")
//    public void newUserIsAdded(String email) {
//        try{
//            var role = roleRepository.findByRoleType(RoleType.USER);
//
//            User user = new User();
//            user.setEmail(email);
//            user.setUsername(email);
//            user.setDateOfBirth(511739146L);
//            user.setPhone("+38111236456");
//            user.setAddress("Pariske komune 5, Beograd, Srbija");
//            user.setRole(role.get());
//            userRepository.save(user);
//        }
//        catch (Exception e){
//            fail(e.getMessage());
//        }
//    }
//
//    private void cleanup(String email){
//        // only cleanup for new user
//        try{
//            if(userRepository.findByEmail(email).isPresent())
//                userRepository.delete(userRepository.findByEmail(email).get());
//        }
//        catch (Exception e){
//            fail(e.getMessage());
//        }
//    }
//}
