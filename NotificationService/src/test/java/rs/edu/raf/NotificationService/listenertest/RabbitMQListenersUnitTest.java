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
import rs.edu.raf.NotificationService.services.EmailService;

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
    @Mock
    private Logger logger;
    @Spy
    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();
    @Spy
    private EmailDtoMapper emailDtoMapper;
    @Mock
    private EmailService emailService;
    @InjectMocks
    private RabbitMQListeners rabbitMQListeners;

    @Test
    void passwordActivationValidInput() {

//        String validJson = generateJson("email", "activationUrl", validEmail, validUrl);
//        PasswordActivationDto passwordActivationDto = new PasswordActivationDto(validEmail, validUrl);
//
//        try {
//            rabbitMQListeners.passwordActivationHandler(passwordActivationDto);
//            verify(emailDtoMapper).activationEmail(passwordActivationDto);
//        } catch (IOException e) {
//            fail(e.getMessage());
//        }

    }

    @Test
    void passwordChangeValidInput() {

//        String validJson = generateJson("email", "urlLink", validEmail, validUrl);
//        PasswordChangeDto passwordChangeDto = new PasswordChangeDto(validEmail, validUrl);
//
//        try {
//            rabbitMQListeners.passwordChangeHandler(passwordChangeDto);
//            verify(emailDtoMapper).changePasswordEmail(passwordChangeDto);
//        } catch (IOException e) {
//            fail(e.getMessage());
//        }

    }

    @Test
    void userProfileActivationCodeValidInput() {

//        ProfileActivationCodeDto profileActivationCodeDto = new ProfileActivationCodeDto(validEmail, "1234567");
//
//        try {
//            rabbitMQListeners.userProfileActivationCodeHandler(profileActivationCodeDto);
//            verify(emailDtoMapper).profileActivationEmail(profileActivationCodeDto);
//        } catch (IOException e) {
//            fail(e.getMessage());
//        }

    }

    @Test
    void passwordActivationInvalidInputs() {
//        List<PasswordActivationDto> invalidDtos = new ArrayList<>();
//        invalidDtos.add(new PasswordActivationDto("", "url"));
//        invalidDtos.add(new PasswordActivationDto("email@gmail.com", ""));
//        invalidDtos.add(new PasswordActivationDto(null, "url"));
//        invalidDtos.add(new PasswordActivationDto("email@gmail.com", null));
//        try {
//            for (PasswordActivationDto passwordActivationDto : invalidDtos) {
//                rabbitMQListeners.passwordActivationHandler(passwordActivationDto);
//                verify(emailDtoMapper, never()).activationEmail(any());
//            }
//        } catch (IOException e) {
//            fail(e.getMessage());
//        }
    }

    @Test
    void passwordChangeInvalidInputs() {
//        List<PasswordChangeDto> invalidDtos = new ArrayList<>();
//        invalidDtos.add(new PasswordChangeDto("", "url"));
//        invalidDtos.add(new PasswordChangeDto("email@gmail.com", ""));
//        invalidDtos.add(new PasswordChangeDto(null, "url"));
//        invalidDtos.add(new PasswordChangeDto("email@gmail.com", null));
//
//        try {
//            for (PasswordChangeDto passwordChangeDto : invalidDtos) {
//                rabbitMQListeners.passwordChangeHandler(passwordChangeDto);
//                verify(emailDtoMapper, never()).changePasswordEmail(any());
//            }
//        } catch (IOException e) {
//            fail(e.getMessage());
//        }
    }

    @Test
    void userProfileActivationCodeInvalidInputs() {
//        try {
//            rabbitMQListeners.userProfileActivationCodeHandler(new ProfileActivationCodeDto("", "1234567"));
//            verify(emailDtoMapper, never()).profileActivationEmail(any());
//        } catch (IOException e) {
//            fail(e.getMessage());
//        }
    }

    private String generateJson(String propName1, String propName2, String value1, String value2) {
        return "{\"" + propName1 + "\":\"" + value1 + "\",\"" + propName2 + "\":\"" + value2 + "\"}";
    }

}
