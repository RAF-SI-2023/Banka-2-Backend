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
import rs.edu.raf.NotificationService.listener.PasswordListener;
import rs.edu.raf.NotificationService.mapper.EmailDtoMapper;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PasswordListenerUnitTest {

    @Mock
    private Logger logger;
    @Spy
    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();
    @Spy
    private EmailDtoMapper emailDtoMapper;
    @InjectMocks
    private PasswordListener passwordListener;


    @Test
    void passwordActivationValidInput(){
        //setup
        String email = "test@example.com";
        String activationUrl = "http://example.com/activate";
        String validJson = "{\"email\":\"" + email + "\",\"activationUrl\":\"" + activationUrl + "\"}";
        PasswordActivationDto passwordActivationDto = new PasswordActivationDto(email, activationUrl);

        Message validMessage = createMockMessage(validJson);

        try {
            passwordListener.passwordActivationHandler(validMessage);
            verify(emailDtoMapper).activationEmail(passwordActivationDto);
        } catch (IOException e) {
            fail(e.getMessage());
        }

    }

    @Test
    void passwordActivationInvalidInputs(){
        String validEmail = "test@example.com";
        String blankEmail = "";
        String invalidEmail = "test";
        String blankActivationUrl = "";
        String validActivationUrl = "http://example.com/activate";
        String invalidJson1 = "{\"email\":\"" + blankEmail + "\",\"activationUrl\":\"" + validActivationUrl + "\"}";
        String invalidJson2 = "{\"email\":\"" + invalidEmail + "\",\"activationUrl\":\"" + validActivationUrl + "\"}";
        String invalidJson3 = "{\"email\":\"" + validEmail + "\",\"activationUrl\":\"" + blankActivationUrl + "\"}";
        String invalidJson4 = "{\"email\":\"" + blankEmail + "\",\"activationUrl\":\"" + blankActivationUrl + "\"}";

        try {
            Message invalidMessage1 = createMockMessage(invalidJson1);
            passwordListener.passwordActivationHandler(invalidMessage1);
            verify(emailDtoMapper, never()).activationEmail(any(PasswordActivationDto.class));

            Message invalidMessage2 = createMockMessage(invalidJson2);
            passwordListener.passwordActivationHandler(invalidMessage2);
            verify(emailDtoMapper, never()).activationEmail(any(PasswordActivationDto.class));

            Message invalidMessage3 = createMockMessage(invalidJson3);
            passwordListener.passwordActivationHandler(invalidMessage3);
            verify(emailDtoMapper, never()).activationEmail(any(PasswordActivationDto.class));

            Message invalidMessage4 = createMockMessage(invalidJson4);
            passwordListener.passwordActivationHandler(invalidMessage4);
            verify(emailDtoMapper, never()).activationEmail(any(PasswordActivationDto.class));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    private Message createMockMessage(String content){
        byte[] body = content.getBytes();
        return new Message(body);
    }

}
