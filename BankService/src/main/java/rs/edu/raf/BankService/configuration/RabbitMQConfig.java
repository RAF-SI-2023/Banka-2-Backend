package rs.edu.raf.BankService.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {


    @Bean
    public Queue activationCodeQueue() {
        return new Queue("user-profile-activation-code", false);
    }

    @Bean
    public Queue userProfileUserAccountBindingConfirmation() { return new Queue("account-link-confirmation", false); }
}
