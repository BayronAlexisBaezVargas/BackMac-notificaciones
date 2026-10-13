package backmac.notifiaciones.consumer;

import backmac.notifiaciones.dto.EnviarNotificacionDTO;
import backmac.notifiaciones.service.NotificacionService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificacionConsumer {

    private final NotificacionService notificacionService;

    @RabbitListener(queues = "${app.rabbitmq.queues.notificaciones:notificaciones.queue}")
    public void recibirNotificacion(EnviarNotificacionDTO dto,
                                    Channel channel,
                                    @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            log.info("[RABBITMQ-CONSUMER] Mensaje recibido: Usuario={}, Pedido={}, Tipo={}",
                    dto.getUsuarioId(), dto.getPedidoId(), dto.getTipo());

            if (dto.getUsuarioId() == null || dto.getMensaje() == null) {
                throw new IllegalArgumentException("Payload inválido en notificación recibida");
            }

            // Procesar guardado en base de datos
            notificacionService.enviar(dto);

            // Confirmación explícita (ACK)
            channel.basicAck(deliveryTag, false);
            log.info("[RABBITMQ-CONSUMER] ACK enviado con éxito para pedido ID: {}", dto.getPedidoId());

        } catch (Exception e) {
            log.error("[RABBITMQ-CONSUMER] Error procesando notificación: {}. Enviando a DLQ...", e.getMessage());
            // Rechazo explícito con requeue = false para redirigir a DLQ
            channel.basicNack(deliveryTag, false, false);
        }
    }
}
