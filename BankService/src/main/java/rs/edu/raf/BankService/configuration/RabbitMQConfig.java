package rs.edu.raf.BankService.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {


    @Bean
    public Queue activationCodeQueue() {
        return new Queue("back-service-activation-code", false);
    }

    @Bean
    public Queue userProfileUserAccountBindingConfirmation() { return new Queue("user-profile-user-account-binding-confirmation", false); }
}
