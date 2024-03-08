package rs.edu.raf.IAMService.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {


    @Bean
    public Queue passwordActivationQueue() {
        return new Queue("password-activation", false);
    }

    @Bean
    public Queue passwordChageQueue() {
        return new Queue("password-change", false);
    }

    @Bean
    public Queue passwordForgotQueue() {
        return new Queue("password-forgot", false);
    }

}
