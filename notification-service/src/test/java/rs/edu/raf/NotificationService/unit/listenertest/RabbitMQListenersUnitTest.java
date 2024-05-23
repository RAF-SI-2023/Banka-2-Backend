package rs.edu.raf.NotificationService.unit.listenertest;

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
import rs.edu.raf.NotificationService.data.dto.EmailDto;
import rs.edu.raf.NotificationService.data.dto.PasswordActivationDto;
import rs.edu.raf.NotificationService.data.dto.PasswordChangeDto;
import rs.edu.raf.NotificationService.data.dto.ProfileActivationCodeDto;
import rs.edu.raf.NotificationService.listener.RabbitMQListeners;
import rs.edu.raf.NotificationService.mapper.EmailDtoMapper;
import rs.edu.raf.NotificationService.services.EmailService;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RabbitMQListenersUnitTest {
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
        String email = "email@gmail.com";
        String subject = "Subject";
        String url = "http://localhost:8000/link";
        PasswordActivationDto passwordActivationDto = new PasswordActivationDto(email, subject, url);

        try {
            rabbitMQListeners.passwordActivationHandler(passwordActivationDto);
            verify(emailDtoMapper).activationEmail(passwordActivationDto);
            verify(emailService).sendSimpleMailMessage(email, subject, url);
        } catch (IOException e) {
            fail(e.getMessage());
        }

    }

    @Test
    void passwordChangeValidInput() {
        String email = "email@gmail.com";
        String subject = "Subject";
        String url = "http://localhost:8000/link";
        PasswordChangeDto passwordChangeDto = new PasswordChangeDto(email, subject, url);

        try {
            rabbitMQListeners.passwordChangeHandler(passwordChangeDto);
            verify(emailDtoMapper).changePasswordEmail(passwordChangeDto);
        } catch (IOException e) {
            fail(e.getMessage());
        }

    }

    @Test
    void userProfileActivationCodeValidInput() {

        String email = "email@gmail.com";
        String subject = "Subject";
        String code = "123456789";

        ProfileActivationCodeDto profileActivationCodeDto = new ProfileActivationCodeDto(email, subject, code);
        EmailDto emailDto = emailDtoMapper.profileActivationEmail(profileActivationCodeDto);

        try {
            rabbitMQListeners.userProfileActivationCodeHandler(profileActivationCodeDto);
            verify(emailService).sendSimpleMailMessage(emailDto.getEmail(), emailDto.getSubject(), emailDto.getContent());
        } catch (IOException e) {
            fail(e.getMessage());
        }

    }

}
