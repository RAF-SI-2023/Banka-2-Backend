package rs.edu.raf.IAMService.e2e.auth;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import rs.edu.raf.IAMService.data.dto.LoginDto;
import rs.edu.raf.IAMService.data.dto.TokenDto;


public class AuthControllerTestsSteps extends AuthControllerTests {


    private RestTemplate restTemplate = new RestTemplateBuilder().build();

    private ResponseEntity<TokenDto> response;
    private LoginDto loginDto;

    @Given("a valid login request with email {string} and password {string}")
    public void aValidLoginRequestWithEmailAndPassword(String email, String password) {
        loginDto = new LoginDto(email, password);
    }

    @Given("an invalid login request with email {string} and password {string}")
    public void anInvalidLoginRequestWithEmailAndPassword(String email, String password) {
        loginDto = new LoginDto(email, password);
    }


    @When("the user logs in")
    public void theUserLogsIn() {
        String url = "http://localhost:8000/api/auth/login";
        try {
            response = restTemplate.postForEntity(url, loginDto, TokenDto.class);
        } catch (HttpClientErrorException.Unauthorized e) {
            // Handle unauthorized exception here
            response = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Then("a valid JWT token is returned")
    public void aValidJWTTokenIsReturned() {
        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() != null;
        assert response.getBody().getToken() != null;
    }

    @Then("a {int} unauthorized response is returned")
    public void anUnauthorizedResponseIsReturned(int statusCode) {
        assert response.getStatusCode() == HttpStatus.UNAUTHORIZED;
    }

}
