package rs.edu.raf.NotificationService.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import rs.edu.raf.NotificationService.data.dto.*;
import rs.edu.raf.NotificationService.mapper.EmailDtoMapper;
import rs.edu.raf.NotificationService.services.EmailService;

import java.io.IOException;
import java.util.Set;


@Component
@RequiredArgsConstructor
public class RabbitMQListeners {

    private final Logger logger = LoggerFactory.getLogger(RabbitMQListeners.class);
    private final ObjectMapper objectMapper;
    private final EmailDtoMapper emailDtoMapper;
    private final Validator validator;
    private final EmailService emailService;

    @RabbitListener(queues = "password-activation")
    public void passwordActivationHandler(PasswordActivationDto passwordActivationDto) throws IOException {
        if (!isValid(passwordActivationDto)) return;

        EmailDto activationEmail = emailDtoMapper.activationEmail(passwordActivationDto);

        logger.info("passwordActivationListener received message" + activationEmail);

        emailService.sendSimpleMailMessage(passwordActivationDto.getEmail(), passwordActivationDto.getSubject(), passwordActivationDto.getActivationUrl());

    }

    @RabbitListener(queues = "password-change")
    public void passwordChangeHandler(PasswordChangeDto passwordChangeDto) throws IOException {
        if (!isValid(passwordChangeDto)) return;

        EmailDto passwordChangeEmail = emailDtoMapper.changePasswordEmail(passwordChangeDto);

        logger.info("passwordChangeListener received message: " + passwordChangeEmail);
    }

    @RabbitListener(queues = "user-profile-activation-code")
    public void userProfileActivationCodeHandler(ProfileActivationCodeDto profileActivationCodeDto) throws IOException {

        if (!isValid(profileActivationCodeDto)) return;

        EmailDto userActivationCodeEmail = emailDtoMapper.profileActivationEmail(profileActivationCodeDto);

        emailService.sendSimpleMailMessage(userActivationCodeEmail.getEmail(), userActivationCodeEmail.getSubject(), userActivationCodeEmail.getContent());

        logger.info("userProfileActivationCodeListener received message: " + userActivationCodeEmail);
    }

    @RabbitListener(queues = "password-forgot")
    public void passwordForgotHandler(Message message) {
        logger.info("passwordForgotListener received message");
    }

    @RabbitListener(queues = "transaction-verification")
    public void transactionVerification(TransferTransactionVerificationDto transferTransactionVerificationDto) throws IOException {

        if (!isValid(transferTransactionVerificationDto)) return;

        EmailDto transactionVerification = emailDtoMapper.transactionVerification(transferTransactionVerificationDto);

        emailService.sendSimpleMailMessage(transactionVerification.getEmail(), transactionVerification.getSubject(), transactionVerification.getContent());

        logger.info("transaction-verification received message: " + transactionVerification);
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
