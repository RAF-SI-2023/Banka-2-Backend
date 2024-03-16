package rs.edu.raf.NotificationService.listenertest;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.amqp.core.Message;
import rs.edu.raf.NotificationService.data.dto.PasswordActivationDto;
import rs.edu.raf.NotificationService.data.dto.PasswordChangeDto;
import rs.edu.raf.NotificationService.data.dto.ProfileActivationCodeDto;
import rs.edu.raf.NotificationService.listener.RabbitMQListeners;
import rs.edu.raf.NotificationService.mapper.EmailDtoMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RabbitMQListenersUnitTest {

    private final String validEmail = "test@example.com";
    private final String validUrl = "http://example.com/activate";
    private final String blankEmail = "";
    private final String invalidEmail = "test";
    private final String blankUrl = "";
    @Mock
    private Logger logger;
    @Spy
    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();
    @Spy
    private EmailDtoMapper emailDtoMapper;
    @InjectMocks
    private RabbitMQListeners rabbitMQListeners;

    @Test
    void passwordActivationValidInput() {

        String validJson = generateJson("email", "activationUrl", validEmail, validUrl);
        PasswordActivationDto passwordActivationDto = new PasswordActivationDto(validEmail, validUrl);

        try {
            rabbitMQListeners.passwordActivationHandler(passwordActivationDto);
            verify(emailDtoMapper).activationEmail(passwordActivationDto);
        } catch (IOException e) {
            fail(e.getMessage());
        }

    }

    @Test
    void passwordChangeValidInput() {

        String validJson = generateJson("email", "urlLink", validEmail, validUrl);
        PasswordChangeDto passwordChangeDto = new PasswordChangeDto(validEmail, validUrl);

        try {
            rabbitMQListeners.passwordChangeHandler(passwordChangeDto);
            verify(emailDtoMapper).changePasswordEmail(passwordChangeDto);
        } catch (IOException e) {
            fail(e.getMessage());
        }

    }

    @Test
    void userProfileActivationCodeValidInput() {

        ProfileActivationCodeDto profileActivationCodeDto = new ProfileActivationCodeDto(validEmail, "1234567");

        try {
            rabbitMQListeners.userProfileActivationCodeHandler(profileActivationCodeDto);
            verify(emailDtoMapper).profileActivationEmail(profileActivationCodeDto);
        } catch (IOException e) {
            fail(e.getMessage());
        }

    }

    @Test
    void passwordActivationInvalidInputs() {
        List<Message> invalidMessages = createInvalidMessages("email", "activationUrl");
        try {
            for (Message message : invalidMessages) {
                rabbitMQListeners.passwordActivationHandler(new PasswordActivationDto("example@raf.rs","url"));
                verify(emailDtoMapper, never()).activationEmail(any());
            }
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void passwordChangeInvalidInputs() {
        List<Message> invalidMessages = createInvalidMessages("email", "urlLink");
        try {
            for (Message message : invalidMessages) {
                rabbitMQListeners.passwordChangeHandler(new PasswordChangeDto("","url"));
                verify(emailDtoMapper, never()).changePasswordEmail(any());
            }
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void userProfileActivationCodeInvalidInputs() {
        try {
            rabbitMQListeners.userProfileActivationCodeHandler(new ProfileActivationCodeDto("", "1234567"));
            verify(emailDtoMapper, never()).profileActivationEmail(any());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    private List<Message> createInvalidMessages(String prop1, String prop2) {
        List<Message> invalidMessages = new ArrayList<>();
        invalidMessages.add(createMockMessage(generateJson(prop1, prop2, blankEmail, validUrl)));
        invalidMessages.add(createMockMessage(generateJson(prop1, prop2, invalidEmail, validUrl)));
        invalidMessages.add(createMockMessage(generateJson(prop1, prop2, validEmail, blankUrl)));
        invalidMessages.add(createMockMessage(generateJson(prop1, prop2, blankEmail, blankUrl)));
        invalidMessages.add(null);
        return invalidMessages;
    }

    private String generateJson(String propName1, String propName2, String value1, String value2) {
        return "{\"" + propName1 + "\":\"" + value1 + "\",\"" + propName2 + "\":\"" + value2 + "\"}";
    }

    private Message createMockMessage(String content) {
        byte[] body = content.getBytes();
        return new Message(body);
    }

}
