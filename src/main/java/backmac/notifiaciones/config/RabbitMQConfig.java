package backmac.notifiaciones.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    @Value("${app.rabbitmq.exchange:pedidos360.exchange}")
    private String exchangeName;

    @Value("${app.rabbitmq.dlx:pedidos360.dlx}")
    private String dlxExchangeName;

    @Value("${app.rabbitmq.queues.notificaciones:notificaciones.queue}")
    private String notificacionesQueueName;

    @Value("${app.rabbitmq.queues.dlq:notificaciones.dlq}")
    private String dlqQueueName;

    @Value("${app.rabbitmq.routing-keys.notificaciones:notificaciones.routing.key}")
    private String notificacionesRoutingKey;

    @Value("${app.rabbitmq.routing-keys.dlq:notificaciones.dlq.routing.key}")
    private String dlqRoutingKey;

    // ==========================================
    // EXCHANGES
    // ==========================================
    @Bean
    public TopicExchange appExchange() {
        return new TopicExchange(exchangeName, true, false);
    }

    @Bean
    public TopicExchange dlxExchange() {
        return new TopicExchange(dlxExchangeName, true, false);
    }

    // ==========================================
    // QUEUES (Principal con DLQ y DLQ)
    // ==========================================
    @Bean
    public Queue notificacionesQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", dlxExchangeName);
        args.put("x-dead-letter-routing-key", dlqRoutingKey);
        return new Queue(notificacionesQueueName, true, false, false, args);
    }

    @Bean
    public Queue dlqQueue() {
        return new Queue(dlqQueueName, true, false, false);
    }

    // ==========================================
    // BINDINGS
    // ==========================================
    @Bean
    public Binding notificacionesBinding() {
        return BindingBuilder
                .bind(notificacionesQueue())
                .to(appExchange())
                .with(notificacionesRoutingKey);
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder
                .bind(dlqQueue())
                .to(dlxExchange())
                .with(dlqRoutingKey);
    }

    // ==========================================
    // SERIALIZADOR JSON & TEMPLATE
    // ==========================================
    @Bean
    public MessageConverter jacksonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jacksonMessageConverter());
        return template;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        return admin;
    }
}
