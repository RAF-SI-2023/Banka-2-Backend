package rs.edu.raf.IAMService.e2e.usercontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.SneakyThrows;
import org.checkerframework.checker.units.qual.A;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.IAMService.data.dto.ActivationRequestDto;
import rs.edu.raf.IAMService.data.dto.AgentDto;
import rs.edu.raf.IAMService.data.dto.ChangePasswordDto;
import rs.edu.raf.IAMService.data.dto.EmployeeDto;
import rs.edu.raf.IAMService.data.entites.Employee;
import rs.edu.raf.IAMService.data.entites.User;
import rs.edu.raf.IAMService.e2e.generators.JwtTokenGenerator;
import rs.edu.raf.IAMService.mapper.UserMapper;
import rs.edu.raf.IAMService.services.UserService;

import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTestSteps extends UserControllerTestConfig{

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserControllerStateTests tests;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;


    private MvcResult mvcResult;



    String email;
    EmployeeDto employeeDto = new EmployeeDto();
    Long id;
    ChangePasswordDto changePasswordDto = new ChangePasswordDto();
    ResponseEntity<ChangePasswordDto> entity;

    AgentDto agentDto = new AgentDto();

    //get user email
    @Given("user gives paramethers email {string}")
    public void user_paramethers_email(String string){
        email = string;
        tests.jwt = JwtTokenGenerator.generateToken(1L, "vasa_email_adresa_1@gmail.com", "ADMIN", "");
    }

    @SneakyThrows
    @When("user send request for geting user")
    public void user_send(){
        ResultActions resultActions =  mockMvc.perform(get("/api/users/email/{email}", email)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + tests.jwt)
                ).andExpect(status().isOk());
        mvcResult = resultActions.andReturn();
    }

    @Then("response status ok and body user")
    public void response_status_ok(){
        assert mvcResult.getResponse() != null;
        assert HttpStatus.OK.value() == mvcResult.getResponse().getStatus();
    }

    //change password
    @Given("user gives paramethers email {string} and password {string}")
    public void user_set_parameters(String email, String password){
        changePasswordDto.setEmail(email);
        changePasswordDto.setPassword(password);
        tests.jwt = JwtTokenGenerator.generateToken(1L, "vasa_email_adresa_1@gmail.com", "ADMIN", "");
    }

    @SneakyThrows
    @When("user send request for changing password")
    public void user_send_req(){
        ResultActions resultActions =  mockMvc.perform(post("/api/users/password-change")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + tests.jwt)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changePasswordDto))
        ).andExpect(status().isOk());
        mvcResult = resultActions.andReturn();

    }

    @Then("response status ok and body true")
    public void response_status(){
        assert mvcResult.getResponse() != null;
    }

//    //create employee
//    @Given("user gives valid paramethers for employee email {string}")
//    public void user_gives_valid_paramethers_for_employee_email_and_id(String string) {
//       employeeDto.setEmail(string);
//       employeeDto.setId(3L);
//       tests.jwt = JwtTokenGenerator.generateToken(1L, "vasa_email_adresa_1@gmail.com", "ADMIN", "");
//    }
//
//    @When("user send request")
//    public void user_send_request() throws Exception {
//        ResultActions resultActions =  mockMvc.perform(post("/api/users/create/employee")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .header("Authorization", "Bearer " + tests.jwt)
//                .accept(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(employeeDto))
//        ).andExpect(status().isOk());
//        mvcResult = resultActions.andReturn();
//    }
//    @Then("status is ok returns as resonse and body id")
//    public void status_is_ok_returns_as_resonse_and_body_id() {
//        //assert
//    }
//
//
//
//    //create agent
//    @Given("user gives valid paramethers for agent email {string}")
//    public void userGivesValidParamethersForAgentEmailAndId(String arg0) {
//        agentDto.setEmail(arg0);
//        tests.jwt = JwtTokenGenerator.generateToken(1L, "vasa_email_adresa_1@gmail.com", "ADMIN", "");
//    }
//
//    @SneakyThrows
//    @When("user send request to create agent")
//    public void userSendRequestToCreateAgent() {
//
//        ResultActions resultActions =  mockMvc.perform(post("/api/users/create/agent")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .header("Authorization", "Bearer " + tests.jwt)
//                .accept(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(agentDto))
//        ).andExpect(status().isOk());
//        mvcResult = resultActions.andReturn();
//    }
//
//    @Then("status is ok returns as resonse and body id for agent create")
//    public void statusIsOkReturnsAsResonseAndBodyIdForAgentCreate() {
//        //assert
//    }


    //find by id
    @Given("user gives paramethers id {string}")
    public void userGivesParamethersId(String arg0) {
        id = Long.valueOf(Integer.parseInt(arg0));
        tests.jwt = JwtTokenGenerator.generateToken(1L, "vasa_email_adresa_1@gmail.com", "ADMIN", "");
    }

    @SneakyThrows
    @When("user send request to get user")
    public void userSendRequestToGetUser() {
        ResultActions resultActions =  mockMvc.perform(get("/api/users/id/{id}", id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + tests.jwt)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        mvcResult = resultActions.andReturn();
    }

    @Then("response status ok and body user for user geting id")
    public void responseStatusOkAndBodyUserForUserGetingId() {
       assert mvcResult.getResponse() != null;
    }
}
