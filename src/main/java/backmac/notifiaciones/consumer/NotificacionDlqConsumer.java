package backmac.notifiaciones.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class NotificacionDlqConsumer {

    @RabbitListener(queues = "${app.rabbitmq.queues.dlq:notificaciones.dlq}")
    public void procesarMensajeFallido(Message message,
                                       Channel channel,
                                       @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        String cuerpo = new String(message.getBody(), StandardCharsets.UTF_8);
        log.warn("[RABBITMQ-DLQ] Mensaje en Dead Letter Queue detectado y registrado para auditoría: {}", cuerpo);

        // Confirmamos para vaciar la DLQ después de auditar
        channel.basicAck(deliveryTag, false);
    }
}
