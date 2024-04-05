package rs.edu.raf.NotificationService.unit.listenertest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.fail;

@Disabled
@SpringBootTest
@EnableRabbit
public class RabbitMQListenersSpringTest {

    @Autowired
    private RabbitTemplate template;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void passwordActivationTest() {
//        PasswordActivationDto dto = new PasswordActivationDto("email@gmail.com", "localhost://link");
//        try {
//            String json = objectMapper.writeValueAsString(dto);
//            template.convertAndSend("password-activation", json);
//            Thread.sleep(2000);
//        } catch (JsonProcessingException | InterruptedException e) {
//            fail(e.getMessage());
//        }

    }

    @Test
    void passwordChangeTest() {
//        PasswordChangeDto dto = new PasswordChangeDto("email@gmail.com", "localhost://link");
//        try {
//            String json = objectMapper.writeValueAsString(dto);
//            template.convertAndSend("password-change", json);
//            Thread.sleep(2000);
//        } catch (JsonProcessingException | InterruptedException | ConstraintViolationException e) {
//            fail(e.getMessage());
//        }

    }

    @Test
    void userProfileActivationCodeTest() {
//        ProfileActivationCodeDto dto = new ProfileActivationCodeDto("email@gmail.com", String.valueOf(1234567890));
//        try {
//            String json = objectMapper.writeValueAsString(dto);
//            template.convertAndSend("user-profile-activation-code", json);
//            Thread.sleep(2000);
//        } catch (JsonProcessingException | InterruptedException | ConstraintViolationException e) {
//            fail(e.getMessage());
//        }

    }


}
