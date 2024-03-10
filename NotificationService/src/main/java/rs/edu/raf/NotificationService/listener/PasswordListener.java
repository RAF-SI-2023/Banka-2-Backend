package rs.edu.raf.NotificationService.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PasswordListener {

    @RabbitListener(queues = "password-activation")
    public void passwordActivationHandler(Message message) {

    }

    @RabbitListener(queues = "password-change")
    public void passwordChangeHandler(Message message) {

    }

    @RabbitListener(queues = "password-forgot")
    public void passwordForgotHandler(Message message) {

    }

}
