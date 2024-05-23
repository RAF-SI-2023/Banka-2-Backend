package rs.edu.raf.BankService.configuration;

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
    public Queue activationCodeQueue() {
        return new Queue("user-profile-activation-code", false);
    }

    @Bean
    public Queue userProfileUserAccountBindingConfirmation() { return new Queue("account-link-confirmation", false); }

    @Bean
    public Queue transactionVerificationtQueue() {
        return new Queue("transaction-verification", false);
    }

    @Bean
    public Queue transactionVerification() {
        return new Queue("transaction-verification", false);
    }

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
