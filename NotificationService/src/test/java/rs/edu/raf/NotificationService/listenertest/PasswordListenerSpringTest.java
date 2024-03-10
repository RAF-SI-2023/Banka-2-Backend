package rs.edu.raf.NotificationService.listenertest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import rs.edu.raf.NotificationService.data.dto.PasswordActivationDto;
import rs.edu.raf.NotificationService.data.dto.PasswordChangeDto;


import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@EnableRabbit
public class PasswordListenerSpringTest {

    @Autowired
    private RabbitTemplate template;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void passwordActivationTest(){
        PasswordActivationDto dto = new PasswordActivationDto("email@gmail.com", "localhost://link");
        try {
            String json = objectMapper.writeValueAsString(dto);
            template.convertAndSend( "password-activation", json);
            Thread.sleep(2000);
        } catch (JsonProcessingException | InterruptedException e) {
            fail(e.getMessage());
        }

    }

    @Test
    void passwordChangeTest(){
        PasswordChangeDto dto = new PasswordChangeDto("email@gmail.com", "localhost://link");
        try {
            String json = objectMapper.writeValueAsString(dto);
            template.convertAndSend( "password-change", json);
            Thread.sleep(2000);
        } catch (JsonProcessingException | InterruptedException e) {
            fail(e.getMessage());
        }

    }




}
