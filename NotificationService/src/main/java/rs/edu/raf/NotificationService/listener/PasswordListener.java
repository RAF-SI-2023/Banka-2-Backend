package rs.edu.raf.NotificationService.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import rs.edu.raf.NotificationService.data.dto.EmailDto;
import rs.edu.raf.NotificationService.data.dto.PasswordActivationDto;
import rs.edu.raf.NotificationService.data.dto.PasswordChangeDto;
import rs.edu.raf.NotificationService.mapper.EmailDtoMapper;

import java.io.IOException;


@Component
public class PasswordListener {

    private final Logger logger = LoggerFactory.getLogger(PasswordListener.class);
    private final ObjectMapper objectMapper;
    private final EmailDtoMapper emailDtoMapper;

    public PasswordListener(ObjectMapper objectMapper, EmailDtoMapper emailDtoMapper) {
        this.objectMapper = objectMapper;
        this.emailDtoMapper = emailDtoMapper;
    }

    @RabbitListener(queues = "password-activation")
    public void passwordActivationHandler(Message message) throws IOException {

        PasswordActivationDto passwordActivationDto = objectMapper.readValue(message.getBody(), PasswordActivationDto.class);
        EmailDto activationEmail = emailDtoMapper.activationEmail(passwordActivationDto);
        System.out.println(activationEmail);
        logger.info("passwordActivationListener received message: " + passwordActivationDto);

    }

    @RabbitListener(queues = "password-change")
    public void passwordChangeHandler(Message message) throws IOException {
        PasswordChangeDto passwordChangeDto = objectMapper.readValue(message.getBody(), PasswordChangeDto.class);
        EmailDto passwordChangeEmail = emailDtoMapper.changePasswordEmail(passwordChangeDto);
        System.out.println(passwordChangeEmail);
        logger.info("passwordChangeListener received message: " + passwordChangeDto);
    }

    @RabbitListener(queues = "password-forgot")
    public void passwordForgotHandler(Message message) {
        logger.info("passwordForgotListener received message");
    }

}
