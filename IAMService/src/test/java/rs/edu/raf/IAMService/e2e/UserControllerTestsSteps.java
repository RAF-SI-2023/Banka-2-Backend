package rs.edu.raf.IAMService.e2e;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import rs.edu.raf.IAMService.IAMTestConfig;
import rs.edu.raf.IAMService.data.dto.*;
import rs.edu.raf.IAMService.data.entites.*;
import rs.edu.raf.IAMService.data.enums.RoleType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;
import rs.edu.raf.IAMService.mapper.UserMapper;
import rs.edu.raf.IAMService.repositories.UserRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class UserControllerTestsSteps extends IAMTestConfig {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IAMTestsState testsState;

    @Autowired
    private UserRepository userRepository;

    private UserMapper userMapper;


    UserDto userDto;
    Long employeeId;
    Long corporateClientId;
    Long privateClientId;
    List<UserDto> users;

    @Given("admin logs in")
    public void adminLogsIn() {
        // BootstrapData adds admin
        try{
            ResultActions resultActions = mockMvc.perform(
                    post("api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .content("{\"email\":\"vasa_email_adresa_1@gmail.com\",\"password\":\"admin\"}")
            ).andExpect(status().isOk());

            MvcResult mvcResult = resultActions.andReturn();
            String loginResponse = mvcResult.getResponse().getContentAsString();
            TokenDto tokenDto = objectMapper.readValue(loginResponse, TokenDto.class);
            testsState.setJwtToken(tokenDto.getToken());
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @And("agent {string} exists")
    public void agentExists(String agentEmail) {
        // BootstrapData adds admin
        try{
            ResultActions resultActions = mockMvc.perform(
                    get("api/users/email/" + agentEmail)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
            ).andExpect(status().isOk());

            MvcResult mvcResult = resultActions.andReturn();
            String jsonUserDto = mvcResult.getResponse().getContentAsString();
            UserDto userDto = objectMapper.readValue(jsonUserDto, UserDto.class);
            assertEquals(userDto.getRole(), RoleType.AGENT);
            this.userDto = userDto;
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @When("agent's {string} left limit is reset")
    public void agentSLeftLimitIsReset(String agentEmail) {
        try{
            // Used findByEmail in step before
            ResultActions resultActions = mockMvc.perform(
                    patch("api/users/agent-limit/reset/" + userDto.getId().toString())
                            .header("Authorization", "Bearer " + testsState.getJwtToken())
            ).andExpect(status().isOk());
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Then("agent's {string} left limit is same as their max limit")
    public void agentSLeftLimitIsSameAsTheirMaxLimit(String agentEmail) {
        try{
            String jsonResponse = mockMvc.perform(get("/api/users/agent-limit/" + userDto.getId().toString())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            BigDecimal value = new BigDecimal(0); // Default value if BigDecimal is not found

            value = new BigDecimal(JsonPath.parse(jsonResponse).read("$.value").toString());

            Agent agent = (Agent) userRepository.findById(userDto.getId()).get();

            // Left Limit (value) should be equal to getLimit()
            assertEquals(agent.getLimit(), value);

        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @And("employee exists")
    public void employeeExists() {
        try{
            // add temp employee to userRepo
            Employee employee1 = new Employee();
            employee1.setEmail("testEmployee@gmail.com");
            employee1.setActive(false);
            employee1.setUsername("testEmployee");
            employee1.setPassword("password");
            employee1.setPhone("55555555");
            employee1.setRole(new Role(RoleType.EMPLOYEE));
            // employee1.setPermissions(List.of(per1, per2));
            employeeId = userRepository.save(employee1).getId();
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @When("employee is activated")
    public void employeeIsActivated() {
        try{
            // Used findByEmail in step before
            ResultActions resultActions = mockMvc.perform(
                    put("api/users/employee-activate/" + employeeId.toString())
                            .header("Authorization", "Bearer " + testsState.getJwtToken())
            ).andExpect(status().isOk());
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Then("employee's active status is true")
    public void employeeSActiveStatusIsTrue() {
        try{
            assertTrue(((Employee)userRepository.findById(employeeId).get()).isActive());
        }
        catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @And("activated employee exists")
    public void activatedEmployeeExists() {
        // Prev scenario activated Employee with employeId
    }

    @When("employee is deactivated")
    public void employeeIsDeactivated() {
        try{
            // Used findByEmail in step before
            ResultActions resultActions = mockMvc.perform(
                    put("api/users/employee-deactivate/" + employeeId.toString())
                            .header("Authorization", "Bearer " + testsState.getJwtToken())
            ).andExpect(status().isOk());
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Then("employee's active status is false")
    public void employeeSActiveStatusIsFalse() {
        try{
            assertTrue(! ((Employee)userRepository.findById(employeeId).get()).isActive() );
        }
        catch (Exception e) {
            fail(e.getMessage());
        }

        // remove employee, not needed anymore
        userRepository.deleteById(employeeId);
    }

    @When("employee is deleted")
    public void employeeIsDeleted() {
        // use employe Id
        try{
            // Used findByEmail in step before
            ResultActions resultActions = mockMvc.perform(
                    delete("api/users/delete/" + userRepository.findById(employeeId).get().getEmail())
                            .header("Authorization", "Bearer " + testsState.getJwtToken())
            ).andExpect(status().isOk());
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Then("employee is not in database")
    public void employeeIsNotInDatabase() {
        try{
            var res = userRepository.findById(employeeId);
            assertTrue(res.isEmpty());
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @When("employee's phone number is changed to {string}")
    public void employeeSPhoneNumberIsChangedTo(String newPhoneNumber) {
        // employee is set in before calling this
        // use employe Id
        try{
            EmployeeDto employeeDto = userMapper.employeeToEmployeeDto((Employee) userRepository.findById(employeeId).get());
            employeeDto.setPhone(newPhoneNumber);
            // Used findByEmail in step before
            ResultActions resultActions = mockMvc.perform(
                    put("api/users/update/employee")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(employeeDto))
                            .header("Authorization", "Bearer " + testsState.getJwtToken())
            ).andExpect(status().isOk());
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Then("employee's phone number in database is {string}")
    public void employeeSPhoneNumberInDatabaseIs(String newPhoneNumber) {
        try{
            assertEquals(userRepository.findById(employeeId).get().getPhone(), newPhoneNumber);
        }
        catch (Exception e){
            fail(e.getMessage());
        }

        // remove employee, not needed anymore
        userRepository.deleteById(employeeId);
    }


    @And("corporate client exists")
    public void corporateClientExists() {
        try{
            CorporateClient corporateClient = new CorporateClient();
            corporateClient.setEmail("testCorpClient@gmail.com");
            corporateClient.setUsername("testClient");
            corporateClient.setPassword("password");
            corporateClient.setRole(new Role(RoleType.USER));
            //corporateClient.setPermissions(List.of(per1, per2));
            corporateClient.setName("Micko");
            corporateClient.setPrimaryAccountNumber("0000000000000000");

            corporateClientId = userRepository.save(corporateClient).getId();
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @When("corporate client's username is changed to {string}")
    public void corporateClientSUsernameIsChangedTo(String newName) {
        // client is set in before calling this
        // use client Id
        try{
            CorporateClientDto corporateClientDto = userMapper.corporateClientToCorporateClientDto((CorporateClient) userRepository.findById(corporateClientId).get());
            corporateClientDto.setUsername(newName);
            // Used findByEmail in step before
            ResultActions resultActions = mockMvc.perform(
                    put("api/users/update/corporate-client")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(corporateClientDto))
                            .header("Authorization", "Bearer " + testsState.getJwtToken())
            ).andExpect(status().isOk());
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Then("corporate client's username in database is {string}")
    public void corporateClientSUsernameInDatabaseIs(String newName) {
        try{
            assertEquals(userRepository.findById(corporateClientId).get().getUsername(), newName);
        }
        catch (Exception e){
            fail(e.getMessage());
        }

        // remove corp client, not needed anymore
        userRepository.deleteById(corporateClientId);
    }

    @And("private client exists")
    public void privateClientExists() {
        PrivateClient privateClient = new PrivateClient();
        privateClient.setEmail("testPrivClient@gmail.com");
        privateClient.setUsername("testPrivClient");
        privateClient.setPassword("password");
        privateClient.setRole(new Role(RoleType.USER));
        //privateClient.setPermissions(List.of(per1, per2));
        privateClient.setName("Zvezdan");
        privateClient.setSurname("prezimeNaIc");
        privateClient.setGender("M");
        privateClient.setPrimaryAccountNumber("0000000000001111");

        privateClientId = userRepository.save(privateClient).getId();
    }

    @When("private client's surname is changed to {string}")
    public void privateClientSSurnameIsChangedTo(String newSurname) {
        // client is set in before calling this
        // use client Id
        try{
            PrivateClientDto privateClientDto = userMapper.privateClientToPrivateClientDto((PrivateClient) userRepository.findById(privateClientId).get());
            privateClientDto.setSurname(newSurname);
            // Used findByEmail in step before
            ResultActions resultActions = mockMvc.perform(
                    put("api/users/update/private-client")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(privateClientDto))
                            .header("Authorization", "Bearer " + testsState.getJwtToken())
            ).andExpect(status().isOk());
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Then("private client's surname in database is {string}")
    public void privateClientSSurnameInDatabaseIs(String newSurname) {
        try{
            assertEquals(((PrivateClient)userRepository.findById(privateClientId).get()).getSurname(), newSurname);
        }
        catch (Exception e){
            fail(e.getMessage());
        }

        // remove private client, not needed anymore
        userRepository.deleteById(privateClientId);
    }

    @When("listing all users")
    public void listingAllUsers() {
        try{
            // Used findByEmail in step before
            ResultActions resultActions = mockMvc.perform(
                    get("api/users/all")
                            .header("Authorization", "Bearer " + testsState.getJwtToken()))
                .andExpect(status().isOk());

            MvcResult mvcResult = resultActions.andReturn();
            String str = mvcResult.getResponse().getContentAsString();
            users = objectMapper.readValue(str, new TypeReference<ArrayList<UserDto>>() {});
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Then("confirm admin is present")
    public void confirmAdminIsPresent() {
        try{
            boolean isAdminPresent = false;
            for (UserDto userDto : users) {
                if (userDto.getRole().equals(RoleType.ADMIN)) {
                    // admin is found
                    isAdminPresent = true;
                }
            }
            assertTrue(isAdminPresent);
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }
}
