package rs.edu.raf.NotificationService.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
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
    public Queue userProfileActivationCodeQueue() {
        return new Queue("user-profile-activation-code", false);
    }

    @Bean
    public Queue passwordForgotQueue() {
        return new Queue("password-forgot", false);
    }

    @Bean
    public Queue transactionVerification() {
        return new Queue("transaction-verification", false);
    }
    @Bean
    public Queue marginCallViolation(){return new Queue("margin-call-violation",false);}

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }
}
