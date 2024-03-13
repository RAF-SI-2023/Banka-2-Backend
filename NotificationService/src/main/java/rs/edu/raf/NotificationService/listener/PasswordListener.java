package rs.edu.raf.NotificationService.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import rs.edu.raf.NotificationService.data.dto.EmailDto;
import rs.edu.raf.NotificationService.data.dto.PasswordActivationDto;
import rs.edu.raf.NotificationService.data.dto.PasswordChangeDto;
import rs.edu.raf.NotificationService.data.dto.ProfileActivationCodeDto;
import rs.edu.raf.NotificationService.mapper.EmailDtoMapper;

import java.io.IOException;
import java.util.Set;


@Component
public class PasswordListener {

    private final Logger logger = LoggerFactory.getLogger(PasswordListener.class);
    private final ObjectMapper objectMapper;
    private final EmailDtoMapper emailDtoMapper;
    private final Validator validator;

    public PasswordListener(ObjectMapper objectMapper, EmailDtoMapper emailDtoMapper, Validator validator) {
        this.objectMapper = objectMapper;
        this.emailDtoMapper = emailDtoMapper;
        this.validator = validator;
    }

    @RabbitListener(queues = "password-activation")
    public void passwordActivationHandler(Message message) throws IOException {
        if (message == null) return;

        PasswordActivationDto passwordActivationDto = objectMapper.readValue(message.getBody(), PasswordActivationDto.class);
        if (!isValid(passwordActivationDto)) return;

        EmailDto activationEmail = emailDtoMapper.activationEmail(passwordActivationDto);
        logger.info("passwordActivationListener received message" + activationEmail);

    }

    @RabbitListener(queues = "password-change")
    public void passwordChangeHandler(Message message) throws IOException {
        if (message == null) return;
        PasswordChangeDto passwordChangeDto = objectMapper.readValue(message.getBody(), PasswordChangeDto.class);
        if (!isValid(passwordChangeDto)) return;

        EmailDto passwordChangeEmail = emailDtoMapper.changePasswordEmail(passwordChangeDto);

        logger.info("passwordChangeListener received message: " + passwordChangeEmail);
    }

    @RabbitListener(queues = "user-profile-activation-code")
    public void userProfileActivationCodeHandler(Message message) throws IOException {
        if(message == null) return;
        ProfileActivationCodeDto profileActivationCodeDto = objectMapper.readValue(message.getBody(), ProfileActivationCodeDto.class);
        if (!isValid(profileActivationCodeDto)) return;
        EmailDto userActivationCodeEmail = emailDtoMapper.profileActivationEmail(profileActivationCodeDto);
        logger.info("userProfileActivationCodeListener received message: " + userActivationCodeEmail);
    }

    @RabbitListener(queues = "password-forgot")
    public void passwordForgotHandler(Message message) {
        logger.info("passwordForgotListener received message");
    }

    private <T> boolean isValid(T dto) {
        Set<ConstraintViolation<T>> violations = validator.validate(dto);

        if (violations.isEmpty()) return true;

        StringBuilder sb = new StringBuilder();
        for (ConstraintViolation<T> violation : violations) {
            sb.append(violation.getMessage());
        }
        logger.error(sb.toString(), violations);
        return false;
    }

}
